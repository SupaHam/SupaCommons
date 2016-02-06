package com.supaham.commons.utils;

import com.google.common.base.Preconditions;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * Represents a utility class for interacting with bean classes.
 *
 * @since 0.5.1
 */
public class BeanUtils {

  private static final Map<Class, PropertyCacheList> CACHE_MAP = new HashMap<>();

  /**
   * Returns a Map of field names and their serialized values for a given {@link Object}. This object is cached after
   * the first generation.
   *
   * @param object object to get properties from
   *
   * @return Map of string (field names) and objects (field values)
   *
   * @see #getPropertiesList(Object, SerializerSet)
   */
  public static Map<String, Object> getPropertiesList(@Nonnull Object object) {
    return getPropertiesList(object, null);
  }

  /**
   * Returns a Map of field names and their serialized values for a given {@link Object}. This object is cached after
   * the first generation. A {@link SerializerSet} is optional but adds more meaning to the output of the serialized
   * object if the SerializerSet supports the type to be serialized. If no SerializerSet is present, the field value
   * itself will be pass on into the returned Map.
   *
   * @param object object to get properties from
   * @param serializerSet serializer set to control serialization, nullable
   *
   * @return Map of string (field names) and objects (field values)
   */
  public static Map<String, Object> getPropertiesList(@Nonnull Object object, @Nullable SerializerSet serializerSet) {
    Preconditions.checkNotNull(object, "object cannot be null.");
    try {
      final Class<?> clazz = object.getClass();
      PropertyCacheList cache = CACHE_MAP.get(clazz);
      if (cache == null) {
        CACHE_MAP.put(clazz, cache = new PropertyCacheList(clazz));
      }

      Map<String, Object> result = new LinkedHashMap<>();
      for (PropertyCache propertyCache : cache) {
        result.put(propertyCache.descriptor.getName(), propertyCache.invoke(object, serializerSet));
      }
      return result;
    } catch (IntrospectionException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*
   * ArrayList collection of PropertyCache objects. Each class has one of these lists.
   */
  private static final class PropertyCacheList extends ArrayList<PropertyCache> {

    public PropertyCacheList(Class clazz) throws IntrospectionException {
      for (PropertyDescriptor descriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
        if (!descriptor.getName().equals("class") // class prints current class (useless to us).
            && descriptor.getReadMethod() != null) {
          Field declaredField;
          try {
            declaredField = getField(clazz, descriptor.getName());
            // Don't include transient fields
            if (Modifier.isTransient(declaredField.getModifiers())) {
              continue;
            }
          } catch (NoSuchFieldException ignored) {
            continue;
          }
          add(new PropertyCache(declaredField, descriptor));
        }
      }
    }

    private Field getField(Class clazz, String name) throws NoSuchFieldException {
      try {
        return clazz.getDeclaredField(name);
      } catch (NoSuchFieldException e) {
        if (!clazz.getSuperclass().equals(Object.class)) {
          return getField(clazz.getSuperclass(), name);
        }
        throw e;
      }
    }
  }

  /*
   * Serves as a cache for both Field and a descriptor. This class makes use of the optional SerializerSet allowing
   * for custom serialization.
   */
  private static final class PropertyCache {

    private Field field;
    private PropertyDescriptor descriptor;

    public PropertyCache(Field field, PropertyDescriptor descriptor) {
      this.field = field;
      this.descriptor = descriptor;
    }

    public Object invoke(Object object) {
      return invoke(object, null);
    }

    public Object invoke(Object object, SerializerSet serializerSet) {
      Class<?> returnType = descriptor.getReadMethod().getReturnType();
      Object invoke;
      try {
        invoke = descriptor.getReadMethod().invoke(object);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
        return null;
      }
      if (serializerSet == null) {
        return invoke;
      } else {
        Serializer serializer;
        SerializeWith annotation = field.getDeclaredAnnotation(SerializeWith.class);
        if (annotation != null) {
          serializer = serializerSet.getSerializerInstance(annotation.value());
        } else {
          serializer = serializerSet.getClassSerializer(returnType);
        }

        return serializer.serialize(invoke, serializerSet);
      }
    }
  }
}
