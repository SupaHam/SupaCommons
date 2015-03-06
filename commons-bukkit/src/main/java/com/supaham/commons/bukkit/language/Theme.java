package com.supaham.commons.bukkit.language;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a theme.
 *
 * @since 0.1
 */
@RequiredArgsConstructor
public class Theme {

  public static final char THEME_MARKER = '$';
  public static final char THEME_ESCAPE_CHAR = '\\';

  @Getter
  private final Character code;
  @Getter
  private final String theme;
}
