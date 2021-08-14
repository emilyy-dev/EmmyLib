//
// This file is part of EmmyLib, licensed under the MIT License.
//
// Copyright (c) 2021 emilyy-dev
// Copyright (c) contributors
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package io.github.emilyydev.emmylib.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.github.emilyydev.emmylib.common.locale.ComponentMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * @param <S>
 */
public class CommandHandler<S> implements CommandSegment.Root<S>, AutoCloseable {

  /**
   * Creates a command handler that runs on the same thread commands are sent on immediately,
   * blocking execution until the command is done.
   *
   * @param <S>
   * @return
   */
  @Contract("-> new")
  public static <S> CommandHandler<S> immediateCommandHandler() {
    return new CommandHandler<>(Runnable::run);
  }

  /**
   * @param <S>
   * @return
   */
  @Contract("-> new")
  public static <S> CommandHandler<S> asynchronousQueuedCommandHandler() {
    return new CommandHandler<>(Executors.newSingleThreadExecutor());
  }

  /**
   * @param executor
   * @param <S>
   * @return
   */
  @Contract("_ -> new")
  public static <S> CommandHandler<S> customExecutorCommandHandler(final @NotNull Executor executor) {
    return new CommandHandler<>(requireNonNull(executor));
  }

  private final Executor executor;
  private final AtomicBoolean closedState = new AtomicBoolean(false);
  private final CommandDispatcher<S> dispatcher = new CommandDispatcher<>();
  private final RootCommandNode<S> rootCommandNode = this.dispatcher.getRoot();

  private CommandHandler(final Executor executor) {
    this.executor = executor;
  }

  @Override
  public @NotNull RootCommandNode<S> commandNode() {
    return this.rootCommandNode;
  }

  /**
   * @param segment
   * @return this command handler for chaining method calls
   */
  @Contract("_ -> this")
  public CommandHandler<S> addCommand(final CommandSegment.Literal<S> segment) {
    return addCommand(segment.commandNode());
  }

  /**
   * @param commandNode
   * @return this command handler for chaining method calls
   */
  @Contract("_ -> this")
  public CommandHandler<S> addCommand(final LiteralCommandNode<S> commandNode) {
    if (this.closedState.get()) {
      throw new IllegalStateException("Command handler is closed");
    }
    this.rootCommandNode.addChild(commandNode);
    return this;
  }

  /**
   * @param input
   * @param subject
   * @return
   */
  public Future<CommandResult> execute(final @NotNull String input, final @NotNull S subject) {
    requireNonNull(input, "input");
    requireNonNull(subject, "subject");

    if (this.closedState.get()) {
      throw new IllegalStateException("Command handler is closed");
    }

    final var task = new FutureTask<>(() -> {
      final var parseResults = this.dispatcher.parse(input.trim(), subject);

      if (!parseResults.getExceptions().isEmpty()) {
        return CommandResult.Type.INVALID_SYNTAX.createResult(ComponentMessage.empty(), parseResults.getExceptions().values());
      }

      try {
        this.dispatcher.execute(parseResults);
        return CommandResult.Type.GENERIC_SUCCESS.createResult(ComponentMessage.empty(), List.of());
      } catch (final CustomCommandException exception) {
        return exception.getResultType().createResult(exception.getCommandMessage(), List.of());
      } catch (final CommandSyntaxException exception) {
        return CommandResult.Type.GENERIC_FAILURE.createResult(exception.getRawMessage(), List.of(exception));
      }
    });

    this.executor.execute(task);
    return task;
  }

  /**
   * @param input
   * @param subject
   * @return
   */
  public CompletableFuture<Suggestions> suggestionsFuture(final @NotNull String input, final @NotNull S subject) {
    final var parseResults = this.dispatcher.parse(requireNonNull(input, "input"), requireNonNull(subject, "subject"));
    if (this.closedState.get()) {
      throw new IllegalStateException("Command handler is closed");
    }
    return this.dispatcher.getCompletionSuggestions(parseResults);
  }

  /**
   * @param input
   * @param subject
   * @return
   */
  public @Unmodifiable Iterable<? extends String> completionSuggestions(final @NotNull String input, final @NotNull S subject) {
    if (this.closedState.get()) {
      throw new IllegalStateException("Command handler is closed");
    }
    return suggestionsFuture(input, subject).join().getList().stream()
                                            .map(Suggestion::getText)
                                            .collect(toUnmodifiableList());
  }

  /**
   * Closes the command handler and stops accepting incoming command and suggestion completion
   * requests.
   * <p>
   * Whether possibly queued commands will run or not depends on the executor used.
   * <p>
   * If the executor used is an {@link java.util.concurrent.ExecutorService}, it will <b>not</b> be
   * shut down and awaited.
   * <p>
   * Any calls to {@link #addCommand(Literal)}, {@link #addCommand(LiteralCommandNode)},
   * {@link #execute(String, Object)}, {@link #suggestionsFuture(String, Object)} and
   * {@link #completionSuggestions(String, Object)} will result in an {@link IllegalStateException}
   * being thrown.
   */
  @Override
  public void close() {
    this.closedState.set(true);
  }

  /**
   * If this handler is accepting new command registrations or execution/completion requests.
   *
   * @return {@code true} if this handler is closed.
   */
  public boolean isClosed() {
    return this.closedState.get();
  }
}
