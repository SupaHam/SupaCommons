package com.supaham.commons.bukkit.utils.misc;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import com.supaham.commons.bukkit.ItemBuilder;
import com.supaham.commons.utils.StringUtils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * MinersNeedCoolShoes' (offline) java banner generator.
 *
 * <table>
 * <thead>
 * <tr>
 * <th>Code</th>
 * <th>Color</th>
 * </tr>
 * <tr><td>a</td><td>{@link DyeColor#BLACK}</td></tr>
 * <tr><td>i</td><td>{@link DyeColor#GRAY}</td></tr>
 * <tr><td>h</td><td>{@link DyeColor#LIGHT_GRAY}</td></tr>
 * <tr><td>p</td><td>{@link DyeColor#WHITE}</td></tr>
 * <tr><td>j</td><td>{@link DyeColor#PINK}</td></tr>
 * <tr><td>n</td><td>{@link DyeColor#MAGENTA}</td></tr>
 * <tr><td>f</td><td>{@link DyeColor#PURPLE}</td></tr>
 * <tr><td>e</td><td>{@link DyeColor#BLUE}</td></tr>
 * <tr><td>g</td><td>{@link DyeColor#CYAN}</td></tr>
 * <tr><td>m</td><td>{@link DyeColor#LIGHT_BLUE}</td></tr>
 * <tr><td>c</td><td>{@link DyeColor#GREEN}</td></tr>
 * <tr><td>l</td><td>{@link DyeColor#YELLOW}</td></tr>
 * <tr><td>o</td><td>{@link DyeColor#ORANGE}</td></tr>
 * <tr><td>d</td><td>{@link DyeColor#BROWN}</td></tr>
 * <tr><td>b</td><td>{@link DyeColor#RED}</td></tr>
 * </thead>
 * </table>
 *
 * <table>
 * <thead>
 * <tr>
 * <th>Code</th>
 * <th>Pattern</th>
 * </tr>
 * <tr><td>a</td><td>{@link PatternType#BASE}</td></tr>
 * <tr><td>p</td><td>{@link PatternType#GRADIENT}</td></tr>
 * <tr><td>K</td><td>{@link PatternType#GRADIENT_UP}</td></tr>
 * <tr><td>e</td><td>{@link PatternType#BRICKS}</td></tr>
 * <tr><td>q</td><td>{@link PatternType#HALF_HORIZONTAL}</td></tr>
 * <tr><td>L</td><td>{@link PatternType#HALF_HORIZONTAL_BOTTOM}</td></tr>
 * <tr><td>H</td><td>{@link PatternType#HALF_VERTICAL}</td></tr>
 * <tr><td>M</td><td>{@link PatternType#HALF_VERTICAL_RIGHT}</td></tr>
 * <tr><td>E</td><td>{@link PatternType#STRIPE_TOP}</td></tr>
 * <tr><td>f</td><td>{@link PatternType#STRIPE_BOTTOM}</td></tr>
 * <tr><td>s</td><td>{@link PatternType#STRIPE_LEFT}</td></tr>
 * <tr><td>y</td><td>{@link PatternType#STRIPE_RIGHT}</td></tr>
 * <tr><td>r</td><td>{@link PatternType#DIAGONAL_LEFT}</td></tr>
 * <tr><td>J</td><td>{@link PatternType#DIAGONAL_RIGHT}</td></tr>
 * <tr><td>I</td><td>{@link PatternType#DIAGONAL_UP_LEFT}</td></tr>
 * <tr><td>x</td><td>{@link PatternType#DIAGONAL_UP_RIGHT}</td></tr>
 * <tr><td>j</td><td>{@link PatternType#CROSS}</td></tr>
 * <tr><td>m</td><td>{@link PatternType#STRIPE_DOWNLEFT}</td></tr>
 * <tr><td>n</td><td>{@link PatternType#STRIPE_DOWNRIGHT}</td></tr>
 * <tr><td>z</td><td>{@link PatternType#STRAIGHT_CROSS}</td></tr>
 * <tr><td>l</td><td>{@link PatternType#STRIPE_CENTER}</td></tr>
 * <tr><td>w</td><td>{@link PatternType#STRIPE_MIDDLE}</td></tr>
 * <tr><td>C</td><td>{@link PatternType#SQUARE_TOP_LEFT}</td></tr>
 * <tr><td>b</td><td>{@link PatternType#SQUARE_BOTTOM_LEFT}</td></tr>
 * <tr><td>D</td><td>{@link PatternType#SQUARE_TOP_RIGHT}</td></tr>
 * <tr><td>d</td><td>{@link PatternType#SQUARE_BOTTOM_RIGHT}</td></tr>
 * <tr><td>F</td><td>{@link PatternType#TRIANGLE_TOP}</td></tr>
 * <tr><td>g</td><td>{@link PatternType#TRIANGLE_BOTTOM}</td></tr>
 * <tr><td>v</td><td>{@link PatternType#RHOMBUS}</td></tr>
 * <tr><td>t</td><td>{@link PatternType#CIRCLE}</td></tr>
 * <tr><td>h</td><td>{@link PatternType#TRIANGLES_BOTTOM}</td></tr>
 * <tr><td>G</td><td>{@link PatternType#TRIANGLES_TOP}</td></tr>
 * <tr><td>B</td><td>{@link PatternType#SMALL_STRIPES}</td></tr>
 * <tr><td>c</td><td>{@link PatternType#BORDER}</td></tr>
 * <tr><td>i</td><td>{@link PatternType#CURLY_BORDER}</td></tr>
 * <tr><td>o</td><td>{@link PatternType#FLOWER}</td></tr>
 * <tr><td>k</td><td>{@link PatternType#CREEPER}</td></tr>
 * <tr><td>A</td><td>{@link PatternType#SKULL}</td></tr>
 * <tr><td>u</td><td>{@link PatternType#MOJANG}</td></tr>
 * </thead>
 * </table>
 *
 * @author SupaHam
 * @see #parseCode(String)
 * @see #parseURL(String)
 * @since 0.2.9.1
 */
public class MNCSBannerGenerator {

  private static final BiMap<Character, DyeColor> COLOR_CODES = HashBiMap.create();
  private static final BiMap<Character, PatternType> PATTERN_CODES = HashBiMap.create();
  public static final Pattern MATCH_URL =
      Pattern.compile("https?://www.needcoolshoes.com/banner\\?=(.*)");

  static {
    COLOR_CODES.put('a', DyeColor.BLACK);
    COLOR_CODES.put('i', DyeColor.GRAY);
    COLOR_CODES.put('h', DyeColor.LIGHT_GRAY);
    COLOR_CODES.put('p', DyeColor.WHITE);
    COLOR_CODES.put('j', DyeColor.PINK);
    COLOR_CODES.put('n', DyeColor.MAGENTA);
    COLOR_CODES.put('f', DyeColor.PURPLE);
    COLOR_CODES.put('e', DyeColor.BLUE);
    COLOR_CODES.put('g', DyeColor.CYAN);
    COLOR_CODES.put('m', DyeColor.LIGHT_BLUE);
    COLOR_CODES.put('c', DyeColor.GREEN);
    COLOR_CODES.put('k', DyeColor.LIME);
    COLOR_CODES.put('l', DyeColor.YELLOW);
    COLOR_CODES.put('o', DyeColor.ORANGE);
    COLOR_CODES.put('d', DyeColor.BROWN);
    COLOR_CODES.put('b', DyeColor.RED);

    PATTERN_CODES.put('a', PatternType.BASE);
    PATTERN_CODES.put('p', PatternType.GRADIENT);
    PATTERN_CODES.put('K', PatternType.GRADIENT_UP);
    PATTERN_CODES.put('e', PatternType.BRICKS);
    PATTERN_CODES.put('q', PatternType.HALF_HORIZONTAL);
    PATTERN_CODES.put('L', PatternType.HALF_HORIZONTAL_BOTTOM);
    PATTERN_CODES.put('H', PatternType.HALF_VERTICAL);
    PATTERN_CODES.put('M', PatternType.HALF_VERTICAL_RIGHT);
    PATTERN_CODES.put('E', PatternType.STRIPE_TOP);
    PATTERN_CODES.put('f', PatternType.STRIPE_BOTTOM);
    PATTERN_CODES.put('s', PatternType.STRIPE_LEFT);
    PATTERN_CODES.put('y', PatternType.STRIPE_RIGHT);
    PATTERN_CODES.put('r', PatternType.DIAGONAL_LEFT);
    PATTERN_CODES.put('J', PatternType.DIAGONAL_RIGHT);
    PATTERN_CODES.put('I', PatternType.DIAGONAL_UP_LEFT);
    PATTERN_CODES.put('x', PatternType.DIAGONAL_UP_RIGHT);
    PATTERN_CODES.put('j', PatternType.CROSS);
    PATTERN_CODES.put('m', PatternType.STRIPE_DOWNLEFT);
    PATTERN_CODES.put('n', PatternType.STRIPE_DOWNRIGHT);
    PATTERN_CODES.put('z', PatternType.STRAIGHT_CROSS);
    PATTERN_CODES.put('l', PatternType.STRIPE_CENTER);
    PATTERN_CODES.put('w', PatternType.STRIPE_MIDDLE);
    PATTERN_CODES.put('C', PatternType.SQUARE_TOP_LEFT);
    PATTERN_CODES.put('b', PatternType.SQUARE_BOTTOM_LEFT);
    PATTERN_CODES.put('D', PatternType.SQUARE_TOP_RIGHT);
    PATTERN_CODES.put('d', PatternType.SQUARE_BOTTOM_RIGHT);
    PATTERN_CODES.put('F', PatternType.TRIANGLE_TOP);
    PATTERN_CODES.put('g', PatternType.TRIANGLE_BOTTOM);
    PATTERN_CODES.put('v', PatternType.RHOMBUS);
    PATTERN_CODES.put('t', PatternType.CIRCLE);
    PATTERN_CODES.put('h', PatternType.TRIANGLES_BOTTOM);
    PATTERN_CODES.put('G', PatternType.TRIANGLES_TOP);
    PATTERN_CODES.put('B', PatternType.SMALL_STRIPES);
    PATTERN_CODES.put('c', PatternType.BORDER);
    PATTERN_CODES.put('i', PatternType.CURLY_BORDER);
    PATTERN_CODES.put('o', PatternType.FLOWER);
    PATTERN_CODES.put('k', PatternType.CREEPER);
    PATTERN_CODES.put('A', PatternType.SKULL);
    PATTERN_CODES.put('u', PatternType.MOJANG);
  }

  /**
   * Parses a MinersNeedCoolShoes Banner generator generated code into a Bukkit Banner ItemStack.
   * Code must have an even amount of characters, with the first of the
   * pair being the color, and the second being the pattern. See {@link MNCSBannerGenerator} for
   * a table of those.
   *
   * @param code code to parse
   *
   * @return banner itemstack
   */
  public static ItemStack parseCode(@Nonnull String code) {
    StringUtils.checkNotNullOrEmpty(code, "code");
    Preconditions.checkArgument(code.length() % 2 == 0, "code must be even characters");
    ItemBuilder builder = ItemBuilder.builder(Material.WHITE_BANNER);
    char[] chars = code.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      DyeColor dyeColor = COLOR_CODES.get(chars[i]);
      Preconditions.checkNotNull(dyeColor, "'" + chars[i] + " is not a valid color.");
      PatternType patternType = PATTERN_CODES.get(chars[++i]);
      Preconditions.checkNotNull(patternType, "'" + chars[i] + " is not a valid pattern.");
      builder.bannerAdd(new org.bukkit.block.banner.Pattern(dyeColor, patternType));
    }
    return builder.build();
  }

  /**
   * Parses a URL such as {@code http://www.needcoolshoes.com/banner?=naau} into a code, the
   * trailing code, which is passed to {@link #parseCode(String)} to return an {@link ItemStack}.
   *
   * @param url url to parse
   *
   * @return banner itemstack
   *
   * @see #parseCode(String)
   */
  public static ItemStack parseURL(@Nonnull String url) {
    StringUtils.checkNotNullOrEmpty(url, "url");
    Matcher matcher = MATCH_URL.matcher(url);
    if (matcher.find()) {
      return parseCode(matcher.group(1));
    }
    return null;
  }

  private MNCSBannerGenerator() {
    throw new AssertionError("So... statics, yeah. Use them.");
  }
}
