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

package io.github.emilyydev.emmylib.common.message;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;

public interface Message {

  @FunctionalInterface
  interface Args0 {

    ComponentLike build();

    default void send(final Audience audience) {
      audience.sendMessage(build());
    }
  }

  @FunctionalInterface
  interface Args1<T> {

    ComponentLike build(T t);

    default void send(final Audience audience, final T t) {
      audience.sendMessage(build(t));
    }
  }

  @FunctionalInterface
  interface Args2<T, U> {

    ComponentLike build(T t, U u);

    default void send(final Audience audience, final T t, final U u) {
      audience.sendMessage(build(t, u));
    }
  }

  @FunctionalInterface
  interface Args3<T, U, V> {

    ComponentLike build(T t, U u, V v);

    default void send(final Audience audience, final T t, final U u, final V v) {
      audience.sendMessage(build(t, u, v));
    }
  }

  @FunctionalInterface
  interface Args4<T, U, V, W> {

    ComponentLike build(T t, U u, V v, W w);

    default void send(final Audience audience, final T t, final U u, final V v, final W w) {
      audience.sendMessage(build(t, u, v, w));
    }
  }

  @FunctionalInterface
  interface Args5<T, U, V, W, X> {

    ComponentLike build(T t, U u, V v, W w, X x);

    default void send(final Audience audience, final T t, final U u, final V v, final W w, final X x) {
      audience.sendMessage(build(t, u, v, w, x));
    }
  }
}
