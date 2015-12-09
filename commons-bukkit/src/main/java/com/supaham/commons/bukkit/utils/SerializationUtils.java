package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.SerializableConfig;
import pluginbase.config.datasource.yaml.YamlDataSource;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;
import pluginbase.config.serializers.Serializers;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.PluginBaseException;

/**
 * Utility methods for working with PluginBase serialization. This class contains methods such as
 * {@link #loadOrCreateProperties(PluginLogger, File, Object, String)}, and more.
 *
 * @since 0.3.2
 */
public final class SerializationUtils {

  public static final SerializerSet SERIALIZER_SET;

  static {
    SERIALIZER_SET = SerializerSet.defaultSet();
  }

  /**
   * Loads or creates a default properties class built on PluginBase. If the {@code file} does not
   * exist, it is created. Then, when loading, comments option is set to true, causing comment
   * output in the {@code file}. Then, the file is searched the given {@code defaults}, if it
   * does not exist, it is written to file. Finally, the yaml writes the result, whether it be the
   * defaults or the newly loaded class, to the config and writes to the {@code file}.
   *
   * @param logger logger to debug to
   * @param file file to load or create
   * @param defaults defaults to use
   * @param <T> type of properties
   *
   * @return object of type {@link T}
   */
  @Nullable
  public static <T> T loadOrCreateProperties(@Nonnull PluginLogger logger, @Nonnull File file,
                                             @Nonnull T defaults) {
    return loadOrCreateProperties(logger, file, defaults, null);
  }

  /**
   * Loads or creates a default properties class built on PluginBase. If the {@code file} does not
   * exist, it is created. Then, when loading, comments option is set to true, causing comment
   * output in the {@code file}. Then, the file is searched for {@code root} yaml object, if it
   * does not exist, it is written to using the {@code defaults}. Finally, the yaml writes the
   * result, whether it be the defaults or the newly loaded class, to the config and writes to the
   * {@code file}.
   *
   * @param logger logger to debug to
   * @param file file to load or create
   * @param defaults defaults to use
   * @param root root yaml object in the file, not null
   * @param <T> type of properties
   *
   * @return object of type {@link T}
   */
  @Nullable
  public static <T> T loadOrCreateProperties(@Nonnull PluginLogger logger, @Nonnull File file,
                                             @Nonnull T defaults, @Nullable String root) {
    Preconditions.checkNotNull(logger, "logger cannot be null.");
    Preconditions.checkNotNull(file, "file cannot be null.");
    Preconditions.checkNotNull(defaults, "defaults cannot be null.");
    T result = defaults;

    try {
      YamlDataSource yaml = YamlDataSource.builder().setFile(file).doComments(true).build();
      if (root == null) {
        result = yaml.loadToObject(defaults);
      } else {
        final Object loadedObject = yaml.load();
        if (loadedObject != null) {
          result = loadToObject(loadedObject, defaults, SERIALIZER_SET);
        }
      }
      if (result == null) {
        result = defaults;
      }
      yaml.save(result);
      logger.fine("Successfully loaded " + file.getName() + ".");
    } catch (PluginBaseException e) {
      logger.severe("Error occurred while loading " + file.getName() + "!");
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      logger.severe("Error occurred while saving " + file.getName() + "!");
      e.printStackTrace();
      return null;
    }
    return result;
  }

  public static <T> Serializer<T> getSerializer(Class<? extends Serializer<T>> serializerClass) {
    return Serializers.getSerializerInstance(serializerClass);
  }

  public static <T> Object serialize(T object) {
    return SerializableConfig.serialize(object);
  }

  public static <T> Object serialize(T object,
                                     @Nonnull Class<? extends Serializer<T>> serializerClass) {
    return serialize(object, serializerClass, SERIALIZER_SET);
  }

  public static <T> Object serialize(T object,
                                     @Nonnull Class<? extends Serializer<T>> serializerClass,
                                     @Nonnull SerializerSet serializerSet) {
    return getSerializer(serializerClass).serialize(object, serializerSet);
  }

  public static <T> T deserializeWith(Object serialized,
                                      @Nonnull Class<? extends Serializer<T>> serializerClass) {
    return (T) deserializeWith(serialized, serializerClass, SERIALIZER_SET);
  }

  public static <T> Object deserializeWith(Object serialized,
                                           @Nonnull Class<? extends Serializer<T>> serializerClass,
                                           @Nonnull SerializerSet serializerSet) {
    if (serialized == null) {
      return null;
    }
    Class<?> typeClass = null;
    if (serialized instanceof Map) {
      Map map = (Map) serialized;
      typeClass = SerializableConfig.getClassFromSerializedData(map);
    }
    if (typeClass == null) {
      typeClass = serialized.getClass();
    }
    return getSerializer(serializerClass).deserialize(serialized, typeClass, serializerSet);
  }

  public static <T> T loadToObject(Object value, T destination, SerializerSet serializerSet)
      throws IOException {
    if (value == null) {
      return null;
    }
    Preconditions.checkState(value instanceof Map, "value is not map, cannot deserialize.");

    // This is where the magic of class (destination) config templates is loaded 
    T source = SerializableConfig
        .deserializeAs(value, (Class<T>) destination.getClass(), serializerSet);

    if (destination.equals(source)) {
      return destination;
    }

    if (source != null) {
      destination = FieldMapper.mapFields(source, destination);
      return destination;
    } else {
      return null;
    }
  }

  private SerializationUtils() {
    throw new AssertionError("Try Weetabix instead...");
  }
}
