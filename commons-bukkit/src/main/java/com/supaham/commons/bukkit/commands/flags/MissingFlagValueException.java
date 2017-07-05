package com.supaham.commons.bukkit.commands.flags;

public class MissingFlagValueException extends RuntimeException {

  private final Flag flag;
  private final String flagString;

  public MissingFlagValueException(Flag flag, String flagString) {
    this.flag = flag;
    this.flagString = flagString;
  }

  public Flag getFlag() {
    return flag;
  }

  public String getFlagString() {
    return flagString;
  }
}
