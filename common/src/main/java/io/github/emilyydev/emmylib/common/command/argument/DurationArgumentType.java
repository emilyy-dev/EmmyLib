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

package io.github.emilyydev.emmylib.common.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.emilyydev.emmylib.common.message.ComponentMessage;
import io.github.emilyydev.emmylib.common.util.Translations;
import io.github.emilyydev.emmylib.common.util.MoreUtils;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class DurationArgumentType implements ArgumentType<Duration> {

  private static final Map<String, TemporalUnit> SCALES;
  private static final List<String> SCALES_SUGGESTIONS;
  private static final Dynamic2CommandExceptionType DURATION_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> {
    return ComponentMessage.of(translatable(Translations.translatableKeyDurationTooSmall(),
                                            text(String.valueOf(min)), text(String.valueOf(found))));
  });
  private static final Dynamic2CommandExceptionType DURATION_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> {
    return ComponentMessage.of(translatable(Translations.translatableKeyDurationTooBig(),
                                            text(String.valueOf(max)), text(String.valueOf(found))));
  });
  private static final Collection<String> EXAMPLES = List.of("12d", "25mins", "8.5ys", "3mo 5ws 2days4.045secs");
  private static final Pattern DURATION_PATTERN =
      Pattern.compile("^" +
                      "(?:(\\d+(?:\\.\\d+)?)(y)(?:ear)?s?)?" +
                      "(?:(\\d+(?:\\.\\d+)?)(mo)(?:nth)?s?)?" +
                      "(?:(\\d+(?:\\.\\d+)?)(w)(?:eek)?s?)?" +
                      "(?:(\\d+(?:\\.\\d+)?)(d)(?:ay)?s?)?" +
                      "(?:(\\d+(?:\\.\\d+)?)(h)(?:r|our)?s?)?" +
                      "(?:(\\d+(?:\\.\\d+)?)(m)(?:in|inute)?s?)?" +
                      "(?:(\\d+(?:\\.\\d+)?)(?:(s)(?:ec|econd)?s?)?)?" +
                      "$");

  static {
    // Use a LinkedHashMap so they retain the order they were put into
    // That same order will be the one in which suggestions will appear
    // See #listSuggestions comments for a brief example
    final var builder = new LinkedHashMap<String, TemporalUnit>(7);
    builder.put("y", ChronoUnit.YEARS);
    builder.put("mo", ChronoUnit.MONTHS);
    builder.put("w", ChronoUnit.WEEKS);
    builder.put("d", ChronoUnit.DAYS);
    builder.put("h", ChronoUnit.HOURS);
    builder.put("m", ChronoUnit.MINUTES);
    builder.put("s", ChronoUnit.SECONDS);

    SCALES = Map.copyOf(builder);
    // This is where and why the order has to be retained
    SCALES_SUGGESTIONS = List.copyOf(builder.keySet());
  }

  public static Duration getDuration(final CommandContext<?> context, final String name) {
    return context.getArgument(name, Duration.class);
  }

  public static DurationArgumentType duration() {
    return new DurationArgumentType(null, null);
  }

  public static DurationArgumentType duration(final @Nullable Duration minimumInclusive) {
    return new DurationArgumentType(minimumInclusive, null);
  }

  public static DurationArgumentType duration(final @Nullable Duration minimumInclusive,
                                              final @Nullable Duration maximumExclusive) {
    return new DurationArgumentType(minimumInclusive, maximumExclusive);
  }

  private final Duration minimum;
  private final Duration maximum;

  private DurationArgumentType(final Duration minimum, final Duration maximum) {
    this.minimum = minimum;
    this.maximum = maximum;
  }

  public Optional<Duration> minimum() {
    return Optional.ofNullable(this.minimum);
  }

  public Optional<Duration> maximum() {
    return Optional.ofNullable(this.maximum);
  }

  @Override
  public Duration parse(final StringReader reader) throws CommandSyntaxException {
    final String input;
    if (StringReader.isQuotedStringStart(reader.peek())) {
      input = reader.readQuotedString().toLowerCase(Locale.ROOT).replaceAll("\\s", "");
    } else {
      input = reader.readUnquotedString().toLowerCase(Locale.ROOT);
    }

    if (input.isEmpty()) {
      throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
    }

    final Matcher matcher = DURATION_PATTERN.matcher(input);
    Duration duration = Duration.ZERO;

    if (matcher.find() && !matcher.group().isEmpty()) {
      for (int i = 1; i <= SCALES.size(); ++i) {
        final String current = matcher.group(i * 2 - 1);
        if (current == null) {
          continue;
        }

        // Defaults to seconds if no scale is provided (ergo: group is null)
        final TemporalUnit unit = SCALES.getOrDefault(matcher.group(i * 2), ChronoUnit.SECONDS);
        final double parsedCurrent = Double.parseDouble(current) * unit.getDuration().getSeconds();
        // Whacky workaround because you can't use ChronoUnits
        // larger than DAYS in Duration#plus(long, TemporalUnit)
        duration = duration.plus(Math.round(parsedCurrent), ChronoUnit.SECONDS);
      }
    } else {
      throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
    }

    if (this.minimum != null && duration.compareTo(this.minimum) < 0) {
      throw DURATION_TOO_SMALL.createWithContext(reader, MoreUtils.shortDuration(duration),
                                                 MoreUtils.shortDuration(this.minimum));
    }

    if (this.maximum != null && duration.compareTo(this.maximum) >= 0) {
      throw DURATION_TOO_BIG.createWithContext(reader, MoreUtils.shortDuration(duration),
                                               MoreUtils.shortDuration(this.maximum));
    }

    return duration;
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
    final String current = builder.getRemainingLowerCase();
    if (StringReader.isQuotedStringStart(current.charAt(0))) {
      for (int i = 1; i < current.toCharArray().length; ++i) {
        if (StringReader.isQuotedStringStart(current.charAt(i))) {
          return Suggestions.empty();
        }
      }
    }
    final String sanitized = current.replaceAll("\\s", "");
    final Matcher matcher = DURATION_PATTERN.matcher(sanitized);

    if (matcher.find()) {
      int nullGroups = 0;
      String lastScale = null;
      // Reverse for loop because we'll take the last non-digit non-null group as base index
      // for taking the suggestions
      for (int i = matcher.groupCount(); i > 0; --i) {
        final String currentGroup = matcher.group(i);
        if (currentGroup == null) {
          ++nullGroups;
        } else if (SCALES.containsKey(currentGroup) && lastScale == null) {
          lastScale = currentGroup;
        }
      }

      // 123 -> [y, mo, w, d, h, m, s]
      // 123ws4 -> [d, h, m, s]
      // 123ws4h56 -> [m, s]
      // 123mo -> [] (can't suggest random numbers, only suggest time scales)
      if (nullGroups % 2 == 1) {
        final int index = SCALES_SUGGESTIONS.indexOf(lastScale) + 1;
        for (int i = index; i < SCALES_SUGGESTIONS.size(); ++i) {
          builder.suggest(current + SCALES_SUGGESTIONS.get(i));
        }
        return builder.buildFuture();
      }
    }

    return Suggestions.empty();
  }

  @Override
  public Collection<String> getExamples() {
    return EXAMPLES;
  }
}
