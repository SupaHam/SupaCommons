package com.supaham.commons;

import com.google.common.collect.Lists;

import com.supaham.commons.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
  public static <T> Collector<T, List<T>, Optional<T>> singleRandom() {
    // TODO make this more efficient by not having to create a list and add.
    return Collector.of(ArrayList::new, List::add, (l, r) -> {
      l.addAll(r);
      return l;
    }, l -> l.isEmpty() ? Optional.empty() : Optional.ofNullable(CollectionUtils.getRandomElement(l)));
  }

  // http://stackoverflow.com/a/29339106/2355760

  /**
   * Returns a collector for a {@link List} collection of the minimum results with the given comparator.
   * <p />
   * If a list of integers such as [1, 3, 2] were given, the number 1 would be returned in a List. However, if there
   * were two ones, such as [1, 3, 2, 1], then the number 1 would be returned twice in a List.
   *
   * @param comp comparator to test for minimum result
   * @param <T> type of object to compare
   *
   * @return list collector
   */
  public static <T> Collector<T, ?, List<T>> minList(Comparator<? super T> comp) {
    return Collector.of(
        ArrayList::new,
        (list, t) -> {
          int c;
          if (list.isEmpty() || (c = comp.compare(t, list.get(0))) == 0) {
            list.add(t);
          } else if (c < 0) {
            list.clear();
            list.add(t);
          }
        },
        (list1, list2) -> {
          if (list1.isEmpty()) {
            return list2;
          }
          if (list2.isEmpty()) {
            return list1;
          }
          int r = comp.compare(list1.get(0), list2.get(0));
          if (r > 0) {
            return list2;
          } else if (r < 0) {
            return list1;
          } else {
            list1.addAll(list2);
            return list1;
          }
        });
  }

  /**
   * Returns a collector for a {@link List} collection of the maximum results with the given comparator.
   * <p />
   * If a list of integers such as [1, 3, 2] were given, the number 3 would be returned in a List. However, if there
   * were two threes, such as [3, 1, 3, 2], then the number 3 would be returned twice in a List.
   *
   * @param comp comparator to test for maximum result
   * @param <T> type of object to compare
   *
   * @return list collector
   */
  public static <T> Collector<T, ?, List<T>> maxList(Comparator<? super T> comp) {
    return Collector.of(
        ArrayList::new,
        (list, t) -> {
          int c;
          if (list.isEmpty() || (c = comp.compare(t, list.get(0))) == 0) {
            list.add(t);
          } else if (c > 0) {
            list.clear();
            list.add(t);
          }
        },
        (list1, list2) -> {
          if (list1.isEmpty()) {
            return list2;
          }
          if (list2.isEmpty()) {
            return list1;
          }
          int r = comp.compare(list1.get(0), list2.get(0));
          if (r < 0) {
            return list2;
          } else if (r > 0) {
            return list1;
          } else {
            list1.addAll(list2);
            return list1;
          }
        });
  }

  private CommonCollectors() {
    throw new AssertionError("nop");
  }
}
