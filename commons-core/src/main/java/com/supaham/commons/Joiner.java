package com.supaham.commons;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

public class Joiner {

  private com.google.common.base.Joiner joiner;

  /**
   * Returns a joiner which automatically places {@code separator} between consecutive elements.
   */
  public static Joiner on(String separator) {
    return new Joiner(separator);
  }

  /**
   * Returns a joiner which automatically places {@code separator} between consecutive elements.
   */
  public static Joiner on(char separator) {
    return new Joiner(String.valueOf(separator));
  }

  private Joiner(String separator) {
    this.joiner = com.google.common.base.Joiner.on(separator);
  }

  private Joiner(Joiner prototype) {
    this.joiner = prototype.joiner;
  }

  private Joiner(com.google.common.base.Joiner prototype) {
    this.joiner = prototype;
  }

  /**
   * Returns a {@link FunctionJoiner} with the same behaviour as this Joiner, and the given
   * function. The function is used to transform input when calling {@code join} methods.
   *
   * @param function function
   * @param <T> type of input to transform
   *
   * @return new function joiner
   */
  public <T> FunctionJoiner<T> function(Function<T, String> function) {
    return new FunctionJoiner<>(this, function);
  }
  
  // TODO add functionality for limiting join output.

  /* ================================
   * >> DELEGATE METHODS
   * ================================ */

  /**
   * Appends the string representation of each of {@code parts}, using the previously configured
   * separator between each, to {@code appendable}.
   */
  public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
    return joiner.appendTo(appendable, parts);
  }

  /**
   * Appends the string representation of each of {@code parts}, using the previously configured
   * separator between each, to {@code appendable}.
   */
  public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
    return joiner.appendTo(appendable, parts);
  }

  /**
   * Appends the string representation of each of {@code parts}, using the previously configured
   * separator between each, to {@code appendable}.
   */
  public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
    return joiner.appendTo(appendable, Arrays.asList(parts));
  }

  /**
   * Appends to {@code appendable} the string representation of each of the remaining arguments.
   */
  public final <A extends Appendable> A appendTo(A appendable,
                                                 @Nullable Object first,
                                                 @Nullable Object second,
                                                 Object... rest) throws IOException {
    return joiner.appendTo(appendable, first, second, rest);
  }

  /**
   * Appends the string representation of each of {@code parts}, using the previously configured
   * separator between each, to {@code builder}. Identical to {@link #appendTo(Appendable,
   * Iterable)}, except that it does not throw {@link IOException}.
   */
  public StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
    return joiner.appendTo(builder, parts);
  }

  /**
   * Appends the string representation of each of {@code parts}, using the previously configured
   * separator between each, to {@code builder}. Identical to {@link #appendTo(Appendable,
   * Iterable)}, except that it does not throw {@link IOException}.
   */
  public StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
    return joiner.appendTo(builder, parts);
  }

  /**
   * Appends the string representation of each of {@code parts}, using the previously configured
   * separator between each, to {@code builder}. Identical to {@link #appendTo(Appendable,
   * Iterable)}, except that it does not throw {@link IOException}.
   */
  public StringBuilder appendTo(StringBuilder builder, Object[] parts) {
    return joiner.appendTo(builder, parts);
  }

  /**
   * Appends to {@code builder} the string representation of each of the remaining arguments.
   * Identical to {@link #appendTo(Appendable, Object, Object, Object...)}, except that it does not
   * throw {@link IOException}.
   */
  public StringBuilder appendTo(StringBuilder builder, @Nullable Object first,
                                @Nullable Object second, Object... rest) {
    return joiner.appendTo(builder, first, second, rest);
  }

  /**
   * Returns a string containing the string representation of each of {@code parts}, using the
   * previously configured separator between each.
   */
  public String join(Iterable<?> parts) {
    return joiner.join(parts);
  }

  /**
   * Returns a string containing the string representation of each of {@code parts}, using the
   * previously configured separator between each.
   */
  public String join(Iterator<?> parts) {
    return joiner.join(parts);
  }

  /**
   * Returns a string containing the string representation of each of {@code parts}, using the
   * previously configured separator between each.
   */
  public String join(Object[] parts) {
    return joiner.join(parts);
  }

  /**
   * Returns a string containing the string representation of each argument, using the previously
   * configured separator between each.
   */
  public String join(@Nullable Object first, @Nullable Object second, Object... rest) {
    return joiner.join(first, second, rest);
  }

  /**
   * Returns a joiner with the same behavior as this one, except automatically substituting {@code
   * nullText} for any provided null elements.
   */
  @CheckReturnValue public Joiner useForNull(String nullText) {
    return new Joiner(joiner.useForNull(nullText));
  }

  /**
   * Returns a joiner with the same behavior as this joiner, except automatically skipping over any
   * provided null elements.
   */
  @CheckReturnValue public Joiner skipNulls() {
    return new Joiner(joiner.skipNulls());
  }
  
  /* ================================
   * >> /DELEGATE METHODS
   * ================================ */

  public final class FunctionJoiner<T> extends Joiner {

    private final Function<T, String> function;

    private FunctionJoiner(Joiner joiner, Function<T, String> function) {
      super(joiner);
      this.function = function;
    }

    @Override public String join(Iterable<?> parts) {
      return joiner.join(Iterables.transform((Iterable<T>) parts, this.function));
    }

    @Override public String join(Iterator<?> parts) {
      return joiner.join(Iterators.transform((Iterator<T>) parts, this.function));
    }

    @Override public String join(Object[] parts) {
      return join(Arrays.asList(parts));
    }

    @Override public String join(@Nullable Object first, @Nullable Object second, Object... rest) {
      return join(Arrays.asList(first, second, rest));
    }

    @Override public FunctionJoiner<T> useForNull(String nullText) {
      return new FunctionJoiner<>(super.useForNull(nullText), this.function);
    }

    @Override public FunctionJoiner<T> skipNulls() {
      return new FunctionJoiner<>(super.skipNulls(), this.function);
    }
  }
}
