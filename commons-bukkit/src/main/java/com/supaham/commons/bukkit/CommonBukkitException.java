package com.supaham.commons.bukkit;

import com.supaham.commons.exceptions.CommonException;
import com.supaham.commons.bukkit.language.Message;

import javax.annotation.Nonnull;

/**
 * Represents a commons bukkit library exception that can be thrown at any time.
 *
 * @since 0.1
 */
public class CommonBukkitException extends CommonException {

  public CommonBukkitException(String message) {
    super(message);
  }

  public CommonBukkitException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommonBukkitException(Throwable cause) {
    super(cause);
  }

  public CommonBukkitException(@Nonnull Message message, Object... args) {
    super(message.getParsedMessage(args));
  }
}
