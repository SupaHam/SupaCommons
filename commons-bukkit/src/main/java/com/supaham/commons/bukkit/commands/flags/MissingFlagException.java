package com.supaham.commons.bukkit.commands.flags;

public class MissingFlagException extends RuntimeException {

  private final Flag flag;

  public MissingFlagException(Flag flag) {
    this.flag = flag;
  }

  public Flag getFlag() {
    return flag;
  }
}
