package com.supaham.commons.placeholders;

import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Represents a data class used for {@link Placeholder}'s {@link Placeholder#apply(PlaceholderData)}.
 * <br /> This class provides a Builder class; accessible through {@link #builder()}. <br />
 * This class provides the ability to add local objects using a {@literal Map<Object, Object>}. This
 * is useful for adding any extra objects that you'd like {@link Placeholder} to have access to.
 */
public class PlaceholderData {

  private final String original;
  private String string = "";
  private String placeholder;
  private final Map<Object, Object> locals;

  /**
   * Creates a new {@link Builder} for constructing a {@link PlaceholderData} instance.
   * @return {@link Builder} instance
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private PlaceholderData(@Nonnull String string) {
    this(string, new HashMap<>());
  }
  
  private PlaceholderData(@Nonnull String string, @Nonnull Map<Object, Object> locals) {
    checkNotNullOrEmpty(string);
    this.original = string;
    this.locals = locals;
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
  
  public static final class Builder {
    private String input;
    private final Map<Object, Object> locals = new HashMap<>();
    
    public Builder input(String input) {
      checkNotNullOrEmpty(input);
      this.input = input;
      return this;
    }
    
    public Builder put(Object local) {
      put(local.getClass(), local);
      return this;
    }
    
    public Builder put(Object key, Object value) {
      this.locals.put(key, value);
      return this;
    }
    
    public PlaceholderData build() {
      return new PlaceholderData(this.input, this.locals);
    }
  }
}
