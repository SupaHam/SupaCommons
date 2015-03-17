package com.supaham.commons.bukkit.worldedit;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.util.command.parametric.ExceptionConverterHelper;
import com.sk89q.worldedit.util.command.parametric.ExceptionMatch;
import com.supaham.commons.exceptions.CommonException;
import com.supaham.commons.bukkit.CommonPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * Converts non {@link CommandException}s into CommandExceptions.
 *
 * @since 0.1
 */
public class CommonExceptionConverter extends ExceptionConverterHelper {

  private static final Pattern numberFormat = Pattern.compile("^For input string: \"(.*)\"$");

  private final CommonPlugin plugin;

  public CommonExceptionConverter(@Nonnull CommonPlugin plugin) {
    this.plugin = plugin;
  }

  @ExceptionMatch
  public void convert(NumberFormatException e) throws CommandException {
    final Matcher matcher = numberFormat.matcher(e.getMessage());
    if (matcher.matches()) {
      throw new CommandException("Number expected; string \"" + matcher.group(1) + "\" given.");
    } else {
      throw new CommandException("Number expected; string given.");
    }
  }

  @ExceptionMatch
  public void convert(IncompleteRegionException e) throws CommandException {
    throw new CommandException("Make a region selection first.");
  }

  @ExceptionMatch
  public void convert(CommonException e) throws CommandException {
    throw new CommandException(e.getMessage(), e);
  }

  // TODO do we need to add a CommonBukkitException or would it fall back to the already existing
  // CommonException converter?
}
