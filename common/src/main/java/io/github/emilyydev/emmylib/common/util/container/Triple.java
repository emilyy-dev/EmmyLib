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
 * A container for three elements.
 * <p>
 * The components are expected to be immutable.
 *
 * @param <T> the type of the first component.
 * @param <U> the type of the second component.
 * @param <V> the type of the third component.
 */
public final class Triple<T, U, V> {

  public static <T, U, V> Triple<T, U, V> triple(final @NotNull T first, final @NotNull U second, final @NotNull V third) {
    return new Triple<>(first, second, third);
  }

  private final T first;
  private final U second;
  private final V third;

  private Triple(final T first, final U second, final V third) {
    this.first = requireNonNull(first, "first");
    this.second = requireNonNull(second, "second");
    this.third = requireNonNull(third, "third");
  }

  public @NotNull T first() {
    return this.first;
  }

  public @NotNull U second() {
    return this.second;
  }

  public @NotNull V third() {
    return this.third;
  }

  @Override
  public String toString() {
    return "Triple["
           + "first=" + this.first
           + ", second=" + this.second
           + ", third=" + this.third
           + ']';
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) { return true; }
    if (other == null || this.getClass() != other.getClass()) { return false; }
    final Triple<?, ?, ?> triple = (Triple<?, ?, ?>) other;
    return this.first.equals(triple.first) && this.second.equals(triple.second) && this.third.equals(triple.third);
  }

  @Override
  public int hashCode() {
    return hash(this.first, this.second, this.third);
  }
}
