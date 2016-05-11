package com.supaham.commons.minecraft.world.space;

import static com.google.common.base.Preconditions.checkArgument;
import static com.supaham.commons.utils.NumberUtils.roundExact;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public final class PositionSerializer implements Serializer<Position> {

  private static final Pattern PATTERN = Pattern.compile("\\s*,\\s*");

  @Nullable @Override
  public Object serialize(@Nullable Position object, @Nonnull SerializerSet serializerSet) {
    if (object == null) {
      return null;
    }
    boolean pitch = object.getPitch() > 0;
    boolean yaw = pitch || object.getYaw() > 0;
    return roundExact(3, object.getX()) + ","
           + roundExact(3, object.getY()) + ","
           + roundExact(3, object.getZ())
           + (yaw ? "," + roundExact(3, object.getYaw()) : "")
           + (pitch ? "," + roundExact(3, object.getPitch()) : "");
  }

  @Nullable @Override
  public Position deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                              @Nonnull SerializerSet serializerSet) {
    if (serialized == null) {
      return null;
    }
    String string = serialized.toString();
    checkNotNullOrEmpty(string, "serialized string");
    String[] split = PATTERN.split(string, 6);
    checkArgument(split.length >= 3 && split.length < 6, "position is in an invalid format: %s", string);
    float yaw = split.length > 3 ? Float.parseFloat(split[3]) : 0;
    float pitch = split.length > 4 ? Float.parseFloat(split[4]) : 0;
    return new Position(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]), yaw, pitch);
  }
}
