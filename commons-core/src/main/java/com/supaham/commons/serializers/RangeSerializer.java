package com.supaham.commons.serializers;

import com.google.common.collect.Range;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * A {@link Range} {@link Serializer} that serializes as "x" or "x y", where x is the lowerEndPoint
 * and y is the upperEndPoint. This serializer also forces whole number doubles to be converted to
 * integers for convenience.
 *
 * @since 0.1
 */
public class RangeSerializer implements Serializer<Range> {

  @Override
  @Nullable
  public Object serialize(@Nullable final Range range, @Nonnull SerializerSet serializerSet) {
    if (range == null) {
      return null;
    }
    return getString(range.lowerEndpoint()) +
           (!range.upperEndpoint().equals(range.lowerEndpoint()) ?
            " " + getString(range.upperEndpoint()) : "");
  }

  @Override
  @Nullable
  public Range deserialize(@Nullable Object serialized, @NotNull Class wantedType, 
                           @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    String[] split = serialized.toString().split(" ");
    Range<Comparable> closed = Range.closed(getType(split[0]),
                                            getType(split[split.length > 1 ? 1 : 0]));
    return closed;
  }


  // getType(String) and getString(Comparable) are methods used to turn Integers into Double values.
  // This might cause issues to some, but it is pretty irritating when your Range expects an 
  // Integer but instead gets a whole value as a double.
  private Comparable getType(String input) {
    try {
      if (!input.contains(".")) {
        return Integer.parseInt(input);
      }
      Double v = Double.parseDouble(input);
      return v % 1 == 0 ? v.intValue() : v;
    } catch (NumberFormatException e) {
      return input;
    }
  }

  private String getString(Comparable comparable) {
    if (comparable instanceof Double) {
      if (((Double) comparable) % 1 == 0) {
        return Math.round(((Double) comparable)) + "";
      }
    } else if (comparable instanceof Float) {
      if (((Float) comparable) % 1 == 0) {
        return Math.round(((Float) comparable)) + "";
      }
    }
    return comparable.toString();
  }
}
