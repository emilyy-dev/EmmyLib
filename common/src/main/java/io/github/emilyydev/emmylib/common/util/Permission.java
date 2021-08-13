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

import io.github.emilyydev.emmylib.common.command.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class Permission implements Predicate<Permissible> {

  public static Permission has(final @NotNull String permission) {
    requireNonNull(permission, "permission");
    return new Permission(permissible -> permissible.hasPermission(permission));
  }

  public static Permission lacks(final @NotNull String permission) {
    return new Permission(permissible -> !permissible.hasPermission(permission));
  }

  private final Predicate<Permissible> delegate;

  private Permission(Predicate<Permissible> delegate) {
    // we only need the root delegate predicate
    while (delegate instanceof Permission) {
      delegate = ((Permission) delegate).delegate;
    }

    this.delegate = delegate;
  }

  @Override
  public boolean test(final Permissible permissible) {
    return this.delegate.test(permissible);
  }

  @Override
  public @NotNull Permission and(final @NotNull Predicate<? super Permissible> other) {
    requireNonNull(other, "other");
    return new Permission(this.delegate.and(other));
  }

  public @NotNull Permission and(final @NotNull String permission) {
    requireNonNull(permission, "permission");
    return and(permissible -> permissible.hasPermission(permission));
  }

  @Override
  public @NotNull Permission or(final @NotNull Predicate<? super Permissible> other) {
    requireNonNull(other, "other");
    return new Permission(this.delegate.or(other));
  }

  public @NotNull Permission or(final @NotNull String permission) {
    requireNonNull(permission, "permission");
    return or(permissible -> permissible.hasPermission(permission));
  }

  @Override
  public @NotNull Permission negate() {
    return new Permission(this.delegate.negate());
  }
}
