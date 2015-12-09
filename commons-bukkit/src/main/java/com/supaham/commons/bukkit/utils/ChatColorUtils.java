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

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.serializers.ColorStringSerializer;
import com.supaham.commons.utils.ArrayUtils;
import com.supaham.commons.utils.CollectionUtils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import pluginbase.config.serializers.Serializers;

/**
 * Utility methods for working with {@link ChatColor} instances. This class contains methods such
 * as {@link #randomChatColorExcept(char...)}, {@link #randomChatColorExcept(ChatColor...)},
 * and more.
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

  public static Object serialize(String string) {
    return SerializationUtils.serialize(string, ColorStringSerializer.class);
  }

  public static String deserialize(String string) {
    return SerializationUtils.deserializeWith(string, ColorStringSerializer.class);
  }

  public static ChatColor getFirstColorFromString(String string) {
    return getFirstColorFromString(string, 0);
  }

  public static ChatColor getFirstColorFromString(String string, int index) {
    for (; index < string.length(); index++) {
      if (string.charAt(index) == ChatColor.COLOR_CHAR && index < string.length()) {
        return ChatColor.getByChar(string.charAt(index + 1));
      }
    }
    return null;
  }

  public static List<ChatColor> getPreviousChatColorsFromString(String string, int index,
                                                                ChatColor... ignore) {
    Preconditions.checkArgument(index >= 0, "index cannot be smaller than 0.");
    return getChatColorsFromString(string, 0, index, ignore);
  }

  public static List<ChatColor> getChatColorsFromString(String string, int startIdx, int endIdx,
                                                        ChatColor... ignore) {
    Preconditions.checkArgument(startIdx >= 0, "start index cannot be smaller than 0.");
    if (endIdx >= string.length()) {
      endIdx = string.length() - 1;
    }
    if (startIdx < 2) {
      return Collections.emptyList();
    }

    ArrayList<ChatColor> result = new ArrayList<>();
    for (int i = startIdx; 0 < endIdx; i++) {
      if (string.charAt(i) == ChatColor.COLOR_CHAR && i < endIdx) {
        ChatColor byChar = ChatColor.getByChar(string.charAt(++i));
        if (byChar != null) {
          if (byChar.isColor() || byChar.equals(RESET)) {
            result.clear();
          }
          if (!ArrayUtils.contains(ignore, byChar)) {
            result.add(byChar);
          }
        }
      }
    }
    return result;
  }
}
