package com.supaham.commons.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.CharMatcher;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Contains {@link String} related utility methods.
 */
public final class StringUtils {

  private StringUtils() {
  }

  /**
   * Checks if a {@link String} contains only ASCII characters.
   *
   * @param string the String to check
   *
   * @return whether the {@code string} is ASCII only
   */
  public static boolean isASCII(@Nonnull String string) {
    return CharMatcher.ASCII.matchesAllOf(checkNotNull(string, "string cannot be null."));
  }

  /**
   * Separates multiple {@link String}s with an equal amount of spaces between all provided
   * Strings.
   * <p/>
   * E.g.:
   * <pre>
   *   StringUtils.formatWhitespace(0, "") = {@link IllegalArgumentException}
   *   StringUtils.formatWhitespace(0, null) = {@link NullPointerException}
   * </pre>
   *
   * @param length length of the String
   * @param args Strings to center
   *
   * @return the formatted string
   */
  @Nonnull
  public static String formatWhitespace(int length, @Nullable String... args) {
    if (length == 0 || args == null || args.length == 0) {
      return "";
    }

    // Calculate the total length of all the provided arguments combined, and the actual argument
    // length, removing any empty or null provided strings.
    args = ArrayUtils.removeEmptyStrings(args, true);
    int totalLength = ArrayUtils.getTotalStringsLength(args);

    checkArgument(totalLength <= length, "args length can not be larger than the length.");

    // This supports only providing one arg, currently redundant, but I thought it better failing 
    // safely.
    if (args.length <= 1) {
      return args[0];
    }

    StringBuilder builder = new StringBuilder(length);
    // Spaces to add, I think this might need some more precision as it does not account for oddness
    int spaces = (int) Math.ceil((length - totalLength) / ((double) args.length - 1));
    String whitespaces = repeat(" ", spaces);

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      builder.append(arg);

      // Check that this isn't the last argument, if that's the case, add the whitespaces.
      if ((i + 1) < args.length) {
        builder.append(whitespaces);
      }
    }
    return builder.toString();
  }

  /**
   * @see org.apache.commons.lang3.StringUtils#repeat(String, int)
   */
  @Contract("!null,_->!null;null,_->null")
  public static String repeat(@Nullable String str, int repeat) {
    return org.apache.commons.lang3.StringUtils.repeat(str, repeat);
  }

  /**
   * Normalizes a string. The following characters are converted to an underscore '_':
   * <ul>
   * <li><b>hyphen</b> '-'</li>
   * <li><b>space</b> ' '</li>
   * </ul>
   *
   * @param str
   *
   * @return
   */
  @Contract("null -> null")
  public static String normalizeString(@Nullable String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("[- ]*", "_").toLowerCase();
  }

  /**
   * Checks if a String is null or empty, if it is, an exception is thrown.
   * <p/>
   * E.g.:
   * <pre>
   *   StringUtils.checkNotNullOrEmpty(null)    = {@link NullPointerException}
   *   StringUtils.checkNotNullOrEmpty("")      = {@link IllegalArgumentException}
   *   StringUtils.checkNotNullOrEmpty("Hello") = "Hello"
   * </pre>
   *
   * @param string string to check
   *
   * @return the same exact {@code string} returned for chaining.
   *
   * @throws NullPointerException thrown if the {@code string} is null
   * @throws IllegalArgumentException thrown if the {@code string} is empty
   * @see #checkNotNullOrEmpty(String, String)
   */
  public static String checkNotNullOrEmpty(@Nullable String string) throws NullPointerException,
                                                                           IllegalArgumentException {
    return checkNotNullOrEmpty(string, null);
  }

  /**
   * Checks if a String is null or empty, if it is, an exception is thrown.
   * <p/>
   * E.g.:
   * <pre>
   *   StringUtils.checkNotNullOrEmpty(null, "mystring")      = {@link NullPointerException}
   *   StringUtils.checkNotNullOrEmpty("", "mystring")        = {@link IllegalArgumentException}
   *   StringUtils.checkNotNullOrEmpty("Hello", "mystring")   = "Hello"
   * </pre>
   *
   * @param string string to check
   * @param desc name of the {@code string} used to detail the thrown exception. If null, it is
   * then set to "String".
   *
   * @return the same exact {@code string} returned for chaining.
   *
   * @throws NullPointerException thrown if the {@code string} is null
   * @throws IllegalArgumentException thrown if the {@code string} is empty
   */
  public static String checkNotNullOrEmpty(@Nullable String string, @Nullable String desc)
      throws NullPointerException, IllegalArgumentException {
    if (desc == null) {
      desc = "string";
    }
    checkNotNull(string, desc + " cannot be null.");
    checkArgument(!string.isEmpty(), desc + " cannot be empty.");
    return string;
  }
}
