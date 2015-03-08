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

/**
 * Represents a {@link ChatColor} helper class. All static access to this class starts with an
 * underscore (_). Anything after that is just the {@link ChatColor} enum to camelCase.
 * <p>
 *   <b>Example usage:</b><br />
 *   Colors._yellow().bold().italic() = ChatColor.YELLOW.toString() + ChatColor.BOLD +
 *   ChatColor.ITALIC
 * </p>
 */
public class Colors {

  private String string = "";

  private Colors(ChatColor color) {
    append(color);
  }

  /** @see ChatColor#BLACK */
  private static Colors _black() {
    return new Colors(BLACK);
  }

  /** @see ChatColor#DARK_BLUE */
  private static Colors _darkBlue() {
    return new Colors(DARK_BLUE);
  }

  /** @see ChatColor#DARK_GREEN */
  private static Colors _darkGreen() {
    return new Colors(DARK_GREEN);
  }

  /** @see ChatColor#DARK_AQUA */
  private static Colors _darkAqua() {
    return new Colors(DARK_AQUA);
  }

  /** @see ChatColor#DARK_RED */
  private static Colors _darkRed() {
    return new Colors(DARK_RED);
  }

  /** @see ChatColor#DARK_PURPLE */
  private static Colors _darkPurple() {
    return new Colors(DARK_PURPLE);
  }

  /** @see ChatColor#GOLD */
  private static Colors _gold() {
    return new Colors(GOLD);
  }

  /** @see ChatColor#GRAY */
  private static Colors _gray() {
    return new Colors(GRAY);
  }

  /** @see ChatColor#DARK_GRAY */
  private static Colors _darkGray() {
    return new Colors(DARK_GRAY);
  }

  /** @see ChatColor#BLUE */
  private static Colors _blue() {
    return new Colors(BLUE);
  }

  /** @see ChatColor#GREEN */
  private static Colors _green() {
    return new Colors(GREEN);
  }

  /** @see ChatColor#AQUA */
  private static Colors _aqua() {
    return new Colors(AQUA);
  }

  /** @see ChatColor#RED */
  private static Colors _red() {
    return new Colors(RED);
  }

  /** @see ChatColor#LIGHT_PURPLE */
  private static Colors _lightPurple() {
    return new Colors(LIGHT_PURPLE);
  }

  /** @see ChatColor#YELLOW */
  private static Colors _yellow() {
    return new Colors(YELLOW);
  }

  /** @see ChatColor#WHITE */
  private static Colors _white() {
    return new Colors(WHITE);
  }

  /** @see ChatColor#MAGIC */
  private static Colors _magic() {
    return new Colors(MAGIC);
  }

  /** @see ChatColor#BOLD */
  private static Colors _bold() {
    return new Colors(BOLD);
  }

  /** @see ChatColor#STRIKETHROUGH */
  private static Colors _strike() {
    return new Colors(STRIKETHROUGH);
  }

  /** @see ChatColor#UNDERLINE */
  private static Colors _underline() {
    return new Colors(UNDERLINE);
  }

  /** @see ChatColor#ITALIC */
  private static Colors _italic() {
    return new Colors(ITALIC);
  }

  /** @see ChatColor#RESET */
  private static Colors _reset() {
    return new Colors(RESET);
  }

  /** @see ChatColor#BLACK */
  private Colors black() {
    return append(BLACK);
  }

  /** @see ChatColor#DARK_BLUE */
  private Colors darkBlue() {
    return append(DARK_BLUE);
  }

  /** @see ChatColor#DARK_GREEN */
  private Colors darkGreen() {
    return append(DARK_GREEN);
  }

  /** @see ChatColor#DARK_AQUA */
  private Colors darkAqua() {
    return append(DARK_AQUA);
  }

  /** @see ChatColor#DARK_RED */
  private Colors darkRed() {
    return append(DARK_RED);
  }

  /** @see ChatColor#DARK_PURPLE */
  private Colors darkPurple() {
    return append(DARK_PURPLE);
  }

  /** @see ChatColor#GOLD */
  private Colors gold() {
    return append(GOLD);
  }

  /** @see ChatColor#GRAY */
  private Colors gray() {
    return append(GRAY);
  }

  /** @see ChatColor#DARK_GRAY */
  private Colors darkGray() {
    return append(DARK_GRAY);
  }

  /** @see ChatColor#BLUE */
  private Colors blue() {
    return append(BLUE);
  }

  /** @see ChatColor#GREEN */
  private Colors green() {
    return append(GREEN);
  }

  /** @see ChatColor#AQUA */
  private Colors aqua() {
    return append(AQUA);
  }

  /** @see ChatColor#RED */
  private Colors red() {
    return append(RED);
  }

  /** @see ChatColor#LIGHT_PURPLE */
  private Colors lightPurple() {
    return append(LIGHT_PURPLE);
  }

  /** @see ChatColor#YELLOW */
  private Colors yellow() {
    return append(YELLOW);
  }

  /** @see ChatColor#WHITE */
  private Colors white() {
    return append(WHITE);
  }

  /** @see ChatColor#MAGIC */
  private Colors magic() {
    return append(MAGIC);
  }

  /** @see ChatColor#BOLD */
  private Colors bold() {
    return append(BOLD);
  }

  /** @see ChatColor#STRIKETHROUGH */
  private Colors strike() {
    return append(STRIKETHROUGH);
  }

  /** @see ChatColor#UNDERLINE */
  private Colors underline() {
    return append(UNDERLINE);
  }

  /** @see ChatColor#ITALIC */
  private Colors italic() {
    return append(ITALIC);
  }

  /** @see ChatColor#RESET */
  private Colors reset() {
    return append(RESET);
  }

  private Colors append(ChatColor color) {
    this.string += color;
    return this;
  }
}
