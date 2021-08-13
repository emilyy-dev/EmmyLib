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

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Represents a segment of a command
 *
 * @param <S>
 * @param <N>
 */
public interface CommandSegment<S, N extends CommandNode<S>> {

  /**
   * @return
   */
  @NotNull N commandNode();

  /**
   * @param name
   * @return
   */
  default @NotNull LiteralArgumentBuilder<S> literal(final @NotNull String name) {
    return LiteralArgumentBuilder.literal(requireNonNull(name, "name"));
  }

  /**
   * @param name
   * @param type
   * @param <T>
   * @return
   */
  default <T> @NotNull RequiredArgumentBuilder<S, T> argument(final @NotNull String name, final @NotNull ArgumentType<T> type) {
    return RequiredArgumentBuilder.argument(requireNonNull(name, "name"), requireNonNull(type, "type"));
  }

  /**
   * @param <S>
   */
  interface Root<S> extends CommandSegment<S, RootCommandNode<S>> { }

  /**
   * @param <S>
   */
  interface Literal<S> extends CommandSegment<S, LiteralCommandNode<S>> { }

  /**
   * @param <S>
   * @param <T>
   */
  interface Argument<S, T> extends CommandSegment<S, ArgumentCommandNode<S, T>> { }
}
