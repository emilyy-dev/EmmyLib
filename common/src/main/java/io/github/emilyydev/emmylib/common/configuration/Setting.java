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

package io.github.emilyydev.emmylib.common.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public abstract class Setting<T> {

  protected static final String KEY_FORMAT = "[a-z0-9-]+";
  private static final Pattern KEY_PATTERN = Pattern.compile(KEY_FORMAT);

  private final String key;
  private final T fallback;
  private final boolean reloadable;
  private transient final int hashCode;

  protected Setting(final @NotNull @org.intellij.lang.annotations.Pattern(KEY_FORMAT) String key,
                    final @NotNull T fallback, final boolean reloadable) {
    checkArgument(KEY_PATTERN.matcher(requireNonNull(key, "key")).matches(), "Setting key does not match required pattern: %s", KEY_FORMAT);

    this.key = key;
    this.fallback = requireNonNull(fallback, "fallback");
    this.reloadable = reloadable;
    this.hashCode = key.hashCode();
  }

  public abstract @NotNull T get(@NotNull ConfigurationAdapter adapter);

  public @NotNull String key() {
    return this.key;
  }

  public @NotNull T fallback() {
    return this.fallback;
  }

  public boolean reloadable() {
    return this.reloadable;
  }

  public boolean notReloadable() {
    return !reloadable();
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "["
           + "key='" + this.key + '\''
           + ", fallback=" + this.fallback
           + ", reloadable=" + this.reloadable
           + ']';
  }

  @Override
  public int hashCode() {
    return this.hashCode;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) { return true; }
    if (other == null || this.getClass() != other.getClass()) { return false; }
    final Setting<?> that = (Setting<?>) other;
    return this.key.equals(that.key);
  }
}
