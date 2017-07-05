package com.supaham.commons.bukkit.commands.flags;

import com.google.common.base.Preconditions;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class Flag {

  private final char character;
  private final String name;
  private final boolean optional;
  private final boolean consumingValue;

  public Flag(char character, @Nonnull String name, boolean optional, boolean consumingValue) {
    this.character = character;
    this.name = Preconditions.checkNotNull(name, "name");
    this.optional = optional;
    this.consumingValue = consumingValue;
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!(obj instanceof Flag)) {
      return false;
    }
    Flag other = (Flag) obj;
    return this.name.equals(other.name)
           && this.character == other.character
           && this.optional == other.optional
           && this.consumingValue == other.consumingValue;
  }

  private Integer hashCode;
  @Override public int hashCode() {
    if (hashCode == null) {
      hashCode = Objects.hash(character, name, optional, consumingValue);
    }
    return hashCode;
  }

  public char getCharacter() {
    return character;
  }

  public String getName() {
    return name;
  }

  public boolean isConsumingValue() {
    return consumingValue;
  }

  public boolean isOptional() {
    return optional;
  }
}
