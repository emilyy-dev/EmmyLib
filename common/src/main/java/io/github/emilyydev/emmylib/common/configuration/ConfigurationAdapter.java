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

package io.github.emilyydev.emmylib.common.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

public abstract class ConfigurationAdapter {

  protected static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationAdapter.class);

  protected final Path configFile;
  protected final Path configFolder;
  protected final Set<Setting<? extends Serializable>> settings;
  protected final String separator;
  protected final Map<String, Object> nonDeserialized = new LinkedHashMap<>();
  protected final Map<String, Object> deserialized = new LinkedHashMap<>();

  private boolean initialized = false;

  protected ConfigurationAdapter(final @NotNull Path configFolder, final @NotNull String configName,
                                 final @NotNull Collection<? extends @NotNull Setting<? extends Serializable>> settings) {
    this(configFolder, configName, settings, ".");
  }

  protected ConfigurationAdapter(final @NotNull Path configFolder, final @NotNull String configName,
                                 final @NotNull Collection<? extends @NotNull Setting<? extends Serializable>> settings,
                                 final @NotNull String separator) {
    this.configFolder = requireNonNull(configFolder, "configFolder");
    this.configFile = configFolder.resolve(requireNonNull(configName, "configName"));
    this.settings = Set.copyOf(requireNonNull(settings, "settings"));
    this.separator = requireNonNull(separator, "separator");
  }

  public void load() throws IOException {
    if (Files.notExists(this.configFolder)) {
      Files.createDirectories(this.configFolder);
    }

    if (Files.notExists(this.configFile)) {
      try (final InputStream stream = getClass().getResourceAsStream(this.configFile.getFileName().toString())) {
        Files.copy(requireNonNull(stream, "stream"), this.configFile);
      }
    }

    reload0();
    this.initialized = true;
    this.settings.forEach(setting -> this.deserialized.put(setting.key(), setting.get(this)));
  }

  public void reload() throws IOException {
    this.nonDeserialized.clear();
    reload0();
    this.settings.forEach(setting -> {
      if (setting.reloadable()) {
        this.deserialized.put(setting.key(), setting.get(this));
      }
    });
  }

  /**
   * Reloads the config file from storage into {@link #nonDeserialized}, without attempting to do
   * any kind of object deserialization other than {@code Map}s and {@code List}s.
   *
   * @throws IOException if an {@code IO} error occurs
   */
  protected abstract void reload0() throws IOException; // don't expose

  public @Nullable Boolean getBoolean(final @NotNull String key) {
    requireNonNull(key, "key");
    return validate(key, get(key), Boolean.class);
  }

  public @Nullable Integer getInt(final @NotNull String key) {
    requireNonNull(key, "key");
    return validate(key, get(key), Integer.class);
  }

  public @Nullable Double getDouble(final @NotNull String key) {
    requireNonNull(key, "key");
    return validate(key, get(key), Double.class);
  }

  public @Nullable String getString(final @NotNull String key) {
    requireNonNull(key, "key");
    return validate(key, get(key), String.class);
  }

  @SuppressWarnings("unchecked")
  public <T> @Nullable List<T> getList(final @NotNull String key) {
    requireNonNull(key, "key");
    return validate(key, get(key), List.class);
  }

  @SuppressWarnings("unchecked")
  public <T> @Nullable Map<String, T> getSection(final @NotNull String key) {
    requireNonNull(key, "key");
    return validate(key, get(key), Map.class);
  }

  /**
   * Gets the cached value of a setting. If it is not cached, this method will call
   * {@link Setting#get(ConfigurationAdapter)} to retrieve its value and cache it.
   * <p>
   * Users are encouraged to use this method instead of calling
   * {@link Setting#get(ConfigurationAdapter)} directly.
   *
   * @param setting
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T extends Serializable> @NotNull T get(final @NotNull Setting<T> setting) {
    checkState(this.initialized, "Cannot fetch settings at this stage; configuration not yet initialized");
    requireNonNull(setting, "setting");
    return (T) this.deserialized.computeIfAbsent(setting.key(), key -> setting.get(this));
  }

  private Object get(final String path) {
    return get(path, this.nonDeserialized);
  }

  private Object get(final String path, final Map<String, Object> map) {
    checkState(this.initialized, "Cannot fetch settings at this stage; configuration not yet initialized");

    final int firstSeparator = path.indexOf(this.separator);
    if (firstSeparator == -1) {
      return map.get(path);
    }

    final String first = path.substring(0, firstSeparator);
    final String rest = path.substring(firstSeparator + 1);
    // blindly expect the "nested" value to be of type Map
    return get(rest, validate(first, map.get(first), Map.class));
  }

  private <T> T validate(final String key, final Object value, final Class<T> type) {
    if (value == null) {
      warn("No value for config key \"{}\" (expected to be of type {})", key, type.getSimpleName());
      return null;
    }

    if (!type.isInstance(value)) {
      warn("Config key \"{}\" expected to be of type {} but got {} instead", key, type, value.getClass());
      return null;
    }

    return type.cast(value);
  }

  private void warn(final String format, final Object... args) {
    LOGGER.warn(format, args);
  }
}
