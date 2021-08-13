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

public final class Vector3 implements Comparable<Vector3>, ByteSerializable {

  public static final Vector3 ZERO = new Vector3(0.0, 0.0, 0.0);
  public static final Vector3 UNIT_I = new Vector3(1.0, 0.0, 0.0);
  public static final Vector3 UNIT_J = new Vector3(0.0, 1.0, 0.0);
  public static final Vector3 UNIT_K = new Vector3(0.0, 0.0, 1.0);
  public static final Vector3 ONE = new Vector3(1.0, 1.0, 1.0);
  public static final Vector3 MINUS_ONE = new Vector3(-1.0, -1.0, -1.0);

  public static @NotNull Vector3 deserialize(final byte @NotNull [] data) {
    return ByteSerializable.deserialize(data, Vector3.class, ZERO);
  }

  public static Vector3 at(final int i, final int j, final int k) {
    if (i == 0 && j == 0 && k == 0) {
      return ZERO;
    }

    if (i == 1 && j == 1 && k == 1) {
      return ONE;
    }

    if (i == -1 && j == -1 && k == -1) {
      return MINUS_ONE;
    }

    return new Vector3(i, j, k);
  }

  public static Vector3 at(final double i, final double j, final double k) {
    if (i == 0.0 && j == 0.0 && k == 0.0) {
      return ZERO;
    }

    if (i == 1.0 && j == 1.0 && k == 1.0) {
      return ONE;
    }

    if (i == -1.0 && j == -1.0 && k == -1.0) {
      return MINUS_ONE;
    }

    return new Vector3(i, j, k);
  }

  private final double i, j, k;

  private Vector3(final double i, final double j, final double k) {
    this.i = i;
    this.j = j;
    this.k = k;
  }

  public Vector3 add(final double i, final double j, final double k) {
    return at(this.i + i, this.j + j, this.k + k);
  }

  public Vector3 add(final double n) {
    if (n == 0.0) {
      return this;
    } else {
      return add(n, n, n);
    }
  }

  public Vector3 add(final @NotNull Vector3 that) {
    if (that == ZERO) {
      return this;
    } else {
      return add(that.i, that.j, that.k);
    }
  }

  public Vector3 subtract(final double i, final double j, final double k) {
    return at(this.i - i, this.j - j, this.k - k);
  }

  public Vector3 subtract(final double n) {
    if (n == 0.0) {
      return this;
    } else {
      return subtract(n, n, n);
    }
  }

  public Vector3 subtract(final @NotNull Vector3 that) {
    if (that == ZERO) {
      return this;
    } else {
      return subtract(that.i, that.j, that.k);
    }
  }

  public Vector3 multiply(final double i, final double j, final double k) {
    return at(this.i * i, this.j * j, this.k * k);
  }

  public Vector3 multiply(final double n) {
    if (n == 1.0) {
      return this;
    } else if (n == 0.0) {
      return ZERO;
    } else {
      return multiply(n, n, n);
    }
  }

  public Vector3 multiply(final @NotNull Vector3 that) {
    if (that == ONE) {
      return this;
    } else if (that == ZERO) {
      return ZERO;
    } else {
      return multiply(that.i, that.j, that.k);
    }
  }

  public Vector3 divide(final double i, final double j, final double k) {
    return at(this.i / i, this.j / j, this.k / k);
  }

  public Vector3 divide(final double n) {
    if (n == 1.0) {
      return this;
    } else {
      return divide(n, n, n);
    }
  }

  public Vector3 divide(final @NotNull Vector3 that) {
    if (that == ONE) {
      return this;
    } else {
      return divide(that.i, that.j, that.k);
    }
  }

  public double modulusSquared() {
    return this.i * this.i + this.j * this.j + this.k * this.k;
  }

  public double modulus() {
    return Math.sqrt(modulusSquared());
  }

  public double distanceSquared(final Vector3 that) {
    final double di = that.i - this.i;
    final double dj = that.j - this.j;
    final double dk = that.k - this.k;
    return di * di + dj * dj + dk * dk;
  }

  public double distance(final Vector3 that) {
    return Math.sqrt(distanceSquared(that));
  }

  public Vector3 normalize() {
    return divide(modulus());
  }

  public double dot(final Vector3 that) {
    return this.i * that.i + this.j * that.j + this.k * that.k;
  }

  public Vector3 cross(final Vector3 that) {
    return at(this.j * that.k - this.k * that.j,
              this.k * that.i - this.i * that.k,
              this.i * that.j - this.j * that.i);
  }

  public Vector3 floor() {
    return at(Math.floor(this.i), Math.floor(this.j), Math.floor(this.k));
  }

  public Vector3 ceil() {
    return at(Math.ceil(this.i), Math.ceil(this.j), Math.ceil(this.k));
  }

  public Vector3 round() {
    return at(Math.floor(this.i + 0.5), Math.floor(this.j + 0.5), Math.floor(this.k + 0.5));
  }

  public Vector3 abs() {
    return at(Math.abs(this.i), Math.abs(this.j), Math.abs(this.k));
  }

  @Override
  public int compareTo(final @NotNull Vector3 that) {
    final int iCompare = Double.compare(this.i, that.i);
    if (iCompare != 0) { return iCompare; }
    final int jCompare = Double.compare(this.j, that.j);
    if (jCompare != 0) { return jCompare; }
    return Double.compare(this.k, that.k);
  }

  @Override
  public String toString() {
    return "Vector3["
           + "i=" + this.i
           + ", j=" + this.j
           + ", k=" + this.k
           + ']';
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) { return true; }
    if (other == null || this.getClass() != other.getClass()) { return false; }
    final Vector3 that = (Vector3) other;
    return compareTo(that) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.i, this.j, this.k);
  }
}
