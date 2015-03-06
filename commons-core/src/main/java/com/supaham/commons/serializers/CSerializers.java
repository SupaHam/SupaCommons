package com.supaham.commons.serializers;

import com.google.common.collect.Range;

import org.joda.time.Duration;

import pluginbase.config.serializers.Serializer;

/**
 * Contains {@link Serializer} classes such as {@link ListDurationSerializer}, {@link
 * ListRangeSerializer}, and more.
 *
 * @since 0.1
 */
public class CSerializers {

  /**
   * @since 0.1
   */
  public static class ListDurationSerializer extends ListSerializer<Duration> {

    @Override
    public Class<DurationSerializer> getSerializerClass() {
      return DurationSerializer.class;
    }
  }

  /**
   * @since 0.1
   */
  public static class ListRangeSerializer extends ListSerializer<Range> {

    @Override
    public Class<RangeSerializer> getSerializerClass() {
      return RangeSerializer.class;
    }
  }
}
