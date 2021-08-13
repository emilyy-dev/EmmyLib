package io.github.emilyydev.emmylib.common.util;

import com.mojang.authlib.GameProfile;

import static java.util.Objects.requireNonNull;

public class NoPropertyFoundException extends Exception {

  private final GameProfile profile;
  private final String property;

  public NoPropertyFoundException(final String property, final GameProfile profile) {
    super();
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public NoPropertyFoundException(final String property, final GameProfile profile,
                                  final String message) {
    super(message);
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public NoPropertyFoundException(final String property, final GameProfile profile,
                                  final Throwable cause) {
    super(cause);
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public NoPropertyFoundException(final String property, final GameProfile profile,
                                  final String message, final Throwable cause) {
    super(message, cause);
    this.property = requireNonNull(property, "property");
    this.profile = requireNonNull(profile, "profile");
  }

  public String property() {
    return this.property;
  }

  public GameProfile profile() {
    return this.profile;
  }
}
