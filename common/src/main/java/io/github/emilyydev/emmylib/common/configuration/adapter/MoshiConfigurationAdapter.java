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

package io.github.emilyydev.emmylib.common.configuration.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import io.github.emilyydev.emmylib.common.configuration.ConfigurationAdapter;
import io.github.emilyydev.emmylib.common.configuration.Setting;
import io.github.emilyydev.emmylib.common.util.MoshiProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public class MoshiConfigurationAdapter extends ConfigurationAdapter {

  private static final Type MAP_TYPE = Types.newParameterizedType(Map.class, String.class, Object.class);
  private static final JsonAdapter<Map<String, Object>> MAP_JSON_ADAPTER = MoshiProvider.get().adapter(MAP_TYPE);

  public MoshiConfigurationAdapter(final @NotNull Path configFolder, final @NotNull String configName,
                                   final @NotNull Collection<? extends @NotNull Setting<? extends Serializable>> settings) {
    super(configFolder, configName, settings);
  }

  public MoshiConfigurationAdapter(final @NotNull Path configFolder, final @NotNull String configName,
                                   final @NotNull Collection<? extends @NotNull Setting<? extends Serializable>> settings,
                                   final @NotNull String separator) {
    super(configFolder, configName, settings, separator);
  }

  @Override
  protected void reload0() throws IOException {
    final Map<String, Object> map = MAP_JSON_ADAPTER.fromJson(Files.readString(this.configFile));
    super.nonDeserialized.putAll(map != null ? map : Map.of());
  }
}
