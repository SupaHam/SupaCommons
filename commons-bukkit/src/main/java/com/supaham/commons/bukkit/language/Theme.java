package com.supaham.commons.bukkit.language;

/**
 * Represents a theme.
 *
 * @since 0.1
 */
public class Theme {

  public static final char THEME_MARKER = '$';
  public static final char THEME_ESCAPE_CHAR = '\\';

  private final Character code;
  private final String theme;

  public Theme(Character code, String theme) {
    this.code = code;
    this.theme = theme;
  }

  public Character getCode() {
    return code;
  }

  public String getTheme() {
    return theme;
  }
}
