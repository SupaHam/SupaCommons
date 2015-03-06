package com.supaham.commons.bukkit;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

import pluginbase.messages.LocalizablePlugin;

/**
 * Simple interface that represents a common plugin. This is used from within the whole commons library.
 *
 * @since 0.1
 */
public interface CommonPlugin extends LocalizablePlugin, Plugin {
  
  <T extends Listener> T registerEvents(@Nonnull T listener);
}
