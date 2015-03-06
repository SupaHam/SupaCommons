package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.serializers.ListSerializer;

import org.bukkit.Location;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import pluginbase.config.serializers.Serializer;

/**
 * Contains Bukkit {@link Serializer} classes such as {@link ListColorStringSerializer}, {@link
 * ListLocationSerializer}, and more.
 * 
 * @since 0.1
 */
public class CBSerializers {

  public static class ListColorStringSerializer extends ListSerializer<String> {

    @Override
    public Class<ColorStringSerializer> getSerializerClass() {
      return ColorStringSerializer.class;
    }
  }
  
  public static class ListLocationSerializer extends ListSerializer<Location> {

    @Override
    public Class<LocationSerializer> getSerializerClass() {
      return LocationSerializer.class;
    }
  }

  public static class ListMaterialDataSerializer extends ListSerializer<MaterialData> {

    @Override
    public Class<MaterialDataSerializer> getSerializerClass() {
      return MaterialDataSerializer.class;
    }
  }

  public static class ListVectorSerializer extends ListSerializer<Vector> {

    @Override
    public Class<VectorSerializer> getSerializerClass() {
      return VectorSerializer.class;
    }
  }

}
