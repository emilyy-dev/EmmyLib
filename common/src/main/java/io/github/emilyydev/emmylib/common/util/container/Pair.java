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

package io.github.emilyydev.emmylib.common.util.container;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * A container for two elements.
 * <p>
 * The components are expected to be immutable.
 *
 * @param <T> the type of the first component.
 * @param <U> the type of the second component.
 */
public final class Pair<T, U> {

  public static <T, U> Pair<T, U> pair(final @NotNull T first, final @NotNull U second) {
    return new Pair<>(first, second);
  }

  private final T first;
  private final U second;

  private Pair(final T first, final U second) {
    this.first = requireNonNull(first, "first");
    this.second = requireNonNull(second, "second");
  }

  public T first() {
    return this.first;
  }

  public U second() {
    return this.second;
  }

  @Override
  public String toString() {
    return "Pair["
           + "first=" + this.first
           + ", second=" + this.second
           + ']';
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) { return true; }
    if (other == null || this.getClass() != other.getClass()) { return false; }
    final Pair<?, ?> that = (Pair<?, ?>) other;
    return this.first.equals(that.first) && this.second.equals(that.second);
  }

  @Override
  public int hashCode() {
    return hash(this.first, this.second);
  }
}
