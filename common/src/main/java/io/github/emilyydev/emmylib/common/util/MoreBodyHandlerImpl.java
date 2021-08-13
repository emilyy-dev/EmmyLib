package io.github.emilyydev.emmylib.common.util;

import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import io.github.emilyydev.emmylib.common.util.function.Throwing;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

interface MoreBodyHandlerImpl {

  final class GsonBodyHandlerImpl<T> implements MoreBodyHandlers.GsonBodyHandler<T> {

    private final Gson gson;
    private final Type type;

    GsonBodyHandlerImpl(final Gson gson, final Type type) {
      this.gson = requireNonNull(gson);
      this.type = requireNonNull(type);
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(final HttpResponse.ResponseInfo responseInfo) {
      return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                                                  s -> this.gson.fromJson(s, this.type));
    }
  }

  final class MoshiBodyHandlerImpl<T> implements MoreBodyHandlers.MoshiBodyHandler<T> {

    private final JsonAdapter<T> adapter;

    MoshiBodyHandlerImpl(final JsonAdapter<T> adapter) {
      this.adapter = requireNonNull(adapter);
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(final HttpResponse.ResponseInfo responseInfo) {
      return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                                                  Throwing.Function.sneaky(this.adapter::fromJson));
    }
  }
}
