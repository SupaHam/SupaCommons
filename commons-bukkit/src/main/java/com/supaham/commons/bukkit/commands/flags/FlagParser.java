package com.supaham.commons.bukkit.commands.flags;

import com.google.common.base.Preconditions;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlagParser {

  public static final String SHORTHAND_PREFIX = "-";
  public static final String LONGHAND_PREFIX = "--";
  private final String shorthandPrefix;
  private final String longhandPrefix;

  private Set<Flag> flags = new HashSet<>();

  public FlagParser() {
    this(SHORTHAND_PREFIX, LONGHAND_PREFIX);
  }

  public FlagParser(String shorthandPrefix, String longhandPrefix) {
    this.shorthandPrefix = shorthandPrefix;
    this.longhandPrefix = longhandPrefix;
  }

  public boolean add(@Nonnull Flag flag) {
    Preconditions.checkNotNull(flag, "flag");
    return flags.add(flag);
  }

  public boolean remove(@Nonnull Flag flag) {
    Preconditions.checkNotNull(flag, "flag");
    return flags.remove(flag);
  }

  @Nonnull
  public FlagParseResult parse(@Nonnull String[] args) {
    Preconditions.checkNotNull(args, "args");
    HashMap<Flag, String> parsedFlags = new HashMap<>();
    int i = 0;
    for (; i < args.length; i++) {
      String arg = args[i];
      if (arg.startsWith(this.shorthandPrefix)) {
        Flag flag;
        if (arg.startsWith(this.longhandPrefix)) {
          flag = getByName(arg.substring(2));
        } else {
          // shorthands only accept one char flags.
          if (arg.length() > 2) {
            throw new InvalidFlagException(arg);
          }
          flag = getByChar(arg.charAt(1));
        }
        if (flag == null) {
          throw new InvalidFlagException(arg);
        }
        String value = null;
        // Handle flag value consumption
        if (flag.isConsumingValue()) {
          if (args.length <= i + 1) {
            throw new MissingFlagValueException(flag, arg);
          }
          value = args[++i];
        }
        parsedFlags.put(flag, value);
      } else {
        break; // flags go before arguments.
      }
    }

    // Ensure all flags non-optional flags have been parsed
    for (Flag flag : this.flags) {
      if (!flag.isOptional() && !parsedFlags.containsKey(flag)) {
        throw new MissingFlagException(flag);
      }
    }
    String[] newArgs = new String[args.length - i];
    if (args.length - i > 0) {
      System.arraycopy(args, i, newArgs, 0, args.length - i);
    }
    return new FlagParseResult(parsedFlags, newArgs);
  }

  @Nullable
  public FlagParseResult parseFor(CommandSender sender, String[] args) {
    try {
      return parse(args);
    } catch (InvalidFlagException e) {
      sender.sendMessage("Unknown flag " + e.getString());
    } catch (MissingFlagException e) {
      sender.sendMessage("Expecting flag '" + e.getFlag().getCharacter() + "'");
    } catch (MissingFlagValueException e) {
      String flagString = e.getFlagString() != null ? e.getFlagString() : String.valueOf(e.getFlag().getCharacter());
      sender.sendMessage("Expecting value for flag '" + flagString + "'");
    }
    return null;
  }

  public Flag getByChar(char character) {
    for (Flag flag : flags) {
      if (flag.getCharacter() == character) {
        return flag;
      }
    }
    return null;
  }

  public Flag getByName(String name) {
    for (Flag flag : flags) {
      if (flag.getName().equals(name)) {
        return flag;
      }
    }
    return null;
  }

  public Set<Flag> getFlags() {
    return Collections.unmodifiableSet(flags);
  }

  public String getShorthandPrefix() {
    return shorthandPrefix;
  }

  public String getLonghandPrefix() {
    return longhandPrefix;
  }
}
