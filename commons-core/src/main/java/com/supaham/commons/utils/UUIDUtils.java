package com.supaham.commons.utils;

import com.google.common.base.Preconditions;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link UUID} instances. This class contains methods such as
 * {@link #stripHyphens(String)}, {@link #constructUUID(String)}, and more.
 *
 * @since 0.3.3
 */
public class UUIDUtils {

  /**
   * Returns a hyphen-stripped {@link String} from a {@link UUID}. E.g. a UUID of
   * {@code "5828669c-1b13-4d0e-b9d3-d1c97c832f49"} transforming into
   * {@code "5828669c1b134d0eb9d3d1c97c832f49"}.This is equivalent to calling
   * {@link #stripHyphens(String)} with the string as {@link UUID#toString()}.
   *
   * @param uuid uuid object to strip
   *
   * @return hyphen-stripped {@code uuid}
   *
   * @see #stripHyphens(String)
   */
  public static String stripHyphens(@Nullable UUID uuid) {
    if (uuid == null) {
      return null;
    }
    return stripHyphens(uuid.toString());
  }

  /**
   * Returns a hyphen-stripped {@link String} from a {@link UUID} represented as a {@link String}.
   * E.g. a UUID of {@code "5828669c-1b13-4d0e-b9d3-d1c97c832f49"} transforming into
   * {@code "5828669c1b134d0eb9d3d1c97c832f49"}.
   *
   * @param uuid uuid object to strip
   *
   * @return hyphen-stripped {@code uuid}
   */
  public static String stripHyphens(@Nullable String uuid) {
    if (StringUtils.trimToNull(uuid) == null) {
      return null;
    }
    return uuid.replaceAll("-", "");
  }

  /**
   * Constructs a {@link UUID} instance based on a uuid represented as a String. This supports
   * hyphen-stripped uuids such as {@code "5828669c1b134d0eb9d3d1c97c832f49"}.
   *
   * @param uuid uuid string to transform
   *
   * @return UUID instance based on the transformed {@code string}
   *
   * @throws IllegalArgumentException when {@code uuid} is not of length 32 or 36
   */
  public static UUID constructUUID(@Nonnull String uuid) {
    Preconditions.checkNotNull(uuid, "uuid cannot be null.");
    Preconditions.checkArgument(uuid.length() == 32 || uuid.length() == 36,
                                "uuid has an unexpected length of " + uuid.length());
    if (uuid.length() == 32) {
      uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }
    return UUID.fromString(uuid);
  }

  private UUIDUtils() {
    throw new AssertionError("");
  }
}
