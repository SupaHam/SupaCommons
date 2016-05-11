package com.supaham.commons.minecraft.world.space;

import static com.google.common.base.Preconditions.checkArgument;
import static com.supaham.commons.utils.NumberUtils.roundExact;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public final class VectorSerializer implements Serializer<Vector> {

  private static final Pattern PATTERN = Pattern.compile("\\s*,\\s*");

  @Nullable @Override
  public Object serialize(@Nullable Vector object,
                          @Nonnull SerializerSet serializerSet) {
    if (object == null) {
      return null;
    }
    return roundExact(3, object.getX()) + ","
           + roundExact(3, object.getY()) + ","
           + roundExact(3, object.getZ());
  }

  @Nullable @Override
  public Vector deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                            @Nonnull SerializerSet serializerSet) {
    if (serialized == null) {
      return null;
    }
    checkNotNullOrEmpty(serialized.toString(), "serialized string");
    String[] split = PATTERN.split(serialized.toString(), 4);
    checkArgument(split.length == 3, "string is in an invalid format.");
    return new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                      Double.parseDouble(split[2]));
  }
}
