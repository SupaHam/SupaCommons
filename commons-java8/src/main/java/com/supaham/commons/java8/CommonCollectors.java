package com.supaham.commons.java8;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public class CommonCollectors {

  /**
   * Creates a {@link Collector} that represents its elements in reverse order, using an
   * {@link ArrayList}.
   * <p/>
   * Thanks to <a href="http://stackoverflow.com/questions/24010109/java-8-stream-reverse-order">
   *   http://stackoverflow.com/questions/24010109/java-8-stream-reverse-order</a>
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

  private CommonCollectors() {
    throw new AssertionError("nop");
  }
}
