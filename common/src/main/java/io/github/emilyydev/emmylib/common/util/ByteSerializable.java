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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

public interface ByteSerializable extends Serializable {

  byte[] NIL = { };

  @Contract("_, _, !null -> !null")
  static <T extends ByteSerializable> @Nullable T deserialize(final byte @NotNull [] data,
                                                              final @NotNull Class<T> clazz,
                                                              final @Nullable T fallback) {
    Objects.requireNonNull(data, "data");
    Objects.requireNonNull(clazz, "clazz");
    try (final var inputStream = new ByteArrayInputStream(data);
         final var objectInputStream = new ObjectInputStream(inputStream)) {
      return clazz.cast(objectInputStream.readObject());
    } catch (final Exception exception) {
      if (exception instanceof ClassNotFoundException) {
        exception.printStackTrace();
      }
      return fallback;
    }
  }

  /**
   * Serializes this object to an array of bytes which can be later re-constructed using the
   * {@link #deserialize(byte[], Class, ByteSerializable)} method.
   *
   * @return the serialized object as a byte array.
   */
  default byte @NotNull [] serialize() {
    try (final var outputStream = new ByteArrayOutputStream();
         final var objectOutputStream = new ObjectOutputStream(outputStream)) {
      objectOutputStream.writeObject(this);
      return outputStream.toByteArray();
    } catch (final IOException exception) {
      // exception delegated from the stream passed to the ObjectOutputStream
      // ByteArrayOutputStream doesn't throw IOException so this body won't be reached
      return NIL;
    }
  }
}
