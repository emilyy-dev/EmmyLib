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

package io.github.emilyydev.emmylib.common.locale;

import com.google.gson.JsonElement;
import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class ComponentMessage implements ComponentLike, Message {

  private static final ComponentMessage EMPTY = new ComponentMessage(Component.empty());
  private static final ComponentMessage NEWLINE = new ComponentMessage(Component.newline());
  private static final ComponentMessage SPACE = new ComponentMessage(Component.space());

  public static ComponentMessage empty() {
    return EMPTY;
  }

  public static ComponentMessage newline() {
    return NEWLINE;
  }

  public static ComponentMessage space() {
    return SPACE;
  }

  public static ComponentMessage of(final @NotNull ComponentLike like) {
    final Component component = requireNonNull(like, "component").asComponent();

    if (component == Component.empty()) {
      return EMPTY;
    } else if (component == Component.newline()) {
      return NEWLINE;
    } else if (component == Component.space()) {
      return SPACE;
    }

    return new ComponentMessage(component);
  }

  private final Component component;
  private final String plain;
  private final JsonElement asJson;

  private ComponentMessage(final Component component) {
    this.component = component;
    this.plain = PlainComponentSerializer.plain().serialize(this.component);
    this.asJson = GsonComponentSerializer.gson().serializeToTree(this.component);
  }

  @Override
  public @NotNull Component asComponent() {
    return this.component;
  }

  @Override
  public String getString() {
    return this.plain;
  }

  public @NotNull JsonElement asJson() {
    return this.asJson;
  }
}
