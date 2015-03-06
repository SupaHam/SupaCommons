package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.bukkit.ChatColor.BLACK;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_BLUE;
import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.MAGIC;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.STRIKETHROUGH;
import static org.bukkit.ChatColor.UNDERLINE;

import com.supaham.commons.utils.CollectionUtils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link ChatColor} instances. This class contains methods such as
 * {@link #randomChatColorExcept(char...)}, {@link #randomChatColorExcept(ChatColor...)}, and more.
 *
 * @since 0.1
 */
public class ChatColorUtils {

  public static final ChatColor[] STYLES = new ChatColor[]{MAGIC, BOLD, STRIKETHROUGH, UNDERLINE,
                                                           ITALIC, RESET};
  public static final ChatColor[] DARK_COLORS = new ChatColor[]{BLACK, DARK_BLUE, DARK_RED,
                                                                DARK_PURPLE};

  /**
   * Returns a random {@link ChatColor} except those provided, assuming any are provided.
   *
   * @param except color codes to ignore
   *
   * @return a pseudorandom chat color
   *
   * @see #randomChatColorExcept(ChatColor...)
   */
  public static ChatColor randomChatColorExcept(@Nonnull char... except) {
    checkNotNull(except, "colors cannot be null.");
    checkArgument(except.length > 0, "at least one color must be provided.");
    ChatColor[] arr = new ChatColor[except.length];
    int i = 0;
    for (char c : except) {
      ChatColor byChar = ChatColor.getByChar(c);
      if (byChar != null) {
        arr[i] = byChar;
        i++;
      }
    }
    return randomChatColorExcept(arr);
  }

  /**
   * Returns a random {@link ChatColor} except those provided, assuming any are provided.
   *
   * @param except chat colors to ignore
   *
   * @return a pseudorandom chat color
   *
   * @see #randomChatColorExcept(ChatColor[]...)
   */
  public static ChatColor randomChatColorExcept(@Nonnull ChatColor... except) {
    checkNotNull(except, "colors cannot be null.");
    checkArgument(except.length > 0, "at least one color must be provided.");
    return randomChatColorExcept((ChatColor[][]) new ChatColor[][]{except});
  }

  /**
   * Returns a random {@link ChatColor} except those provided, assuming any are provided.
   *
   * @param except chat colors to ignore
   *
   * @return a pseudorandom chat color
   */
  public static ChatColor randomChatColorExcept(@Nonnull ChatColor[]... except) {
    checkNotNull(except, "colors cannot be null.");
    checkArgument(except.length > 0, "at least one color must be provided.");
    List<ChatColor> result = new ArrayList<>(Arrays.asList(ChatColor.values()));
    for (ChatColor[] chatColors : except) {
      result.removeAll(Arrays.asList(chatColors));
    }
    return CollectionUtils.getRandomElement(result);
  }

  /**
   * Returns a random {@link ChatColor} from the given colors.
   *
   * @param colors array of the colors to provide
   *
   * @return a pseudorandom chat color
   */
  public static ChatColor randomChatColor(@Nonnull char... colors) {
    checkNotNull(colors, "colors cannot be null.");
    checkArgument(colors.length > 0, "at least one color must be provided.");
    ChatColor[] arr = new ChatColor[colors.length];
    int i = 0;
    for (char c : colors) {
      ChatColor byChar = ChatColor.getByChar(c);
      if (byChar != null) {
        arr[i] = byChar;
        i++;
      }
    }
    return randomChatColorExcept(arr);
  }

  /**
   * Returns a random {@link ChatColor} from a collection.
   *
   * @param colors array of the colors to provide
   *
   * @return a pseudorandom chat color
   */
  public static ChatColor randomChatColor(@Nonnull ChatColor... colors) {
    checkNotNull(colors, "colors cannot be null.");
    checkArgument(colors.length > 0, "at least one color must be provided.");
    return randomChatColor((ChatColor[][]) new ChatColor[][]{colors});
  }

  /**
   * Returns a random {@link ChatColor} from a collection.
   *
   * @param colors array of the colors to provide
   *
   * @return a pseudorandom chat color
   */
  public static ChatColor randomChatColor(@Nonnull ChatColor[]... colors) {
    checkNotNull(colors, "colors cannot be null.");
    checkArgument(colors.length > 0, "at least one color must be provided.");
    Set<ChatColor> result = new HashSet<>();
    for (ChatColor[] chatColors : colors) {
      result.addAll(Arrays.asList(chatColors));
    }
    checkArgument(!result.isEmpty(), "at least one color must be provided.");
    return CollectionUtils.getRandomElement(result);
  }
}
