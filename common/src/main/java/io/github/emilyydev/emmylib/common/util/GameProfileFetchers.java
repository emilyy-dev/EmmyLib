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

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import io.github.emilyydev.emmylib.common.util.function.Throwing;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface GameProfileFetchers {

  static CompletableFuture<JsonObject> fetchProfileJson(final UUID uuid) {
    return CompletableFuture.supplyAsync(Throwing.Supplier.sneaky(() -> {
      final var httpRequest = HttpRequest.newBuilder(profileUrl(uuid).toURI())
                                         .header("User-Agent", GameProfileFetchers.class.getCanonicalName())
                                         .GET().build();
      final var gsonBodyHandler = MoreBodyHandlers.gson(GsonProvider.simple(), JsonObject.class);
      return HttpClient.newHttpClient().send(httpRequest, gsonBodyHandler).body();
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
