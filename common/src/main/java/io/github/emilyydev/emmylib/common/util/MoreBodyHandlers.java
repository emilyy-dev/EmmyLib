package io.github.emilyydev.emmylib.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.moshi.JsonAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;

public interface MoreBodyHandlers {

  static <T> MoshiBodyHandler<T> moshi(final @NotNull JsonAdapter<T> adapter) {
    return new MoreBodyHandlerImpl.MoshiBodyHandlerImpl<>(adapter);
  }

  static <T> GsonBodyHandler<T> gson(final @NotNull Gson gson, final @NotNull Class<T> clazz) {
    return new MoreBodyHandlerImpl.GsonBodyHandlerImpl<>(gson, clazz);
  }

  static <T> GsonBodyHandler<T> gson(final @NotNull Gson gson, final @NotNull TypeToken<T> typeToken) {
    return new MoreBodyHandlerImpl.GsonBodyHandlerImpl<>(gson, typeToken.getType());
  }

  static <T> GsonBodyHandler<T> gson(final @NotNull Gson gson, final @NotNull Type type) {
    return new MoreBodyHandlerImpl.GsonBodyHandlerImpl<>(gson, type);
  }

  interface GsonBodyHandler<T> extends HttpResponse.BodyHandler<T> { }

  interface MoshiBodyHandler<T> extends HttpResponse.BodyHandler<T> { }
}
