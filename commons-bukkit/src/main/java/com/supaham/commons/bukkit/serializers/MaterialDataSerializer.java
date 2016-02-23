package com.supaham.commons.bukkit.serializers;

import com.google.common.base.Preconditions;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * A {@link MaterialData} serializer that serializes as "x:y" where x is {@link
 * MaterialData#getItemType()} and y is {@link MaterialData#getData()}.
 *
 * @since 0.1
 */
public class MaterialDataSerializer implements Serializer<MaterialData> {

  @Nullable
  @Override
  public Object serialize(@Nullable MaterialData object, @Nonnull SerializerSet serializerSet) {
    return object == null ? null : object.getItemType() + ":" + object.getData();
  }

  @Nullable
  @Override
  public MaterialData deserialize(@Nullable Object serialized, @NotNull Class wantedType,
                                  @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }
    String[] split = serialized.toString().split(":");
    Material material = Material.matchMaterial(split[0]);
    Preconditions.checkArgument(material != null, split[0] + " is not a valid material.");
    return new MaterialData(material,
                            split.length >= 2 && !split[1].isEmpty() ? Byte.valueOf(split[1]) : 0);
  }
}
