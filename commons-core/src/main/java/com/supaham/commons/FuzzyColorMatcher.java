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

package com.supaham.commons;

import com.google.common.base.Preconditions;

import java.awt.*;
import java.util.Arrays;

import javax.annotation.Nonnull;

/**
 * Fuzzy {@link Color} matching feature. This class allows you to match a given color to a given array of colors. <p />
 *
 * This works by checking the distance between two colours and returning the one with the least distance. <p />
 *
 * Credit goes to Bukkit: https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/map/MapPalette.java
 */
public class FuzzyColorMatcher {

  private final Color[] colors;

  private static double getDistance(Color c1, Color c2) {
    double rmean = (c1.getRed() + c2.getRed()) / 2.0;
    double r = c1.getRed() - c2.getRed();
    double g = c1.getGreen() - c2.getGreen();
    int b = c1.getBlue() - c2.getBlue();
    double weightR = 2 + rmean / 256.0;
    double weightG = 4.0;
    double weightB = 2 + (255 - rmean) / 256.0;
    return weightR * r * r + weightG * g * g + weightB * b * b;
  }

  public FuzzyColorMatcher(@Nonnull Color[] colors) {
    Preconditions.checkNotNull(colors, "colors cannot be null.");
    Preconditions.checkArgument(colors.length > 0, "colors array cannot be empty.");
    this.colors = Arrays.copyOf(colors, colors.length);
  }

  /**
   * Finds a {@link Color} match from a given color based on the colors this matcher was created with. The given
   * color may be returned if there is no better result.
   *
   * @param r red component to find match for
   * @param g green component to find match for
   * @param b blue component to find match for
   *
   * @return match for color
   */
  public Color findMatch(int r, int g, int b) {
    Preconditions.checkArgument(r >= 0 && r <= 255, "red must be within 0-255.");
    Preconditions.checkArgument(g >= 0 && g <= 255, "green must be within 0-255.");
    Preconditions.checkArgument(b >= 0 && b <= 255, "blue must be within 0-255.");
    return findMatch(new Color(r, g, b));
  }

  /**
   * Finds a {@link Color} match from a given {@link Color} based on the colors this matcher was created with. The given
   * color may be returned if there is no better result.
   *
   * @param color color to find match for
   *
   * @return match for color
   */
  @Nonnull
  public Color findMatch(@Nonnull Color color) {
    Preconditions.checkNotNull(color, "color1 cannot be null.");
    Preconditions.checkArgument(color.getAlpha() == 255, "Only fully opaque colours allowed.");
    Color bestColor = color;
    double bestDistance = Double.MAX_VALUE;
    for (Color color2 : this.colors) {
      double distance = getDistance(color, color2);
      if (distance < bestDistance) {
        bestColor = color2;
        bestDistance = distance;
      }

    }
    return bestColor;
  }
}
