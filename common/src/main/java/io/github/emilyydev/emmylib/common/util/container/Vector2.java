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

import io.github.emilyydev.emmylib.common.util.ByteSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Vector2 implements Comparable<Vector2>, ByteSerializable {

  public static final Vector2 ZERO = new Vector2(0.0, 0.0);
  public static final Vector2 UNIT_I = new Vector2(1.0, 0.0);
  public static final Vector2 UNIT_J = new Vector2(0.0, 1.0);
  public static final Vector2 ONE = new Vector2(1.0, 1.0);
  public static final Vector2 MINUS_ONE = new Vector2(-1.0, -1.0);

  public static @NotNull Vector2 deserialize(final byte @NotNull [] data) {
    return ByteSerializable.deserialize(data, Vector2.class, ZERO);
  }

  public static Vector2 at(final double i, final double j) {
    if (i == 0.0 && j == 0.0) {
      return ZERO;
    }

    if (i == 1.0 && j == 1.0) {
      return ONE;
    }

    if (i == -1.0 && j == -1.0) {
      return MINUS_ONE;
    }

    return new Vector2(i, j);
  }

  public static Vector2 at(final int i, final int j) {
    if (i == 0 && j == 0) {
      return ZERO;
    }

    if (i == 1 && j == 1) {
      return ONE;
    }

    if (i == -1 && j == -1) {
      return MINUS_ONE;
    }

    return new Vector2(i, j);
  }

  private final double i, j;

  private Vector2(final double i, final double j) {
    this.i = i;
    this.j = j;
  }

  public Vector2 add(final double i, final double j) {
    return at(this.i + i, this.j + j);
  }

  public Vector2 add(final double n) {
    if (n == 0.0) {
      return this;
    } else {
      return add(n, n);
    }
  }

  public Vector2 add(final @NotNull Vector2 that) {
    if (that == ZERO) {
      return this;
    } else {
      return add(that.i, that.j);
    }
  }

  public Vector2 subtract(final double i, final double j) {
    return at(this.i - i, this.j - j);
  }

  public Vector2 subtract(final double n) {
    if (n == 0.0) {
      return this;
    } else {
      return subtract(n, n);
    }
  }

  public Vector2 subtract(final @NotNull Vector2 that) {
    if (that == ZERO) {
      return this;
    } else {
      return subtract(that.i, that.j);
    }
  }

  public Vector2 multiply(final double i, final double j) {
    return at(this.i * i, this.j * j);
  }

  public Vector2 multiply(final double n) {
    if (n == 1.0) {
      return this;
    } else if (n == 0.0) {
      return ZERO;
    } else {
      return multiply(n, n);
    }
  }

  public Vector2 multiply(final @NotNull Vector2 that) {
    if (that == ONE) {
      return this;
    } else if (that == ZERO) {
      return ZERO;
    } else {
      return multiply(that.i, that.j);
    }
  }

  public Vector2 divide(final double i, final double j) {
    return at(this.i / i, this.j / j);
  }

  public Vector2 divide(final double n) {
    if (n == 1.0) {
      return this;
    } else {
      return divide(n, n);
    }
  }

  public Vector2 divide(final @NotNull Vector2 that) {
    if (that == ONE) {
      return this;
    } else {
      return divide(that.i, that.j);
    }
  }

  public double modulusSquared() {
    return this.i * this.i + this.j * this.j;
  }

  public double modulus() {
    return Math.sqrt(modulusSquared());
  }

  public double distanceSquared(final Vector2 that) {
    final double di = that.i - this.i;
    final double dj = that.j - this.j;
    return di * di + dj * dj;
  }

  public double distance(final Vector2 that) {
    return Math.sqrt(distanceSquared(that));
  }

  public Vector2 normalize() {
    return divide(modulus());
  }

  public double dot(final Vector2 that) {
    return this.i * that.i + this.j * that.j;
  }

  public Vector2 floor() {
    return at(Math.floor(this.i), Math.floor(this.j));
  }

  public Vector2 ceil() {
    return at(Math.ceil(this.i), Math.ceil(this.j));
  }

  public Vector2 round() {
    return at(Math.floor(this.i + 0.5), Math.floor(this.j + 0.5));
  }

  public Vector2 abs() {
    return at(Math.abs(this.i), Math.abs(this.j));
  }

  @Override
  public int compareTo(final @NotNull Vector2 that) {
    final int iCompare = Double.compare(this.i, that.i);
    if (iCompare != 0) { return iCompare; }
    return Double.compare(this.j, that.j);
  }

  @Override
  public String toString() {
    return "Vector2["
           + "i=" + this.i
           + ", j=" + this.j
           + ']';
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) { return true; }
    if (other == null || this.getClass() != other.getClass()) { return false; }
    final Vector2 that = (Vector2) other;
    return compareTo(that) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.i, this.j);
  }
}
