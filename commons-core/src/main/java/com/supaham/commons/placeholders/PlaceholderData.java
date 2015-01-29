package com.supaham.commons.placeholders;

import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import javax.annotation.Nonnull;

/**
 * Represents a data class used for {@link Placeholder}'s {@link Placeholder#apply(PlaceholderData)}.
 */
public class PlaceholderData {

  private final String original;
  private String string = "";
  private String placeholder;

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
}
