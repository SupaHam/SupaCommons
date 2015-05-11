package com.supaham.commons.bukkit.text;

/**
 * Represents a {@link String} parser used to parse messages.
 *
 * @since 0.2.4
 */
public interface Parser {

  /**
   * Parses a {@link String} using this Parser.
   *
   * @param source source to parse
   * @param params params to replace
   *
   * @return an instance of {@link FancyMessage} with the parsed {@code source}
   */
  FancyMessage parse(String source, Object... params) throws Exception;
}
