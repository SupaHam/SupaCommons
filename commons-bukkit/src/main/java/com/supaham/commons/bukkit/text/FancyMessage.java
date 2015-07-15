package com.supaham.commons.bukkit.text;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonWriter;

import com.supaham.commons.Enums;
import com.supaham.commons.bukkit.Colors;
import com.supaham.commons.bukkit.utils.ChatColorUtils;
import com.supaham.commons.bukkit.utils.ReflectionUtils;
import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import org.apache.commons.lang.Validate;
import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Represents a Minecraft chat message that can be fancified with hover & click events, colors,
 * styles and more.
 * <p/>
 * This class uses reflection for some methods such as achievements (Objects, not names) and
 * sending the packet to a player.
 *
 * @since 0.2.4
 */
public class FancyMessage {

  private final List<MessagePart> messageParts;
  private String jsonString;
  private boolean dirty;

  public static FancyMessage of(JsonElement serialized) {
    if (serialized.isJsonPrimitive()) {
      return new FancyMessage(serialized.getAsString());
    } else if (!serialized.isJsonObject()) {
      if (serialized.isJsonArray()) {
        JsonArray array = serialized.getAsJsonArray();
        FancyMessage fancyMessage = null;
        for (JsonElement jsonElement : array) {
          FancyMessage curMessage = of(jsonElement);
          if (fancyMessage == null) {
            fancyMessage = curMessage;
          } else {
            fancyMessage.append(curMessage);
          }
        }
        return fancyMessage;
      } else {
        throw new JsonParseException("Invalid JSON chat syntax: " + serialized.toString());
      }
    } else {
      JsonObject jsonObject = serialized.getAsJsonObject();
      FancyMessage result = new FancyMessage();
      if (jsonObject.has("text")) {
        result = new FancyMessage(jsonObject.get("text").getAsString());
      } else if (jsonObject.has("translate")) {
        // TODO
      } else if (jsonObject.has("score")) {
        // TODO
      } else {
        if (!jsonObject.has("selector")) {
          throw new JsonParseException("Invalid JSON chat syntax: " + serialized.toString());
        }
        // TODO
      }

      if (jsonObject.has("extra")) {
        JsonArray jsonarray2 = jsonObject.getAsJsonArray("extra");

        if (jsonarray2.size() <= 0) {
          throw new JsonParseException("Unexpected empty array of components.");
        }

        for (int j = 0; j < jsonarray2.size(); ++j) {
          result.append(of(jsonarray2.get(j)));
        }
      }

      if (jsonObject.has("color")) {
        result.color(Enums.findFuzzyByValue(ChatColor.class,
                                            jsonObject.get("color").getAsString()));
      }
      for (ChatColor color : ChatColorUtils.STYLES) {
        if (jsonObject.has(color.name().toLowerCase())) {
          result.style(color);
        } else if (color == ChatColor.MAGIC && jsonObject.has("obfuscated")) {
          result.style(ChatColor.MAGIC);
        } else if (color == ChatColor.UNDERLINE && jsonObject.has("underlined")) {
          result.style(ChatColor.UNDERLINE);
        }
      }

      if (jsonObject.has("clickEvent")) {
        JsonObject clickEvent = jsonObject.getAsJsonObject("clickEvent");
        if (!clickEvent.has("action") || !clickEvent.has("value")) {
          throw new JsonParseException("clickEvent must have an action and value: " + serialized);
        }
        result.onClick(clickEvent.get("action").getAsString(),
                       clickEvent.get("value").getAsString());
      } else if (jsonObject.has("hoverEvent")) {
        JsonObject hoverEvent = jsonObject.getAsJsonObject("hoverEvent");
        if (!hoverEvent.has("action") || !hoverEvent.has("value")) {
          throw new JsonParseException("hoverEvent must have an action and value: " + serialized);
        }
        result.onHover(hoverEvent.get("action").getAsString(),
                       hoverEvent.get("value").getAsString());
      }

      return result;
    }
  }

  public FancyMessage(@Nonnull final Object object) {
    this(Preconditions.checkNotNull(object, "object cannot be null.").toString());
  }

  public FancyMessage(final String firstPartText) {
    messageParts = new ArrayList<>();
    messageParts.add(new MessagePart(firstPartText));
    jsonString = null;
    dirty = true; // true to speed up append(MessagePart)
  }

  public FancyMessage() {
    messageParts = new ArrayList<>();
    messageParts.add(new MessagePart());
    jsonString = null;
    dirty = false;
  }

  /**
   * Sets the text of the current {@link MessagePart}.
   *
   * @param text text to set
   *
   * @return this instance of FancyMessage, for chaining.
   *
   * @throws IllegalStateException thrown if the text is already set for the message part.
   * @see #append(String)
   */
  public FancyMessage text(Colors text) throws IllegalStateException {
    return text(Preconditions.checkNotNull(text, "text cannot be null.").toString());
  }
  
  /**
   * Sets the text of the current {@link MessagePart}.
   *
   * @param text text to set
   *
   * @return this instance of FancyMessage, for chaining.
   *
   * @throws IllegalStateException thrown if the text is already set for the message part.
   * @see #append(String)
   */
  public FancyMessage text(String text) throws IllegalStateException {
    if (hasText()) {
      throw new IllegalStateException("text for this message part is already set");
    }
    latest().text(text);
    dirty = true;
    return this;
  }

  /**
   * Safely appends text to this FancyMessage using {@link SafeFancyMessage#from(String)}. If the 
   * text is already set for this MessagePart it will clone the {@link MessagePart} to keep styles 
   * and colors.
   * <p/>
   * <b>Note:</b> this method calls {@link ChatColor#translateAlternateColorCodes(char, String)} on 
   * the given string, where the {@code char} is &, replacing all ampersands with &sect;.
   *
   * @param text text to append
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage safeAppend(String text) {
    append(SafeFancyMessage.from(ChatColor.translateAlternateColorCodes('&', text)));
    return this;
  }

  /**
   * Appends text to this FancyMessage. If the text is already set for this MessagePart it will
   * clone the {@link MessagePart} to keep styles and colors.
   *
   * @param text text to append
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage append(String text) {
    MessagePart latest = latest();
    if (latest.hasText()) {
      latest = latest.clone();
      messageParts.add(latest);
    }
    latest.text = text;
    dirty = true;
    return this;
  }

  public FancyMessage append(MessagePart part) {
    this.messageParts.add(part);
    dirty = true;
    return this;
  }

  public FancyMessage append(FancyMessage fancyMessage) {
    this.messageParts.addAll(fancyMessage.messageParts);
    return this;
  }

  public FancyMessage add(int index, MessagePart part) throws IllegalArgumentException {
    Validate.isTrue(index < messageParts.size());
    this.messageParts.add(index, part);
    dirty = true;
    return this;
  }

  /**
   * Sets the {@link ChatColor} of this MessagePart.
   *
   * @param color color to set
   *
   * @return this instance of FancyMessage, for chaining.
   *
   * @throws IllegalArgumentException thrown if {@code color} is not a color
   */
  public FancyMessage color(final ChatColor color) throws IllegalArgumentException {
    latest().color(color);
    dirty = true;
    return this;
  }

  /**
   * Applies {@link ChatColor} styles to this MessagePart.
   *
   * @param styles array of styles to apply
   *
   * @return this instance of FancyMessage, for chaining.
   *
   * @throws IllegalArgumentException thrown if {@code styles} contains a color
   */
  public FancyMessage style(ChatColor... styles) throws IllegalArgumentException {
    latest().style(styles);
    dirty = true;
    return this;
  }

  /**
   * Opens a file (client-side) on click event.
   *
   * @param path the path of the file to open
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage file(final String path) {
    latest().file(path);
    dirty = true;
    return this;
  }

  /**
   * Opens a URL on click event.
   *
   * @param url url to open
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage link(final String url) {
    latest().link(url);
    dirty = true;
    return this;
  }

  /**
   * Suggests a command on click event. This opens the player's chat box and inserts the given
   * String.
   *
   * @param command command to suggest
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage suggest(final String command) {
    latest().suggest(command);
    dirty = true;
    return this;
  }

  /**
   * Executes a command on click event. It should be noted that the commands executed are run by
   * the
   * player,
   * thus appearing in their recent chat history.
   *
   * @param command command to execute
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage command(final String command) {
    latest().command(command);
    dirty = true;
    return this;
  }

  /**
   * Displays an achievement on hover event.
   *
   * @param name name of the achievement to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage achievementTooltip(final String name) {
    latest().achievementTooltip("achievement." + name);
    dirty = true;
    return this;
  }

  /**
   * Displays an {@link Achievement} on hover event.
   *
   * @param which achievement to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage achievementTooltip(final Achievement which) {
    latest().achievementTooltip(which);
    dirty = true;
    return this;
  }

  /**
   * Displays a statistic on hover event.
   *
   * @param which statistic to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage statisticTooltip(final Statistic which) {
    latest().statisticTooltip(which);
    dirty = true;
    return this;
  }

  /**
   * Displays a statistic that requires a {@link Material} parameter on hover event.
   *
   * @param statistic statistic to display
   * @param material material to pass to the {@code statistic}
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage statisticTooltip(final Statistic statistic, Material material) {
    latest().statisticTooltip(statistic, material);
    dirty = true;
    return this;
  }

  /**
   * Displays a statistic that requires a {@link EntityType} parameter on hover event.
   *
   * @param statistic statistic to display
   * @param entity entity to pass to the {@code statistic}
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage statisticTooltip(final Statistic statistic, EntityType entity) {
    latest().statisticTooltip(statistic, entity);
    dirty = true;
    return this;
  }

  /**
   * Displays an Item written in json on hover event.
   *
   * @param itemJSON item's json to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage itemTooltip(final String itemJSON) {
    latest().itemTooltip(itemJSON);
    dirty = true;
    return this;
  }

  /**
   * Displays an {@link ItemStack} on hover event.
   *
   * @param itemStack
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage itemTooltip(final ItemStack itemStack) {
    latest().itemTooltip(itemStack);
    dirty = true;
    return this;
  }

  /**
   * Displays a tooltip on hover event.
   *
   * @param text text to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage tooltip(final Colors text) {
    return tooltip(Preconditions.checkNotNull(text, "text cannot be null.").toString());
  }
  
  /**
   * Displays a tooltip on hover event.
   *
   * @param text text to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage tooltip(final String text) {
    latest().tooltip(text);
    dirty = true;
    return this;
  }

  /**
   * Displays a list of lines on hover event.
   *
   * @param lines lines to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage tooltip(final List<String> lines) {
    latest().tooltip(lines);
    dirty = true;
    return this;
  }
  /**
   * Displays a list of lines on hover event.
   *
   * @param lines lines to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage tooltip(final Colors... lines) {
    for (Colors line : Preconditions.checkNotNull(lines, "lines cannot be null.")) {
      tooltip(Preconditions.checkNotNull(line, "line element cannot be null."));
    }
    return this;
  }

  /**
   * Displays a list of lines on hover event.
   *
   * @param lines lines to display
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage tooltip(final String... lines) {
    latest().tooltip(lines);
    dirty = true;
    return this;
  }

  /**
   * Creates a new MessagePart and appends an {@link Object} to it.
   *
   * @param obj object to append (toString() is called)
   *
   * @return this instance of FancyMessage, for chaining.
   *
   * @throws IllegalStateException thrown if the latest MessagePart doesn't have text
   */
  public FancyMessage then(final Object obj) throws IllegalStateException {
    if (!latest().hasText()) {
      return this;
    }
    messageParts.add(new MessagePart(obj.toString()));
    dirty = true;
    return this;
  }

  /**
   * Creates a new MessagePart.
   *
   * @return this instance of FancyMessage, for chaining.
   */
  public FancyMessage then() {
    if (!latest().hasText()) {
      return this;
    }
    messageParts.add(new MessagePart());
    dirty = true;
    return this;
  }

  /**
   * Creates a click event.
   *
   * @param name name of the event
   * @param data data to pass to the event
   */
  public void onClick(final String name, final String data) {
    latest().onClick(name, data);
    dirty = true;
  }

  /**
   * Creates a hover event.
   *
   * @param name name of the event
   * @param data data to pass to the event
   */
  public void onHover(final String name, final String data) {
    latest().onHover(name, data);
    dirty = true;
  }

  /**
   * Checks whether the current MessagePart has text.
   *
   * @return whether the current MessagePart has text
   */
  public boolean hasText() {
    return latest().hasText();
  }

  /**
   * Converts this FancyMessage to a JSON String.
   *
   * @return JSON of this fancy message
   */
  public String toJSONString() {
    if (!dirty && jsonString != null) {
      return jsonString;
    }
    StringWriter string = new StringWriter();
    JsonWriter json = new JsonWriter(string);
    try {
      if (messageParts.size() == 1) {
        latest().writeJson(json);
      } else {
        json.beginObject().name("text").value("").name("extra").beginArray();
        for (final MessagePart part : messageParts) {
          part.writeJson(json);
        }
        json.endArray().endObject();
        json.close();
      }
    } catch (Exception e) {
      throw new RuntimeException("invalid message", e);
    }
    jsonString = string.toString();
    dirty = false;
    return jsonString;
  }

  /**
   * Returns a human readable string of this fancy message.
   *
   * @return readable string
   */
  public String toReadableString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (MessagePart messagePart : this.messageParts) {
      if(messagePart.getColor() != null) {
        stringBuilder.append(messagePart.getColor());
      }
      for (ChatColor color : messagePart.getStyles()) {
        stringBuilder.append(color);
      }
      stringBuilder.append(messagePart.getText());
    }
    return stringBuilder.toString();
  }

  private MessagePart latest() {
    return messageParts.get(messageParts.size() - 1);
  }

  public List<MessagePart> getMessageParts() {
    return Collections.unmodifiableList(messageParts);
  }

  public MessagePart removePart(int index) {
    MessagePart removed = this.messageParts.remove(index);
    if (removed != null) {
      this.dirty = true;
    }
    return removed;
  }

  protected static Class<?> nmsIChatBaseComponent = PackageType.MINECRAFT_SERVER
      .getClassSafe("IChatBaseComponent");
  protected static Class<?> nmsPacketPlayOutChat = PackageType.MINECRAFT_SERVER
      .getClassSafe("PacketPlayOutChat");
  protected static Class<?> nmsChatSerializer;


  static {
    if (ReflectionUtils.isServer18()) {
      nmsChatSerializer = PackageType.MINECRAFT_SERVER
          .getClassSafe("IChatBaseComponent$ChatSerializer");
    } else {
      nmsChatSerializer = PackageType.MINECRAFT_SERVER.getClassSafe("ChatSerializer");
    }
  }

  /**
   * Sends this FancyMessage to a {@link CommandSender} by calling {@link #toReadableString()} for
   * {@link CommandSender#sendMessage(String)}.
   *
   * @param commandSender command sender to send message to
   *
   * @see #send(Iterable)
   */
  public void send(@Nonnull CommandSender commandSender) {
    Preconditions.checkNotNull(commandSender, "command sender cannot be null.");
    if (commandSender instanceof Player) {
      send(((Player) commandSender));
    } else {
      commandSender.sendMessage(toReadableString());
    }
  }

  /**
   * Sends this FancyMessage to a {@link Player}.
   *
   * @param player player to send message to
   *
   * @see #send(Iterable)
   */
  public void send(@Nonnull Player player) {
    Preconditions.checkNotNull(player, "player cannot be null.");
    try {
      Object packet = nmsPacketPlayOutChat.getConstructor(nmsIChatBaseComponent)
          .newInstance(getNMSChatObject());
      ReflectionUtils.sendPacket(player, packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends this FancyMessage to a {@link Player}s.
   *
   * @param players players to send this message to
   *
   * @see #send(Player)
   */
  public void send(@Nonnull final Iterable<Player> players) {
    Preconditions.checkNotNull(players, "players cannot be null.");
    try {
      Object packet = nmsPacketPlayOutChat.getConstructor(nmsIChatBaseComponent)
          .newInstance(getNMSChatObject());
      for (final Player player : players) {
        ReflectionUtils.sendPacket(player, packet);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Object getNMSChatObject() {
    try {
      return ReflectionUtils.getMethod(nmsChatSerializer, "a", String.class)
          .invoke(null, toJSONString());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
