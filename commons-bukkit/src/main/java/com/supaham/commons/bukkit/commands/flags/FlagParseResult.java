package com.supaham.commons.bukkit.commands.flags;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

public class FlagParseResult {

  Map<Flag, String> results;
  private String[] args;
  
  public FlagParseResult(@Nullable Map<Flag, String> results, String[] args) {
    this.results = results;
    this.args = args;
  }

  public boolean containsExact(char character) {
    for (Flag flag : results.keySet()) {
      if (flag.getCharacter() == character) {
        return true;
      }
    }
    return false;
  }

  public boolean containsExact(String str) {
    for (Flag flag : results.keySet()) {
      if (flag.getName().equals(str)) {
        return true;
      }
    }
    return false;
  }

  public boolean contains(char character) {
    char lcase = Character.toLowerCase(character);
    for (Flag flag : results.keySet()) {
      if (Character.toLowerCase(flag.getCharacter()) == lcase) {
        return true;
      }
    }
    return false;
  }

  public boolean contains(String str) {
    String lcase = str.toLowerCase();
    for (Flag flag : results.keySet()) {
      if (flag.getName().toLowerCase().equals(lcase)) {
        return true;
      }
    }
    return false;
  }

  public String getExact(char character) {
    for (Entry<Flag, String> entry : results.entrySet()) {
      if (entry.getKey().getCharacter() == character) {
        return entry.getValue();
      }
    }
    return null;
  }

  public String getExact(String str) {
    for (Entry<Flag, String> entry : results.entrySet()) {
      if (entry.getKey().getName().equals(str)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public String get(char character) {
    char lcase = Character.toLowerCase(character);
    for (Entry<Flag, String> entry : results.entrySet()) {
      if (Character.toLowerCase(entry.getKey().getCharacter()) == lcase) {
        return entry.getValue();
      }
    }
    return null;
  }

  public String get(String str) {
    String lcase = str.toLowerCase();
    for (Entry<Flag, String> entry : results.entrySet()) {
      if (entry.getKey().getName().toLowerCase().equals(lcase)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public boolean isError() {
    return results == null;
  }

  public Map<Flag, String> getResults() {
    return Collections.unmodifiableMap(results);
  }

  public String[] getArgs() {
    return Arrays.copyOf(args, args.length);
  }
}
