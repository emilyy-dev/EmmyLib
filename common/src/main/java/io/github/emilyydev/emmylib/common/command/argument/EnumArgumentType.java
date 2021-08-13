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

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class EnumArgumentType<E extends Enum<E>> implements ArgumentType<E> {

  private static final Dynamic2CommandExceptionType UNKNOWN_ENUM_CONSTANT = new Dynamic2CommandExceptionType((value, clazz) -> {
    return ComponentMessage.of(translatable(Translations.translatableKeyUnknownEnumConstant(),
                                            text(String.valueOf(value)), text(String.valueOf(clazz))));
  });

  public static <E extends Enum<E>> E value(final CommandContext<?> context, final String name, final Class<E> clazz) {
    return context.getArgument(name, clazz);
  }

  public static <E extends Enum<E>> EnumArgumentType<E> of(final Class<E> clazz) {
    return new EnumArgumentType<>(clazz);
  }

  private final Class<E> clazz;
  private final Map<String, E> nameValueMap;

  private EnumArgumentType(final Class<E> clazz) {
    this.clazz = clazz;

    final E[] values = clazz.getEnumConstants();
    this.nameValueMap = stream(values).collect(toUnmodifiableMap(e -> e.name().toLowerCase(), e -> e));
  }

  @Override
  public E parse(final StringReader reader) throws CommandSyntaxException {
    final String name = reader.readUnquotedString().toLowerCase(Locale.ROOT);
    final E value = this.nameValueMap.get(name);
    if (value != null) {
      return value;
    }

    throw UNKNOWN_ENUM_CONSTANT.createWithContext(reader, name, this.clazz.getSimpleName());
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
    final String current = builder.getRemainingLowerCase();
    for (final String name : this.nameValueMap.keySet()) {
      if (name.startsWith(current)) {
        builder.suggest(name);
      }
    }

    return builder.buildFuture();
  }

  @Override
  public Collection<String> getExamples() {
    return this.nameValueMap.keySet().stream().collect(Collectors.toUnmodifiableList());
  }
}
