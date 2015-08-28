package com.supaham.commons.bukkit.language;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a message ({@link String}) represented by a node ({@link String}) that supports
 * multiple {@link Locale}s.
 *
 * @since 0.1
 */
public class Message {

  public static final Locale DEFAULT_LOCALE = Locale.getDefault();
  private final MessageManager manager;
  private final String node;
  private final Map<Locale, String> messages = new HashMap<>();

  public Message(@Nonnull MessageManager manager, @Nonnull String node, @Nonnull String message) {
    this.manager = Preconditions.checkNotNull(manager, "manager cannot be null.");
    this.node = Preconditions.checkNotNull(node, "node cannot be null.");
    Preconditions.checkNotNull(message, "message cannot be null.");
    this.messages.put(DEFAULT_LOCALE, message);
  }

  /**
   * Checks whether a {@link Locale} is supported by this {@link Message}.
   *
   * @param locale local to check.
   *
   * @return whether the {@code locale} is supported.
   */
  public boolean supports(@Nonnull Locale locale) {
    Preconditions.checkNotNull(locale, "locale cannot be null.");
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
  public String getMessage(@Nonnull Locale locale) {
    Preconditions.checkNotNull(locale, "locale cannot be null.");
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
  public String addLocale(@Nonnull Locale locale, @Nonnull String message) {
    return this.messages.put(locale, message);
  }

  /**
   * Sends the {@link #getMessage()} to a {@link CommandSender}.
   *
   * @param sender command sender to send this {@link Message} to
   * @param args the arguments to pass to this message
   */
  public void send(@Nonnull CommandSender sender, Object... args) {
    Preconditions.checkNotNull(sender, "sender cannot be null.");
    send(sender, DEFAULT_LOCALE, args);
  }

  /**
   * Sends a message belonging to a {@link Locale} to a {@link CommandSender}.
   *
   * @param sender sender to send this {@link Message} to
   * @param locals locals to provide for the placeholder functions
   * @param args the arguments to pass to this message
   */
  public void send(@Nonnull CommandSender sender, @Nullable Map<Object, Object> locals,
                   Object... args) {
    Preconditions.checkNotNull(sender, "sender cannot be null.");
    send(sender, DEFAULT_LOCALE, locals, args);
  }

  /**
   * Sends a message belonging to a {@link Locale} to a {@link CommandSender}.
   *
   * @param sender sender to send this {@link Message} to
   * @param locale locale to get message for
   * @param args the arguments to pass to this message
   */
  public void send(@Nonnull CommandSender sender, Locale locale, Object... args) {
    Preconditions.checkNotNull(sender, "sender cannot be null.");
    send(sender, locale, null, args);
  }

  /**
   * Sends a message belonging to a {@link Locale} to a {@link CommandSender}.
   *
   * @param sender sender to send this {@link Message} to
   * @param locale locale to get message for
   * @param args the arguments to pass to this message
   */
  public void send(@Nonnull CommandSender sender, Locale locale,
                   @Nullable Map<Object, Object> locals, Object... args) {
    Preconditions.checkNotNull(sender, "sender cannot be null.");
    sender.sendMessage(getParsedMessage(locale, locals, args));
  }

  public void broadcast(Object... args) {
    broadcast(DEFAULT_LOCALE, args);
  }

  public void broadcast(@Nonnull Locale locale, Object... args) {
    Preconditions.checkNotNull(locale, "locale cannot be null.");
    broadcast(locale, null, args);
  }

  public void broadcast(@Nullable Map<Object, Object> locals, Object... args) {
    broadcast(DEFAULT_LOCALE, locals, args);
  }

  public void broadcast(@Nonnull Locale locale, @Nullable Map<Object, Object> locals,
                        Object... args) {
    Preconditions.checkNotNull(locale, "locale cannot be null.");
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
  public void broadcast(@Nonnull Collection<CommandSender> receivers, Object... args) {
    Preconditions.checkNotNull(receivers, "receivers cannot be null.");
    broadcast(receivers, null, args);
  }

  /**
   * Sends the {@link #getMessage()} to a {@link Collection} of {@link CommandSender}.
   *
   * @param receivers command receivers to send this {@link Message} to.
   * @param args the arguments to pass to this message.
   */
  public void broadcast(@Nonnull Collection<CommandSender> receivers,
                        @Nullable Map<Object, Object> locals, Object... args) {
    Preconditions.checkNotNull(receivers, "receivers cannot be null.");
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
    Preconditions.checkNotNull(message, "Could not find message for locale "
                                        + locale.toLanguageTag());
    return _getParsedMessage(message, locals, args);
  }

  protected String _getParsedMessage(@Nonnull String message, @Nullable Map<Object, Object> locals,
                                     Object... args) {
    return manager.parseMessage(String.format(message, args), locals);
  }

  public String getNode() {
    return node;
  }
}
