/*
 * Copyright 2017 Ali Moghnieh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.supaham.commons.bukkit;

import com.google.common.collect.ImmutableMap;

import com.supaham.commons.FuzzyColorMatcher;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class FuzzyColorMatchers {

  private static final Map<Color, ChatColor> CHAT_COLORS_MAP = ImmutableMap.<Color, ChatColor>builder()
      .put(new Color(0, 0, 0), ChatColor.BLACK)
      .put(new Color(0, 0, 170), ChatColor.DARK_BLUE)
      .put(new Color(0, 170, 0), ChatColor.DARK_GREEN)
      .put(new Color(0, 170, 170), ChatColor.DARK_AQUA)
      .put(new Color(170, 0, 0), ChatColor.DARK_RED)
      .put(new Color(170, 0, 170), ChatColor.DARK_PURPLE)
      .put(new Color(255, 170, 0), ChatColor.GOLD)
      .put(new Color(170, 170, 170), ChatColor.GRAY)
      .put(new Color(85, 85, 85), ChatColor.DARK_GRAY)
      .put(new Color(85, 85, 255), ChatColor.BLUE)
      .put(new Color(85, 255, 85), ChatColor.GREEN)
      .put(new Color(85, 255, 255), ChatColor.AQUA)
      .put(new Color(255, 85, 85), ChatColor.RED)
      .put(new Color(255, 85, 255), ChatColor.LIGHT_PURPLE)
      .put(new Color(255, 255, 85), ChatColor.YELLOW)
      .put(new Color(255, 255, 255), ChatColor.WHITE).build();
  private static final FuzzyColorMatcher CHAT_COLOR_FUZZY_MATCHER = new FuzzyColorMatcher(
      CHAT_COLORS_MAP.keySet().toArray(new Color[0]));
  private static final FuzzyColorMatcher DYE_COLOR_FUZZY_MATCHER;
  private static final Map<Color, DyeColor> DYE_COLORS_MAP;

  static {
    Map<Color, DyeColor> builder = new HashMap<>();
    for (DyeColor dyeColor : DyeColor.values()) {
      builder.put(new Color(dyeColor.getColor().asRGB()), dyeColor);
    }
    DYE_COLORS_MAP = ImmutableMap.copyOf(builder);
    DYE_COLOR_FUZZY_MATCHER = new FuzzyColorMatcher(DYE_COLORS_MAP.keySet().toArray(new Color[0]));

  }

  public static ChatColor matchChatColor(org.bukkit.Color color) {
    if (CHAT_COLORS_MAP.containsKey(color)) {
      return CHAT_COLORS_MAP.get(color);
    } else {
      return CHAT_COLORS_MAP.get(CHAT_COLOR_FUZZY_MATCHER.findMatch(new Color(color.asRGB())));
    }
  }

  public static DyeColor matchDyeColor(org.bukkit.Color color) {
    if (DYE_COLORS_MAP.containsKey(color)) {
      return DYE_COLORS_MAP.get(color);
    } else {
      return DYE_COLORS_MAP.get(DYE_COLOR_FUZZY_MATCHER.findMatch(new Color(color.asRGB())));
    }
  }

  private FuzzyColorMatchers() {
    throw new AssertionError("Are you feeling a bit fuzzy?");
  }
}
