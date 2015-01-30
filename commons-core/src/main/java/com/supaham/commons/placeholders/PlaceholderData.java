package com.supaham.commons.placeholders;

import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Represents a data class used for {@link Placeholder}'s {@link Placeholder#apply(PlaceholderData)}.
 * This class provides the ability to add local objects using a Map<Object, Object>. This is useful
 * for adding any extra objects that you'd like {@link Placeholder} to have access to.
 */
public class PlaceholderData {

  private final String original;
  private String string = "";
  private String placeholder;
  private final Map<Object, Object> locals = new HashMap<>();

  public PlaceholderData(@Nonnull String string) {
    checkNotNullOrEmpty(string);
    this.original = string;
  }

  /**
   * Used for testing.
   */
  protected static PlaceholderData create(@Nonnull String string) {
    PlaceholderData data = new PlaceholderData(string);
    data.setPlaceholder(string);
    return data;
  }

  /**
   * Gets the original version of {@link #getString()}.
   *
   * @return full original string
   */
  public String getOriginal() {
    return original;
  }

  /**
   * Gets the full {@link String} being replaced.
   *
   * @return full string
   */
  public String getString() {
    return string;
  }

  protected final void setString(@Nonnull String string) {
    checkNotNullOrEmpty(string);
    this.string = string;
  }

  protected final void append(@Nonnull String string) {
    checkNotNullOrEmpty(string);
    this.string += string;
  }

  /**
   * Gets the current placeholder {@link String}.
   *
   * @return placeholder string
   */
  public String getPlaceholder() {
    return placeholder;
  }

  protected final void setPlaceholder(@Nonnull String placeholder) {
    checkNotNullOrEmpty(placeholder, "placeholder");
    this.placeholder = placeholder;
  }

  /**
   * @see Map#containsKey(Object)
   */
  public boolean containsKey(Object key) {
    return locals.containsKey(key);
  }

  /**
   * @see Map#containsValue(Object)
   */
  public boolean containsValue(Object value) {
    return locals.containsValue(value);
  }

  /**
   * @see Map#get(Object)
   */
  public Object get(Object key) {
    return locals.get(key);
  }

  /**
   * This method simply casts the {@link #get(Object)} return to the class generic type passed in
   * this method.
   *
   * @param key class with generic type to cast the get() to
   *
   * @see #get(Object)
   */
  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> key) {
    return (T) locals.get(key);
  }

  /**
   * @see Map#put(Object, Object) 
   */
  public Object put(Object key, Object value) {
    return this.locals.put(key, value);
  }
}
