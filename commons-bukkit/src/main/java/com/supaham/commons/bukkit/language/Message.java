package com.supaham.commons.bukkit.language;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a message ({@link String}) represented by a node ({@link String}) that supports
 * multiple {@link Locale}s.
 *
 * @since 0.1
 */
public class Message {

  public static final Locale DEFAULT_LOCALE = Locale.getDefault();
  private final MessageManager manager;
  @Getter
  private final String node;
  private final Map<Locale, String> messages = new HashMap<>();

  public Message(@NonNull MessageManager manager, @NonNull String node, @NonNull String message) {
    this.manager = manager;
    this.node = node;
    this.messages.put(DEFAULT_LOCALE, message);
  }

  /**
   * Checks whether a {@link Locale} is supported by this {@link Message}.
   *
   * @param locale local to check.
   *
   * @return whether the {@code locale} is supported.
   */
  public boolean supports(@NonNull Locale locale) {
    return this.messages.containsKey(locale);
  }

  /**
   * Gets the message from the default locale ({@link Locale#getDefault()}).
   *
   * @return the message, not null.
   */
  public String getMessage() {
    return this.messages.get(DEFAULT_LOCALE);
  }

  /**
   * Gets the message belonging to a {@link Locale}.
   *
   * @param locale locale to get message for.
   *
   * @return message belonging to {@code locale}, nullable.
   */
  public String getMessage(@NonNull Locale locale) {
    return this.messages.get(locale);
  }

  /**
   * Adds a new {@link Locale} to this {@link Message}.
   *
   * @param locale locale to add
   * @param message message in the {@code locale}
   *
   * @return the old message belonging to the {@code locale}
   */
  public String addLocale(@NonNull Locale locale, @NonNull String message) {
    return this.messages.put(locale, message);
  }

  /**
   * Sends the {@link #getMessage()} to a {@link CommandSender}.
   *
   * @param sender command sender to send this {@link Message} to
   * @param args the arguments to pass to this message
   */
  public void send(@NonNull CommandSender sender, Object... args) {
    send(sender, DEFAULT_LOCALE, args);
  }

  /**
   * Sends a message belonging to a {@link Locale} to a {@link CommandSender}.
   *
   * @param sender sender to send this {@link Message} to
   * @param locals locals to provide for the placeholder functions
   * @param args the arguments to pass to this message
   */
  public void send(@NonNull CommandSender sender, @Nullable Map<Object, Object> locals,
                   Object... args) {
    send(sender, DEFAULT_LOCALE, locals, args);
  }

  /**
   * Sends a message belonging to a {@link Locale} to a {@link CommandSender}.
   *
   * @param sender sender to send this {@link Message} to
   * @param locale locale to get message for
   * @param args the arguments to pass to this message
   */
  public void send(@NonNull CommandSender sender, Locale locale, Object... args) {
    send(sender, locale, null, args);
  }

  /**
   * Sends a message belonging to a {@link Locale} to a {@link CommandSender}.
   *
   * @param sender sender to send this {@link Message} to
   * @param locale locale to get message for
   * @param args the arguments to pass to this message
   */
  public void send(@NonNull CommandSender sender, Locale locale,
                   @Nullable Map<Object, Object> locals, Object... args) {
    sender.sendMessage(getParsedMessage(locale, locals, args));
  }

  public void broadcast(Object... args) {
    broadcast(DEFAULT_LOCALE, args);
  }

  public void broadcast(@NonNull Locale locale, Object... args) {
    broadcast(locale, null, args);
  }

  public void broadcast(@Nullable Map<Object, Object> locals, Object... args) {
    broadcast(DEFAULT_LOCALE, locals, args);
  }

  public void broadcast(@NonNull Locale locale, @Nullable Map<Object, Object> locals,
                        Object... args) {
    ArrayList<CommandSender> receivers = new ArrayList<CommandSender>(Bukkit.getOnlinePlayers());
    receivers.add(Bukkit.getConsoleSender());
    broadcast(receivers, locale, locals, args);
  }

  /**
   * Sends the {@link #getMessage()} to a {@link Collection} of {@link CommandSender}.
   *
   * @param receivers command receivers to send this {@link Message} to.
   * @param args the arguments to pass to this message.
   */
  public void broadcast(@NonNull Collection<CommandSender> receivers, Object... args) {
    broadcast(receivers, null, args);
  }

  /**
   * Sends the {@link #getMessage()} to a {@link Collection} of {@link CommandSender}.
   *
   * @param receivers command receivers to send this {@link Message} to.
   * @param args the arguments to pass to this message.
   */
  public void broadcast(@NonNull Collection<CommandSender> receivers,
                        @Nullable Map<Object, Object> locals, Object... args) {
    broadcast(receivers, DEFAULT_LOCALE, locals, args);
  }

  /**
   * Sends a message belonging to a {@link Locale} to a {@link Collection} of {@link
   * CommandSender}.
   *
   * @param receivers receivers to send this {@link Message} to.
   * @param locale locale to get message for.
   * @param args the arguments to pass to this message.
   */
  public void broadcast(@Nonnull Collection<CommandSender> receivers, Locale locale,
                        @Nullable Map<Object, Object> locals, Object... args) {
    String parsedMessage = getParsedMessage(locale, locals, args);
    for (CommandSender sender : receivers) {
      sender.sendMessage(parsedMessage);
    }
  }

  public String getParsedMessage(Object... args) {
    return getParsedMessage((Map<Object, Object>) null, args);
  }

  public String getParsedMessage(@Nullable Map<Object, Object> locals, Object... args) {
    return getParsedMessage(DEFAULT_LOCALE, locals, args);
  }

  public String getParsedMessage(Locale locale, Object... args) {
    return getParsedMessage(locale, null, args);
  }

  public String getParsedMessage(Locale locale, @Nullable Map<Object, Object> locals,
                                 Object... args) {
    if (locale == null) {
      locale = DEFAULT_LOCALE;
    }
    String message = getMessage(locale);
    Validate.notNull(message, "Could not find message for locale " + locale.toLanguageTag());
    return _getParsedMessage(message, locals, args);
  }

  protected String _getParsedMessage(@Nonnull String message, @Nullable Map<Object, Object> locals,
                                     Object... args) {
    return manager.parseMessage(String.format(message, args), locals);
  }
}
