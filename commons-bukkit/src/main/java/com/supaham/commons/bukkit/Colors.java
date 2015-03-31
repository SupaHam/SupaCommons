package com.supaham.commons.bukkit;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.BLACK;
import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_BLUE;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.DARK_GREEN;
import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.LIGHT_PURPLE;
import static org.bukkit.ChatColor.MAGIC;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.STRIKETHROUGH;
import static org.bukkit.ChatColor.UNDERLINE;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;

/**
 * Represents a {@link ChatColor} helper class. All static access to this class starts with an
 * underscore (_). Anything after that is just the {@link ChatColor} enum to camelCase.
 * <p>
 * <b>Example usage:</b><br />
 * Colors._yellow().bold().italic() = ChatColor.YELLOW.toString() + ChatColor.BOLD +
 * ChatColor.ITALIC
 * </p>
 *
 * @since 0.1
 */
public class Colors {

  private final StringBuilder builder;

  /** @see ChatColor#BLACK */
  public static Colors _black() {
    return new Colors(BLACK);
  }

  /** @see ChatColor#DARK_BLUE */
  public static Colors _darkBlue() {
    return new Colors(DARK_BLUE);
  }

  /** @see ChatColor#DARK_GREEN */
  public static Colors _darkGreen() {
    return new Colors(DARK_GREEN);
  }

  /** @see ChatColor#DARK_AQUA */
  public static Colors _darkAqua() {
    return new Colors(DARK_AQUA);
  }

  /** @see ChatColor#DARK_RED */
  public static Colors _darkRed() {
    return new Colors(DARK_RED);
  }

  /** @see ChatColor#DARK_PURPLE */
  public static Colors _darkPurple() {
    return new Colors(DARK_PURPLE);
  }

  /** @see ChatColor#GOLD */
  public static Colors _gold() {
    return new Colors(GOLD);
  }

  /** @see ChatColor#GRAY */
  public static Colors _gray() {
    return new Colors(GRAY);
  }

  /** @see ChatColor#DARK_GRAY */
  public static Colors _darkGray() {
    return new Colors(DARK_GRAY);
  }

  /** @see ChatColor#BLUE */
  public static Colors _blue() {
    return new Colors(BLUE);
  }

  /** @see ChatColor#GREEN */
  public static Colors _green() {
    return new Colors(GREEN);
  }

  /** @see ChatColor#AQUA */
  public static Colors _aqua() {
    return new Colors(AQUA);
  }

  /** @see ChatColor#RED */
  public static Colors _red() {
    return new Colors(RED);
  }

  /** @see ChatColor#LIGHT_PURPLE */
  public static Colors _lightPurple() {
    return new Colors(LIGHT_PURPLE);
  }

  /** @see ChatColor#YELLOW */
  public static Colors _yellow() {
    return new Colors(YELLOW);
  }

  /** @see ChatColor#WHITE */
  public static Colors _white() {
    return new Colors(WHITE);
  }

  /** @see ChatColor#MAGIC */
  public static Colors _magic() {
    return new Colors(MAGIC);
  }

  /** @see ChatColor#BOLD */
  public static Colors _bold() {
    return new Colors(BOLD);
  }

  /** @see ChatColor#STRIKETHROUGH */
  public static Colors _strike() {
    return new Colors(STRIKETHROUGH);
  }

  /** @see ChatColor#UNDERLINE */
  public static Colors _underline() {
    return new Colors(UNDERLINE);
  }

  /** @see ChatColor#ITALIC */
  public static Colors _italic() {
    return new Colors(ITALIC);
  }

  /** @see ChatColor#RESET */
  public static Colors _reset() {
    return new Colors(RESET);
  }

  /**
   * Creates a new instance of {@link Colors} with a given {@link String} appended to it.
   *
   * @param string string to append
   *
   * @return the new instance of this builder
   */
  public static Colors _append(@Nullable String string) {
    return new Colors(string);
  }

  private Colors(ChatColor color) {
    this(color.toString());
  }

  private Colors(String string) {
    this.builder = new StringBuilder(string != null ? string.length() : 16);
    append(string);
  }

  /** @see ChatColor#BLACK */
  public Colors black() {
    return append(BLACK);
  }

  /** @see ChatColor#DARK_BLUE */
  public Colors darkBlue() {
    return append(DARK_BLUE);
  }

  /** @see ChatColor#DARK_GREEN */
  public Colors darkGreen() {
    return append(DARK_GREEN);
  }

  /** @see ChatColor#DARK_AQUA */
  public Colors darkAqua() {
    return append(DARK_AQUA);
  }

  /** @see ChatColor#DARK_RED */
  public Colors darkRed() {
    return append(DARK_RED);
  }

  /** @see ChatColor#DARK_PURPLE */
  public Colors darkPurple() {
    return append(DARK_PURPLE);
  }

  /** @see ChatColor#GOLD */
  public Colors gold() {
    return append(GOLD);
  }

  /** @see ChatColor#GRAY */
  public Colors gray() {
    return append(GRAY);
  }

  /** @see ChatColor#DARK_GRAY */
  public Colors darkGray() {
    return append(DARK_GRAY);
  }

  /** @see ChatColor#BLUE */
  public Colors blue() {
    return append(BLUE);
  }

  /** @see ChatColor#GREEN */
  public Colors green() {
    return append(GREEN);
  }

  /** @see ChatColor#AQUA */
  public Colors aqua() {
    return append(AQUA);
  }

  /** @see ChatColor#RED */
  public Colors red() {
    return append(RED);
  }

  /** @see ChatColor#LIGHT_PURPLE */
  public Colors lightPurple() {
    return append(LIGHT_PURPLE);
  }

  /** @see ChatColor#YELLOW */
  public Colors yellow() {
    return append(YELLOW);
  }

  /** @see ChatColor#WHITE */
  public Colors white() {
    return append(WHITE);
  }

  /** @see ChatColor#MAGIC */
  public Colors magic() {
    return append(MAGIC);
  }

  /** @see ChatColor#BOLD */
  public Colors bold() {
    return append(BOLD);
  }

  /** @see ChatColor#STRIKETHROUGH */
  public Colors strike() {
    return append(STRIKETHROUGH);
  }

  /** @see ChatColor#UNDERLINE */
  public Colors underline() {
    return append(UNDERLINE);
  }

  /** @see ChatColor#ITALIC */
  public Colors italic() {
    return append(ITALIC);
  }

  /** @see ChatColor#RESET */
  public Colors reset() {
    return append(RESET);
  }

  /**
   * Appends a {@link String} to this {@link Colors}.
   *
   * @param string string to append
   *
   * @return this instance for chaining
   */
  public Colors append(String string) {
    this.builder.append(string);
    return this;
  }

  private Colors append(ChatColor color) {
    this.builder.append(color);
    return this;
  }
}
