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
import com.google.gson.reflect.TypeToken;
import com.squareup.moshi.JsonAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;

public interface MoreBodyHandlers {

  static <T> MoshiBodyHandler<T> moshi(final @NotNull JsonAdapter<T> adapter) {
    return new MoreBodyHandlerImpl.MoshiBodyHandlerImpl<>(adapter);
  }

  static <T> GsonBodyHandler<T> gson(final @NotNull Gson gson, final @NotNull Class<T> clazz) {
    return new MoreBodyHandlerImpl.GsonBodyHandlerImpl<>(gson, clazz);
  }

  static <T> GsonBodyHandler<T> gson(final @NotNull Gson gson, final @NotNull TypeToken<T> typeToken) {
    return new MoreBodyHandlerImpl.GsonBodyHandlerImpl<>(gson, typeToken.getType());
  }

  static <T> GsonBodyHandler<T> gson(final @NotNull Gson gson, final @NotNull Type type) {
    return new MoreBodyHandlerImpl.GsonBodyHandlerImpl<>(gson, type);
  }

  interface GsonBodyHandler<T> extends HttpResponse.BodyHandler<T> { }

  interface MoshiBodyHandler<T> extends HttpResponse.BodyHandler<T> { }
}
