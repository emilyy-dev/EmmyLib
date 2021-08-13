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

package io.github.emilyydev.emmylib.common.util;

import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import io.github.emilyydev.emmylib.common.util.function.Throwing;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

interface MoreBodyHandlerImpl {

  final class GsonBodyHandlerImpl<T> implements MoreBodyHandlers.GsonBodyHandler<T> {

    private final Gson gson;
    private final Type type;

    GsonBodyHandlerImpl(final Gson gson, final Type type) {
      this.gson = requireNonNull(gson);
      this.type = requireNonNull(type);
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(final HttpResponse.ResponseInfo responseInfo) {
      return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                                                  s -> this.gson.fromJson(s, this.type));
    }
  }

  final class MoshiBodyHandlerImpl<T> implements MoreBodyHandlers.MoshiBodyHandler<T> {

    private final JsonAdapter<T> adapter;

    MoshiBodyHandlerImpl(final JsonAdapter<T> adapter) {
      this.adapter = requireNonNull(adapter);
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(final HttpResponse.ResponseInfo responseInfo) {
      return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                                                  Throwing.Function.sneaky(this.adapter::fromJson));
    }
  }
}
