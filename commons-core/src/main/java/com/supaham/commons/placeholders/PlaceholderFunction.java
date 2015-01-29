package com.supaham.commons.placeholders;

import com.google.common.base.Function;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * Represents a {@link Function} implementation for placeholder strings replacement task.
 */
public abstract class PlaceholderFunction implements Function<String, String> {

  /**
   * This represents the pattern <b>{@code ([{]+.[^{}]+[}]+)}</b>. The following String is an
   * example that would get matched by this pattern. Anything formatted in bold is what would be
   * matched in the actual example.
   * <pre>
   *   Hi, my name is <b>{name}</b>. I live in <b>{city}</b>, but was born in <b>{birthplace}</b>.
   * </pre>
   */
  public final static Pattern PH_PATTERN = Pattern.compile("([{]+.[^{}]+[}]+)");

  /**
   * Gets a {@link Collection} of {@link Placeholder}s to use when calling {@link #apply(String)}.
   *
   * @return collection of placeholders
   */
  abstract Collection<? extends Placeholder> getPlaceholders();

  /**
   * Performs a placeholder replacing task. This method utilizes {@link #getPlaceholders()} for 
   * passing the matched placeholders to them. This method uses the {@link #PH_PATTERN} for 
   * matching placeholders. Please keep in mind that the {@link Placeholder#apply(String)} will not 
   * receive the braces, e.g. <em>{abc}</em> is given as <em>abc</em> 
   *
   * @param input input to search for placeholders in
   *
   * @return the placeholder replaced string
   */
  @Nonnull
  @Override
  public String apply(String input) {
    Collection<? extends Placeholder> placeholders = getPlaceholders();
    String result = "";
    int index = 0;

    Matcher matcher = PH_PATTERN.matcher(input);
    // Every matched placeholder
    while (matcher.find()) {
      String origPhStr = matcher.group();
      // Remove the braces.
      String phStr = origPhStr.substring(1, origPhStr.length() - 1);
      
      boolean found = false;
      // Ask each Placeholder to handle the matched placeholder string until one Placeholder has.
      for (Placeholder placeholder : placeholders) {
        String match = placeholder.apply(phStr);
        // Match is null when the placeholder has not applied anything to it.
        if (match != null) {
          phStr = match;
          found = true;
          break;
        }
      }
      // Append since the last index till the current matched starting index, then append the
      // placeholder string. Finally, update the index to the end of the matched placeholder string.
      result += input.substring(index, matcher.start()) + (found ? phStr : origPhStr);
      index = matcher.end();
    }

    // If there's still input after the last matched, append it all to result.
    if (index < input.length()) {
      result += input.substring(index, input.length());
    }

    // Let's notify the placeholders that we're done
    for (Placeholder placeholder : placeholders) {
      placeholder.onComplete(result);
    }
    return result;
  }
}
