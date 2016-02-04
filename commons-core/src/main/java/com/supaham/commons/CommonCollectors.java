package com.supaham.commons;

import com.google.common.collect.Lists;

import com.supaham.commons.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class CommonCollectors {

  /**
   * Creates a {@link Collector} that represents its elements in reverse order, using an
   * {@link ArrayList}.
   * <p/>
   * Thanks to <a href="http://stackoverflow.com/questions/24010109/java-8-stream-reverse-order">
   * http://stackoverflow.com/questions/24010109/java-8-stream-reverse-order</a>
   *
   * @param <T> type of list to return
   *
   * @return a list collector in reversed order
   */
  public static <T> Collector<T, List<T>, List<T>> inReverse() {
    return Collector.of(ArrayList::new, List::add, (l, r) -> {
      l.addAll(r);
      return l;
    }, Lists::<T>reverse);
  }

  /**
   * Creates a {@link Collector} random element from a {@link Stream}. This method collects the stream and calls
   * {@link CollectionUtils#getRandomElement(Collection)} which supplies the final result.
   *
   * @param <T> type of object to return
   *
   * @return random element
   */
  public static <T> Collector<T, List<T>, T> singleRandom() {
    // TODO make this more efficient by not having to create a list and add.
    return Collector.of(ArrayList::new, List::add, (l, r) -> {
      l.addAll(r);
      return l;
    }, CollectionUtils::getRandomElement);
  }

  private CommonCollectors() {
    throw new AssertionError("nop");
  }
}
