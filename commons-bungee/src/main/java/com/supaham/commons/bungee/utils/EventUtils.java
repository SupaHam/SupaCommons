package com.supaham.commons.bungee.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Event;

/**
 * Utility methods for working with {@link Event} instances. This class contains methods such as
 * {@link ##callEvent(Object)} and more.
 *
 * @since 0.3.6
 */
public class EventUtils {

  /**
   * Helper method for calling an {@link Event} and returning it.
   *
   * @param event event to call
   * @param <T> event type
   *
   * @return same exact {@code event} instance after it is called
   */
  public static <T extends Event> T callEvent(T event) {
    ProxyServer.getInstance().getPluginManager().callEvent(event);
    return event;
  }
}
