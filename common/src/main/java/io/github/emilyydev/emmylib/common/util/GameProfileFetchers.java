package io.github.emilyydev.emmylib.common.util;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import io.github.emilyydev.emmylib.common.util.function.Throwing;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface GameProfileFetchers {

  static CompletableFuture<JsonObject> fetchProfileJson(final UUID uuid) {
    return CompletableFuture.supplyAsync(Throwing.Supplier.sneaky(() -> {
      try (final var inputStream = profileUrl(uuid).openStream();
           final var reader = new InputStreamReader(inputStream)) {
        return GsonProvider.simple().fromJson(reader, JsonObject.class);
      }
    }));
  }

  static CompletableFuture<GameProfile> fetchGameProfile(final UUID uuid) {
    return fetchProfileJson(uuid).thenApplyAsync(profile -> {
      final var gameProfile = new GameProfile(uuid, profile.getAsJsonPrimitive("name").getAsString());
      final var serializer = new PropertyMap.Serializer();
      gameProfile.getProperties().putAll(serializer.deserialize(profile.getAsJsonArray("properties"), PropertyMap.class, null));
      return gameProfile;
    });
  }

  static CompletableFuture<Property> fetchSkin(final UUID uuid) {
    return fetchGameProfile(uuid).thenApplyAsync(Throwing.Function.sneaky(profile -> {
      final var it = profile.getProperties().get("textures").iterator();
      if (it.hasNext()) {
        return it.next();
      } else {
        throw new NoPropertyFoundException("textures", profile);
      }
    }));
  }

  private static URL profileUrl(final UUID uuid) throws MalformedURLException {
    return new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid)));
  }
}
