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

import com.mojang.authlib.GameProfile;

import static java.util.Objects.requireNonNull;

public class NoPropertyFoundException extends Exception {

  private final GameProfile profile;
  private final String property;

  public NoPropertyFoundException(final String property, final GameProfile profile) {
    super();
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public NoPropertyFoundException(final String property, final GameProfile profile,
                                  final String message) {
    super(message);
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public NoPropertyFoundException(final String property, final GameProfile profile,
                                  final Throwable cause) {
    super(cause);
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public NoPropertyFoundException(final String property, final GameProfile profile,
                                  final String message, final Throwable cause) {
    super(message, cause);
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public String property() {
    return this.property;
  }

  public GameProfile profile() {
    return this.profile;
  }
}
