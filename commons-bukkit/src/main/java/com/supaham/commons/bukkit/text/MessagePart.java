package com.supaham.commons.bukkit.text;

import com.google.common.base.Preconditions;
import com.google.gson.stream.JsonWriter;

import com.supaham.commons.bukkit.utils.ReflectionUtils;

import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Part of a message belonging to a {@link FancyMessage}.
 *
 * @since 0.2.4
 */
public class MessagePart implements Cloneable {

  ChatColor color = null;
  ArrayList<ChatColor> styles = new ArrayList<ChatColor>();
  String clickEvent = null, clickEventData = null,
      hoverEvent = null, hoverEventData = null;
  String text = "";

  protected static Class<?> nmsTagCompound = ReflectionUtils.getNMSClass("NBTTagCompound");
  protected static Class<?> nmsAchievement = ReflectionUtils.getNMSClass("Achievement");
  protected static Class<?> nmsStatistic = ReflectionUtils.getNMSClass("Statistic");
  protected static Class<?> nmsItemStack = ReflectionUtils.getNMSClass("ItemStack");

  protected static Class<?> obcStatistic = ReflectionUtils.getOBCClass("CraftStatistic");
  protected static Class<?> obcItemStack = ReflectionUtils.getOBCClass("inventory.CraftItemStack");

  public MessagePart() {
  }

  public MessagePart(final String text) {
    this.text = text;
  }

  @Override
  protected MessagePart clone() {
    try {
      super.clone();
      MessagePart part = new MessagePart();
      part.color = this.color;
      part.styles = this.styles;
      part.clickEvent = this.clickEvent;
      part.clickEventData = this.clickEventData;
      part.hoverEvent = this.hoverEvent;
      part.hoverEventData = this.hoverEventData;
      part.text = this.text;
      return part;
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Sets the text of this {@link MessagePart}.
   *
   * @param text text to set
   *
   * @return the previous text, nullable
   */
  public String text(String text) {
    String prev = this.text;
    this.text = text;
    return prev;
  }

  /**
   * Sets the {@link ChatColor} of this MessagePart.
   *
   * @param color color to set
   *
   * @return the previous color, nullable
   *
   * @throws IllegalArgumentException thrown if {@code color} is not a color
   */
  public ChatColor color(final ChatColor color) throws IllegalArgumentException {
    if (!color.equals(ChatColor.RESET) && !color.isColor()) {
      throw new IllegalArgumentException(color.name() + " is not a color");
    }

    ChatColor prev = this.color;
    this.color = color;
    return prev;
  }

  /**
   * Applies {@link ChatColor} styles to this MessagePart.
   *
   * @param styles array of styles to apply
   *
   * @return the previous styles
   *
   * @throws IllegalArgumentException thrown if {@code styles} contains a color
   */
  public List<ChatColor> style(ChatColor... styles) throws IllegalArgumentException {
    boolean reset = false;
    for (final ChatColor style : styles) {
      if (style.equals(ChatColor.RESET)) {
        reset = true;
        break;
      }
      if (!style.isFormat()) {
        throw new IllegalArgumentException(style.name() + " is not a style");
      }
    }
    List<ChatColor> prev = new ArrayList<ChatColor>(this.styles);

    if (reset) {
      this.styles.add(ChatColor.RESET);
    } else {
      this.styles.addAll(Arrays.asList(styles));
    }
    return prev;
  }

  /**
   * Opens a file (client-side) on click event.
   *
   * @param path the path of the file to open
   */
  public void file(final String path) {
    onClick("open_file", path);
  }

  /**
   * Opens a URL on click event.
   *
   * @param url url to open
   */
  public void link(final String url) {
    onClick("open_url", url);
  }

  /**
   * Suggests a command on click event. This opens the player's chat box and inserts the given
   * String.
   *
   * @param command command to suggest
   */
  public void suggest(final String command) {
    onClick("suggest_command", command);
  }

  /**
   * Executes a command on click event. It should be noted that the commands executed are run by the
   * player,
   * thus appearing in their recent chat history.
   *
   * @param command command to execute
   */
  public void command(final String command) {
    onClick("run_command", command);
  }

  /**
   * Displays an achievement on hover event.
   *
   * @param name name of the achievement to display
   */
  public void achievementTooltip(final String name) {
    onHover("show_achievement", "achievement." + name);
  }

  /**
   * Displays an {@link Achievement} on hover event.
   *
   * @param which achievement to display
   */
  public void achievementTooltip(final Achievement which) {
    try {
      Object achievement = ReflectionUtils.getMethod(obcStatistic,
                                                    "getNMSAchievement").invoke(null, which);
      achievementTooltip((String) ReflectionUtils.getField(nmsAchievement, "name")
          .get(achievement));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays a statistic on hover event.
   *
   * @param which statistic to display
   */
  public void statisticTooltip(final Statistic which) {
    Statistic.Type type = which.getType();
    if (type != Statistic.Type.UNTYPED) {
      throw new IllegalArgumentException(
          "That statistic requires an additional " + type + " parameter!");
    }
    try {
      Object statistic =
          ReflectionUtils.getMethod(obcStatistic, "getNMSStatistic").invoke(null, which);
      achievementTooltip((String) ReflectionUtils.getField(nmsStatistic, "name")
          .get(statistic));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays a statistic that requires a {@link Material} parameter on hover event.
   *
   * @param statistic statistic to display
   * @param material material to pass to the {@code statistic}
   */
  public void statisticTooltip(final Statistic statistic, Material material) {
    Statistic.Type type = statistic.getType();
    if (type == Statistic.Type.UNTYPED) {
      throw new IllegalArgumentException("That statistic needs no additional parameter!");
    }
    if ((type == Statistic.Type.BLOCK && material.isBlock()) || type == Statistic.Type.ENTITY) {
      throw new IllegalArgumentException(
          "Wrong parameter type for that statistic - needs " + type + "!");
    }
    try {
      Object obcStatistic = ReflectionUtils.getMethod(MessagePart.obcStatistic,
                                                     "getMaterialStatistic")
          .invoke(null, statistic, material);
      achievementTooltip(
          (String) ReflectionUtils.getField(nmsStatistic, "name").get(obcStatistic));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays a statistic that requires a {@link EntityType} parameter on hover event.
   *
   * @param statistic statistic to display
   * @param entity entity to pass to the {@code statistic}
   */
  public void statisticTooltip(final Statistic statistic, EntityType entity) {
    Statistic.Type type = statistic.getType();
    if (type == Statistic.Type.UNTYPED) {
      throw new IllegalArgumentException("That statistic needs no additional parameter!");
    }
    if (type != Statistic.Type.ENTITY) {
      throw new IllegalArgumentException(
          "Wrong parameter type for that statistic - needs " + type + "!");
    }
    try {
      Object obcStatistic = ReflectionUtils.getMethod(MessagePart.obcStatistic,
                                                      "getEntityStatistic")
          .invoke(null, statistic, entity);
      achievementTooltip(
          (String) ReflectionUtils.getField(nmsStatistic, "name").get(obcStatistic));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays an Item written in json on hover event.
   *
   * @param itemJSON item's json to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public void itemTooltip(final String itemJSON) {
    onHover("show_item", itemJSON);
  }

  /**
   * Displays an {@link ItemStack} on hover event.
   *
   * @param itemStack
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public void itemTooltip(final ItemStack itemStack) {
    try {
      Object nmsItem =
          ReflectionUtils.getMethod(obcItemStack, "asNMSCopy", ItemStack.class)
              .invoke(null, itemStack);
      itemTooltip(
          ReflectionUtils.getMethod(nmsItemStack, "save")
              .invoke(nmsItem, nmsTagCompound.newInstance())
              .toString()
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays a tooltip on hover event.
   *
   * @param text text to display
   */
  public void tooltip(final String text) {
    tooltip(text.split("\\n"));
  }

  /**
   * Displays a list of lines on hover event.
   *
   * @param lines lines to display
   */
  public void tooltip(final List<String> lines) {
    tooltip((String[]) lines.toArray());
  }

  /**
   * Displays a list of lines on hover event.
   *
   * @param lines array of String to set
   */
  public void tooltip(final String... lines) {
    if (lines.length == 1) {
      onHover("show_text", lines[0]);
    } else {
      itemTooltip(makeMultilineTooltip(lines));
    }
  }

  /**
   * Creates a click event.
   *
   * @param name name of the event
   * @param data data to pass to the event
   */
  public void onClick(final String name, final String data) {
    Preconditions.checkNotNull(name, "click event name cannot be null.");
    Preconditions.checkNotNull(data, "click event data cannot be null.");
    clickEvent = name;
    clickEventData = data;
  }

  /**
   * Creates a hover event.
   *
   * @param name name of the event
   * @param data data to pass to the event
   */
  public void onHover(final String name, final String data) {
    Preconditions.checkNotNull(name, "hover event name cannot be null.");
    Preconditions.checkNotNull(data, "hover event data cannot be null.");
    hoverEvent = name;
    hoverEventData = data;
  }

  protected String makeMultilineTooltip(final String[] lines) {
    StringWriter string = new StringWriter();
    JsonWriter json = new JsonWriter(string);
    try {
      json.beginObject().name("id").value(1);
      json.name("tag").beginObject().name("display").beginObject();
      json.name("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
      json.name("Lore").beginArray();
      for (int i = 1; i < lines.length; i++) {
        final String line = lines[i];
        json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
      }
      json.endArray().endObject().endObject().endObject();
      json.close();
    } catch (Exception e) {
      throw new RuntimeException("invalid tooltip", e);
    }
    return string.toString();
  }

  JsonWriter writeJson(JsonWriter json) {
    try {
      json.beginObject().name("text").value(text);
      if (color != null) {
        json.name("color").value(color.name().toLowerCase());
      }
      for (final ChatColor style : styles) {
        String styleName;
        switch (style) {
          case MAGIC:
            styleName = "obfuscated";
            break;
          case UNDERLINE:
            styleName = "underlined";
            break;
          default:
            styleName = style.name().toLowerCase();
            break;
        }
        json.name(styleName).value(true);
      }
      if (clickEvent != null && clickEventData != null) {
        json.name("clickEvent")
            .beginObject()
            .name("action").value(clickEvent)
            .name("value").value(clickEventData)
            .endObject();
      }
      if (hoverEvent != null && hoverEventData != null) {
        json.name("hoverEvent")
            .beginObject()
            .name("action").value(hoverEvent)
            .name("value").value(hoverEventData)
            .endObject();
      }
      return json.endObject();
    } catch (Exception e) {
      e.printStackTrace();
      return json;
    }
  }

  /**
   * Converts this {@link MessagePart} to a JSON String.
   *
   * @return JSON of this MessagePart
   */
  public String toJSONString() {
    StringWriter json = new StringWriter();
    writeJson(new JsonWriter(json));
    return json.toString();
  }

  /**
   * Checks whether this {@link MessagePart} has text.
   *
   * @return whether this MessagePart has text
   */
  public boolean hasText() {
    return !text.isEmpty();
  }

  /**
   * Sets this {@link MessagePart}'s text.
   *
   * @return this MessagePart's text
   */
  public String getText() {
    return text;
  }

  /**
   * Gets this {@link MessagePart}'s text.
   *
   * @param text text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Gets this {@link MessagePart}'s hover event name.
   *
   * @return hover event name, nullable
   */
  public String getHoverEvent() {
    return hoverEvent;
  }

  /**
   * Sets this {@link MessagePart}'s hover event name.
   *
   * @param hoverEvent name of the hover event to set
   */
  public void setHoverEvent(String hoverEvent) {
    this.hoverEvent = hoverEvent;
  }

  /**
   * Gets this {@link MessagePart}'s hover event data.
   *
   * @return hover event data, nullable
   */
  public String getHoverEventData() {
    return hoverEventData;
  }

  /**
   * Sets this {@link MessagePart}'s hover event data.
   *
   * @param hoverEventData hover event data to set
   */
  public void setHoverEventData(String hoverEventData) {
    this.hoverEventData = hoverEventData;
  }

  /**
   * Gets this {@link MessagePart}'s click event name.
   *
   * @return click event name, nullable
   */
  public String getClickEvent() {
    return clickEvent;
  }

  /**
   * Sets this {@link MessagePart}'s click event name.
   *
   * @param clickEvent click event name to set
   */
  public void setClickEvent(String clickEvent) {
    this.clickEvent = clickEvent;
  }

  /**
   * Gets this {@link MessagePart}'s hover event data.
   *
   * @return click event data, nullable
   */
  public String getClickEventData() {
    return clickEventData;
  }

  /**
   * Sets this {@link MessagePart}'s click event data.
   *
   * @param clickEventData click event data to set
   */
  public void setClickEventData(String clickEventData) {
    this.clickEventData = clickEventData;
  }

  /**
   * Gets this {@link MessagePart}'s chat styles.
   *
   * @return
   */
  public ArrayList<ChatColor> getStyles() {
    return styles;
  }

  /**
   * Sets this {@link MessagePart}'s chat styles.
   *
   * @param styles
   */
  public void setStyles(ArrayList<ChatColor> styles) {
    this.styles = styles;
  }

  /**
   * Gets this {@link MessagePart}'s chat color.
   *
   * @return
   */
  public ChatColor getColor() {
    return color;
  }

  /**
   * Sets this {@link MessagePart}'s chat color.
   *
   * @param color
   */
  public void setColor(ChatColor color) {
    this.color = color;
  }
}
