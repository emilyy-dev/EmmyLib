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

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class CommandResult {

  private final CommandResult.Type resultType;
  private final Message message;
  private final Collection<CommandSyntaxException> exceptions;

  private CommandResult(final CommandResult.Type resultType, final Message message,
                        final Collection<CommandSyntaxException> exceptions) {
    this.resultType = resultType;
    this.message = message;
    this.exceptions = List.copyOf(exceptions);
  }

  public @Unmodifiable Collection<CommandSyntaxException> getExceptions() {
    return this.exceptions;
  }

  public CommandResult.Type getResultType() {
    return this.resultType;
  }

  public Message getMessage() {
    return this.message;
  }

  public boolean wasSuccessful() {
    return this.resultType.wasSuccessful;
  }

  public boolean wasFailure() {
    return !wasSuccessful();
  }

  public static final class Type {

    public static final Type GENERIC_SUCCESS = new Type(true);
    public static final Type GENERIC_FAILURE = new Type(false);
    public static final Type INVALID_SYNTAX = new Type(false);

    private final boolean wasSuccessful;

    public Type(final boolean wasSuccessful) {
      this.wasSuccessful = wasSuccessful;
    }

    public boolean wasSuccessful() {
      return this.wasSuccessful;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public CommandResult createResult(final @NotNull Message message, final @NotNull Collection<CommandSyntaxException> exceptions) {
      return new CommandResult(this, requireNonNull(message, "message"), requireNonNull(exceptions, "exceptions"));
    }

    @Contract(value = "_ -> fail", pure = true)
    public void throwCustomException(final @NotNull Message message) throws CustomCommandException {
      throw new CustomCommandException(message, this);
    }
  }
}
