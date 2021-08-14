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

import com.google.common.base.Joiner;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;

import static java.util.Objects.requireNonNull;

public final class Translations {

  private static final Joiner SEPARATOR = Joiner.on('.');

  // io.github.emilyydev.emmylib
  private static final String LIBRARY_PREFIX_NON_RELOCATED = SEPARATOR.join("io", "github", "emilyydev", "emmylib");
  // ???????????????????????????
  private static final String LIBRARY_PREFIX_RELOCATED = "io.github.emilyydev.emmylib";

  // io.github.emilyydev.emmylib.option
  private static final String OPTION_PREFIX = SEPARATOR.join(LIBRARY_PREFIX_NON_RELOCATED, "option");

  // io.github.emilyydev.emmylib.option.UseUniversalTranslations
  private static final boolean USE_UNIVERSAL_TRANSLATIONS = Boolean.getBoolean(SEPARATOR.join(OPTION_PREFIX, "UseUniversalTranslations"));
  private static final String LIBRARY_PREFIX = USE_UNIVERSAL_TRANSLATIONS ? LIBRARY_PREFIX_NON_RELOCATED : LIBRARY_PREFIX_RELOCATED;

  // io.github.emilyydev.emmylib.translation
  private static final String TRANSLATION_PREFIX = SEPARATOR.join(LIBRARY_PREFIX, "translation");

  // Intentionally submit key value to relocation
  private static final Key EMMYLIB_TRANSLATIONS_KEY = Key.key("emmylib", "io.github.emilyydev.emmylib.translations");
  private static final TranslationRegistry TRANSLATION_REGISTRY = TranslationRegistry.create(EMMYLIB_TRANSLATIONS_KEY);

  /* ************************************** */
  /* ********** Translation keys ********** */
  /* ************************************** */

  // io.github.emilyydev.emmylib.translation.command.argument.exception.DurationTooSmall
  private static final String TRANSLATABLE_KEY_DURATION_TOO_SMALL =
      register(SEPARATOR.join(TRANSLATION_PREFIX, "command", "argument", "exception", "DurationTooSmall"),
               pattern("Duration must not be less than {0}, found {1}"));

  // io.github.emilyydev.emmylib.translation.command.argument.exception.DurationTooBig
  private static final String TRANSLATABLE_KEY_DURATION_TOO_BIG =
      register(SEPARATOR.join(TRANSLATION_PREFIX, "command", "argument", "exception", "DurationTooBig"),
               pattern("Duration must not be more than or equal to {0}, found {1}"));

  // io.github.emilyydev.emmylib.translation.command.argument.exception.UnknownEnumConstant
  private static final String TRANSLATABLE_KEY_UNKNOWN_ENUM_CONSTANT =
      register(SEPARATOR.join(TRANSLATION_PREFIX, "command", "argument", "exception", "UnknownEnumConstant"),
               pattern("Unknown value name {0} for enum type {1}"));

  // io.github.emilyydev.emmylib.translation.command.argument.exception.InvalidVector2
  private static final String TRANSLATABLE_KEY_INVALID_VECTOR_2_FORMAT =
      register(SEPARATOR.join(TRANSLATION_PREFIX, "command", "argument", "exception", "InvalidVector2"),
               pattern("Invalid input format for 2-axis vector: must be ''x y'', got ''{0}''"));

  // io.github.emilyydev.emmylib.translation.command.argument.exception.InvalidVector3
  private static final String TRANSLATABLE_KEY_INVALID_VECTOR_3_FORMAT =
      register(SEPARATOR.join(TRANSLATION_PREFIX, "command", "argument", "exception", "InvalidVector3"),
               pattern("Invalid input format for 3-axis vector: must be ''x y z'', got ''{0}''"));

  /* ************************************** */
  /* ************************************** */
  /* ************************************** */

  static {
    TRANSLATION_REGISTRY.defaultLocale(Locale.ENGLISH);
    GlobalTranslator.get().addSource(TRANSLATION_REGISTRY);
  }

  public static TranslationRegistry translationRegistry() {
    return TRANSLATION_REGISTRY;
  }

  public static boolean useUniversalTranslations() {
    return USE_UNIVERSAL_TRANSLATIONS;
  }

  public static String register(final @NotNull String key, final @NotNull MessageFormat format) {
    TRANSLATION_REGISTRY.register(requireNonNull(key, "key"), Locale.ENGLISH, requireNonNull(format, "format"));
    return key;
  }

  public static MessageFormat pattern(final @NotNull String pattern) {
    return new MessageFormat(requireNonNull(pattern, "pattern"), Locale.ENGLISH);
  }

  public static String translatableKeyDurationTooSmall() {
    return TRANSLATABLE_KEY_DURATION_TOO_SMALL;
  }

  public static String translatableKeyDurationTooBig() {
    return TRANSLATABLE_KEY_DURATION_TOO_BIG;
  }

  public static String translatableKeyUnknownEnumConstant() {
    return TRANSLATABLE_KEY_UNKNOWN_ENUM_CONSTANT;
  }

  public static String translatableKeyInvalidVector2Format() {
    return TRANSLATABLE_KEY_INVALID_VECTOR_2_FORMAT;
  }

  public static String translatableKeyInvalidVector3Format() {
    return TRANSLATABLE_KEY_INVALID_VECTOR_3_FORMAT;
  }

  private Translations() {
    throw new UnsupportedOperationException("Cannot instantiate utility class");
  }
}
