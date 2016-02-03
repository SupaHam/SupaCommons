package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.serializers.ColorSerializer;
import com.supaham.commons.serializers.DurationSerializer;
import com.supaham.commons.utils.ThrowableUtils;

import org.bukkit.Color;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.SerializableConfig;
import pluginbase.config.datasource.DataSource;
import pluginbase.config.datasource.yaml.YamlDataSource;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;
import pluginbase.config.serializers.SerializerSet.Builder;
import pluginbase.messages.PluginBaseException;

/**
 * Utility methods for working with PluginBase serialization. This class contains methods such as
 * {@link #loadOrCreateProperties(Logger, DataSource, Object, String)}, and more.
 *
 * @since 0.3.2
 */
public final class SerializationUtils {

  public static final SerializerSet SERIALIZER_SET;

  static {
    Builder builder = SerializerSet.builder(SerializerSet.defaultSet());
    _add(builder, Duration.class, new DurationSerializer());
    _add(builder, Color.class, new ColorSerializer());
    SERIALIZER_SET = builder.build();
  }

  private static final <T> Builder _add(Builder builder, Class<T> clazz, Serializer<T> serializer) {
    return builder.addSerializer(clazz, () -> serializer);
  }

  public static YamlDataSource.Builder yaml(File file) throws IOException {
    if (!file.exists()) {
      file.createNewFile();
    }
    return YamlDataSource.builder().setFile(file).setIndent(2);
  }

  /**
   * Loads or creates a default properties class built on PluginBase. If the {@code file} does not
   * exist, it is created. Then, when loading, comments option is set to true, causing comment
   * output in the {@code file}. Then, the file is searched the given {@code defaults}, if it
   * does not exist, it is written to file. Finally, the yaml writes the result, whether it be the
   * defaults or the newly loaded class, to the config and writes to the {@code file}.
   *
   * @param logger logger to debug to
   * @param dataSource data source to load from
   * @param defaults defaults to use
   * @param <T> type of properties
   *
   * @return object of type {@link T}
   */
  @Nullable
  public static <T> T loadOrCreateProperties(@Nonnull Logger logger,
                                             @Nonnull DataSource dataSource,
                                             @Nonnull T defaults) {
    return loadOrCreateProperties(logger, dataSource, defaults, null);
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
   * @param dataSource data source to load from
   * @param defaults defaults to use
   * @param root root yaml object in the file, not null
   * @param <T> type of properties
   *
   * @return object of type {@link T}
   */
  @Nullable
  public static <T> T loadOrCreateProperties(@Nonnull Logger logger,
                                             @Nonnull DataSource dataSource, @Nonnull T defaults,
                                             @Nullable String root) {
    return loadOrCreateProperties(logger, dataSource, defaults, root, SERIALIZER_SET);
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
   * @param dataSource data source to load from
   * @param defaults defaults to use
   * @param root root yaml object in the file, not null
   * @param <T> type of properties
   *
   * @return object of type {@link T}
   */
  @Nullable
  public static <T> T loadOrCreateProperties(@Nonnull Logger logger,
                                             @Nonnull DataSource dataSource, @Nonnull T defaults,
                                             @Nullable String root, SerializerSet serializerSet) {
    Preconditions.checkNotNull(logger, "logger cannot be null.");
    Preconditions.checkNotNull(dataSource, "dataSource cannot be null.");
    Preconditions.checkNotNull(defaults, "defaults cannot be null.");
    T result = defaults;

    try {
      final Object loadedObject = dataSource.load();
      if (loadedObject != null) {
        result = loadToObject(loadedObject, defaults, serializerSet);
      }
      if (result == null) {
        result = defaults;
      }
    } catch (PluginBaseException e) {
      e.printStackTrace();
      return null;
    }
    return result;
  }

  public static <T extends Serializer> T getSerializer(Class<T> serializerClass) {
    return SERIALIZER_SET.getSerializerInstance(serializerClass);
  }

  public static <T> Serializer<T> getClassSerializer(Class<T> serializerClass) {
    return (Serializer<T>) SERIALIZER_SET.getClassSerializer(serializerClass);
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
  
  // See https://github.com/dumptruckman/PluginBase/issues/24 for more information
  private static Method METHOD;
  private static Method METHOD2;

  static {
    try {
      METHOD = Class.forName("pluginbase.config.serializers.DefaultSerializer")
          .getDeclaredMethod("deserializeToObject", Map.class, Object.class, SerializerSet.class);
      METHOD.setAccessible(true);
      METHOD2 = SerializerSet.class.getDeclaredMethod("getFallbackSerializer");
      METHOD2.setAccessible(true);
    } catch (NoSuchMethodException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static <T> T loadToObject(Object value, T destination, SerializerSet serializerSet) {
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
      try {
        // WORKING CODE VIA REFLECTION
        return (T) METHOD.invoke(METHOD2.invoke(serializerSet), value, destination, serializerSet);
      } catch (IllegalAccessException | InvocationTargetException e) {
        ThrowableUtils.getCause(e).printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  private SerializationUtils() {
    throw new AssertionError("Try Weetabix instead...");
  }
}
