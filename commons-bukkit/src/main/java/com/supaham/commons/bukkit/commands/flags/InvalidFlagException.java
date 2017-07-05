package com.supaham.commons.bukkit.commands.flags;

public class InvalidFlagException extends RuntimeException {

  private final String string;

  public InvalidFlagException(String string) {
    this.string = string;
  }

  public String getString() {
    return string;
  }
}
