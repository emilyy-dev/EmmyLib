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

import io.github.emilyydev.emmylib.common.util.function.Throwing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

public interface MoreUtils {

  static @NotNull String sanitize(final @Nullable String input) {
    if (input == null) {
      return "null";
    }

    return input.trim().toLowerCase(Locale.ROOT);
  }

  static @NotNull String shortDuration(final @NotNull Duration duration) {
    Objects.requireNonNull(duration, "duration");

    final StringJoiner joiner = new StringJoiner("");

    long years = duration.getSeconds();
    long months = years % ChronoUnit.YEARS.getDuration().getSeconds();
    long weeks = months % ChronoUnit.MONTHS.getDuration().getSeconds();
    long days = weeks % ChronoUnit.WEEKS.getDuration().getSeconds();
    long hours = days % ChronoUnit.DAYS.getDuration().getSeconds();
    long minutes = hours % ChronoUnit.HOURS.getDuration().getSeconds();
    final long seconds = minutes % ChronoUnit.MINUTES.getDuration().getSeconds();

    years /= ChronoUnit.YEARS.getDuration().getSeconds();
    months /= ChronoUnit.MONTHS.getDuration().getSeconds();
    weeks /= ChronoUnit.WEEKS.getDuration().getSeconds();
    days /= ChronoUnit.DAYS.getDuration().getSeconds();
    hours /= ChronoUnit.HOURS.getDuration().getSeconds();
    minutes /= ChronoUnit.MINUTES.getDuration().getSeconds();

    if (years != 0) {
      joiner.add(years + "y");
    }
    if (months != 0) {
      joiner.add(months + "mo");
    }
    if (weeks != 0) {
      joiner.add(weeks + "w");
    }
    if (days != 0) {
      joiner.add(days + "d");
    }
    if (hours != 0) {
      joiner.add(hours + "h");
    }
    if (minutes != 0) {
      joiner.add(minutes + "m");
    }
    if (seconds != 0) {
      joiner.add(seconds + "s");
    }

    return joiner.toString();
  }

  static @NotNull String longDuration(final @NotNull Duration duration) {
    Objects.requireNonNull(duration, "duration");

    final StringJoiner joiner = new StringJoiner(", ");

    long years = duration.getSeconds();
    long months = years % ChronoUnit.YEARS.getDuration().getSeconds();
    long weeks = months % ChronoUnit.MONTHS.getDuration().getSeconds();
    long days = weeks % ChronoUnit.WEEKS.getDuration().getSeconds();
    long hours = days % ChronoUnit.DAYS.getDuration().getSeconds();
    long minutes = hours % ChronoUnit.HOURS.getDuration().getSeconds();
    final long seconds = minutes % ChronoUnit.MINUTES.getDuration().getSeconds();

    years /= ChronoUnit.YEARS.getDuration().getSeconds();
    months /= ChronoUnit.MONTHS.getDuration().getSeconds();
    weeks /= ChronoUnit.WEEKS.getDuration().getSeconds();
    days /= ChronoUnit.DAYS.getDuration().getSeconds();
    hours /= ChronoUnit.HOURS.getDuration().getSeconds();
    minutes /= ChronoUnit.MINUTES.getDuration().getSeconds();

    if (years != 0) {
      joiner.add(years + " year" + (years > 1 ? "s" : ""));
    }
    if (months != 0) {
      joiner.add(months + " month" + (months > 1 ? "s" : ""));
    }
    if (weeks != 0) {
      joiner.add(weeks + " week" + (weeks > 1 ? "s" : ""));
    }
    if (days != 0) {
      joiner.add(days + " day" + (days > 1 ? "s" : ""));
    }
    if (hours != 0) {
      joiner.add(hours + " hour" + (hours > 1 ? "s" : ""));
    }
    if (minutes != 0) {
      joiner.add(minutes + " minute" + (minutes > 1 ? "s" : ""));
    }
    if (seconds != 0) {
      joiner.add(seconds + " second" + (seconds > 1 ? "s" : ""));
    }

    return joiner.toString();
  }

  @SuppressWarnings("unchecked")
  static <T extends Throwable> void sneakyThrow(final Throwable throwable) throws T {
    throw (T) throwable;
  }

  static void runThrowing(final Throwing.Runnable runnable) {
    try {
      runnable.run();
    } catch (final Exception exception) {
      sneakyThrow(exception);
    }
  }

  static <R> R supplyThrowing(final Throwing.Supplier<R> supplier) {
    try {
      return supplier.get();
    } catch (final Exception exception) {
      sneakyThrow(exception);
      return null;  // won't be reached
    }
  }

  static <T> void acceptThrowing(final Throwing.Consumer<T> consumer, final T t) {
    try {
      consumer.accept(t);
    } catch (final Exception exception) {
      sneakyThrow(exception);
    }
  }

  static <T, U> void acceptThrowing(final Throwing.BiConsumer<T, U> consumer, final T t, final U u) {
    try {
      consumer.accept(t, u);
    } catch (final Exception exception) {
      sneakyThrow(exception);
    }
  }

  static <T, U, V> void acceptThrowing(final Throwing.TriConsumer<T, U, V> consumer, final T t, final U u, final V v) {
    try {
      consumer.accept(t, u, v);
    } catch (final Exception exception) {
      sneakyThrow(exception);
    }
  }

  static <T, R> R applyThrowing(final Throwing.Function<T, R> function, final T t) {
    try {
      return function.apply(t);
    } catch (final Exception exception) {
      sneakyThrow(exception);
      return null;  // won't be reached
    }
  }

  static <T, U, R> R applyThrowing(final Throwing.BiFunction<T, U, R> function, final T t, final U u) {
    try {
      return function.apply(t, u);
    } catch (final Exception exception) {
      sneakyThrow(exception);
      return null;  // won't be reached
    }
  }

  static <T, U, V, R> R applyThrowing(final Throwing.TriFunction<T, U, V, R> function, final T t, final U u, final V v) {
    try {
      return function.apply(t, u, v);
    } catch (final Exception exception) {
      sneakyThrow(exception);
      return null;  // won't be reached
    }
  }
}
