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
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.emilyydev.emmylib.common.locale.ComponentMessage;
import io.github.emilyydev.emmylib.common.locale.Translations;
import io.github.emilyydev.emmylib.common.util.container.Vector3;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class Vector3ArgumentType implements ArgumentType<Vector3> {

  private static final DynamicCommandExceptionType INVALID_FORMAT_EXCEPTION = new DynamicCommandExceptionType(read -> {
    return ComponentMessage.of(translatable(Translations.translatableKeyInvalidVector3Format(), text(String.valueOf(read))));
  });

  private static final List<String> EXAMPLES = List.of("0.0 0.0 0.0", "1 2 3", "-1.65 1589 -9681.4");

  private static final Pattern UNIT_PATTERN = Pattern.compile("-?\\d+(?:\\.\\d+)?");
  private static final Pattern VEC3_PATTERN = Pattern.compile(UNIT_PATTERN.pattern() + ' '
                                                              + UNIT_PATTERN.pattern() + ' '
                                                              + UNIT_PATTERN.pattern());

  public static Vector3 getVector3(final CommandContext<?> context, final String name) {
    return context.getArgument(name, Vector3.class);
  }

  public static Vector3ArgumentType vec3Arg() {
    return new Vector3ArgumentType();
  }

  private Vector3ArgumentType() { }

  @Override
  public Vector3 parse(final StringReader reader) throws CommandSyntaxException {
    final StringBuilder argumentBuilder = new StringBuilder();

    argumentBuilder.append(reader.readUnquotedString());
    argumentBuilder.append(' ');
    reader.skipWhitespace();

    argumentBuilder.append(reader.readUnquotedString());
    argumentBuilder.append(' ');
    reader.skipWhitespace();

    argumentBuilder.append(reader.readUnquotedString());

    final String argument = argumentBuilder.toString();
    if (VEC3_PATTERN.matcher(argument).matches()) {
      final StringReader argumentReader = new StringReader(argument);

      final double x = argumentReader.readDouble();
      argumentReader.skipWhitespace();

      final double y = argumentReader.readDouble();
      argumentReader.skipWhitespace();

      final double z = argumentReader.readDouble();

      return Vector3.at(x, y, z);
    }

    throw INVALID_FORMAT_EXCEPTION.createWithContext(reader, argument);
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
    // TODO this, suggest a trailing whitespace until complete
    return Suggestions.empty();
  }

  @Override
  public Collection<String> getExamples() {
    return EXAMPLES;
  }
}
