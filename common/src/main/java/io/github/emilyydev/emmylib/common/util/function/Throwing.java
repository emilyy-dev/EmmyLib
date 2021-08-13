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

package io.github.emilyydev.emmylib.common.util.function;

import io.github.emilyydev.emmylib.common.util.MoreUtils;

public interface Throwing {

  @FunctionalInterface
  interface BiConsumer<T, U> {

    static <T, U> java.util.function.BiConsumer<T, U> sneaky(final BiConsumer<T, U> biConsumer) {
      return (t, u) -> MoreUtils.acceptThrowing(biConsumer, t, u);
    }

    void accept(T t, U u) throws Exception;

    default java.util.function.BiConsumer<T, U> sneaky() {
      return sneaky(this);
    }
  }

  @FunctionalInterface
  interface Function<T, R> {

    static <T, R> java.util.function.Function<T, R> sneaky(final Function<T, R> function) {
      return t -> MoreUtils.applyThrowing(function, t);
    }

    R apply(T t) throws Exception;

    default java.util.function.Function<T, R> sneaky() {
      return sneaky(this);
    }
  }

  @FunctionalInterface
  interface BiFunction<T, U, R> {

    static <T, U, R> java.util.function.BiFunction<T, U, R> sneaky(final BiFunction<T, U, R> biFunction) {
      return (t, u) -> MoreUtils.applyThrowing(biFunction, t, u);
    }

    R apply(T t, U u) throws Exception;

    default java.util.function.BiFunction<T, U, R> sneaky() {
      return sneaky(this);
    }
  }

  @FunctionalInterface
  interface Consumer<T> {

    static <T> java.util.function.Consumer<T> sneaky(final Consumer<T> consumer) {
      return t -> MoreUtils.acceptThrowing(consumer, t);
    }

    void accept(T t) throws Exception;

    default java.util.function.Consumer<T> sneaky() {
      return sneaky(this);
    }
  }

  @FunctionalInterface
  interface Runnable {

    static java.lang.Runnable sneaky(final Runnable runnable) {
      return () -> MoreUtils.runThrowing(runnable);
    }

    void run() throws Exception;

    default java.lang.Runnable sneaky() {
      return sneaky(this);
    }
  }

  @FunctionalInterface
  interface Supplier<R> {

    static <R> java.util.function.Supplier<R> sneaky(final Supplier<R> supplier) {
      return () -> MoreUtils.supplyThrowing(supplier);
    }

    R get() throws Exception;

    default java.util.function.Supplier<R> sneaky() {
      return sneaky(this);
    }
  }

  @FunctionalInterface
  interface TriConsumer<T, U, V> {

    static <T, U, V> io.github.emilyydev.emmylib.common.util.function.TriConsumer<T, U, V> sneaky(final TriConsumer<T, U, V> triConsumer) {
      return (t, u, v) -> MoreUtils.acceptThrowing(triConsumer, t, u, v);
    }

    void accept(T t, U u, V v) throws Exception;

    default io.github.emilyydev.emmylib.common.util.function.TriConsumer<T, U, V> sneaky() {
      return sneaky(this);
    }
  }

  @FunctionalInterface
  interface TriFunction<T, U, V, R> {

    static <T, U, V, R> io.github.emilyydev.emmylib.common.util.function.TriFunction<T, U, V, R> sneaky(final TriFunction<T, U, V, R> triFunction) {
      return (t, u, v) -> MoreUtils.applyThrowing(triFunction, t, u, v);
    }

    R apply(T t, U u, V v) throws Exception;

    default io.github.emilyydev.emmylib.common.util.function.TriFunction<T, U, V, R> sneaky() {
      return sneaky(this);
    }
  }
}
