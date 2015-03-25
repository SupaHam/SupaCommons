package com.supaham.commons.bukkit.worldedit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.command.parametric.ArgumentStack;
import com.sk89q.worldedit.util.command.parametric.BindingBehavior;
import com.sk89q.worldedit.util.command.parametric.BindingHelper;
import com.sk89q.worldedit.util.command.parametric.BindingMatch;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import com.supaham.commons.bukkit.CommonBukkitException;
import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.bukkit.Language;
import com.supaham.commons.bukkit.Language.WorldEdit;
import com.supaham.commons.bukkit.serializers.MaterialDataSerializer;
import com.supaham.commons.bukkit.utils.WorldEditUtils;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import javax.annotation.Nonnull;

import pluginbase.config.serializers.Serializers;

/**
 * @since 0.1
 */
public class CBinding extends BindingHelper {

  private final CommonPlugin plugin;

  protected static <T> T _checkNotNull(T object, String message) throws ParameterException {
    if (object == null) {
      throw new ParameterException(message);
    }
    return object;
  }

  /**
   * Create a new instance.
   *
   * @param plugin the WorldEdit instance to bind to
   */
  public CBinding(@Nonnull CommonPlugin plugin) {
    checkNotNull(plugin, "plugin cannot be null");
    this.plugin = plugin;
  }

  /**
   * Gets an {@link Player} from an {@link ArgumentStack}.
   *
   * @param context the context
   *
   * @return a {@link Player}
   *
   * @throws ParameterException on error
   */
  @BindingMatch(type = Player.class,
      behavior = BindingBehavior.PROVIDES)
  public Player getPlayer(ArgumentStack context) throws ParameterException {
    CommandSender sender = getCommandSender(context);
    if (sender == null) {
      throw new ParameterException("No player to get.");
    } else if (sender instanceof Player) {
      return (Player) sender;
    } else {
      throw new ParameterException("Sender is not a player.");
    }
  }

  /**
   * Gets an {@link Player} from an {@link ArgumentStack}.
   *
   * @param context the context
   *
   * @return a {@link Player}
   *
   * @throws ParameterException on error
   */
  @BindingMatch(type = CommandSender.class,
      behavior = BindingBehavior.PROVIDES)
  public CommandSender getCommandSender(ArgumentStack context) throws ParameterException {
    return context.getContext().getLocals().get(CommandSender.class);
  }

  /**
   * Gets a {@link MaterialData} from an {@link ArgumentStack}.
   *
   * @param context the context
   *
   * @return a {@link MaterialData} instance
   *
   * @throws ParameterException on error
   */
  @BindingMatch(type = MaterialData.class,
      behavior = BindingBehavior.CONSUMES,
      consumedCount = 1)
  public MaterialData getMaterialData(ArgumentStack context) throws ParameterException {
    String input = _checkNotNull(context.next(), "Please specify a material data.");
    MaterialDataSerializer serializer = Serializers.getSerializer(MaterialDataSerializer.class);
    return _checkNotNull(serializer.deserialize(input, MaterialData.class),
                         "'" + input + "' is not a valid arena.");
  }

  /**
   * Gets a {@link World} from a {@link Player}.
   *
   * @param context the context
   *
   * @return a {@link World} instance
   *
   * @throws ParameterException on error
   */
  @BindingMatch(type = World.class,
      behavior = BindingBehavior.PROVIDES)
  public World getWorldByPlayer(ArgumentStack context) throws ParameterException {
    return getPlayer(context).getWorld();
  }

  /**
   * Gets a {@link World} from an {@link ArgumentStack}.
   *
   * @param context the context
   *
   * @return a {@link World} instance
   *
   * @throws ParameterException on error
   */
  @BindingMatch(type = World.class,
      behavior = BindingBehavior.CONSUMES,
      consumedCount = 1)
  public World getWorld(ArgumentStack context) throws ParameterException {
    String input = _checkNotNull(context.next(), "Please specify a world name.");
    World world = plugin.getServer().getWorld(input);
    if (world == null) {
      throw new ParameterException(Language.World.NOT_FOUND.getParsedMessage(input));
    }
    return world;
  }

  /* ================================
   * >> WORLDEDIT
   * ================================ */
  
  /**
   * Gets a selection from a {@link ArgumentStack}.
   *
   * @param context the context
   * @param selection the annotation
   *
   * @return a selection
   *
   * @throws IncompleteRegionException if no selection is available
   * @throws ParameterException on other error
   */
  @BindingMatch(classifier = Selection.class,
      type = Region.class,
      behavior = BindingBehavior.PROVIDES)
  public Object getSelection(ArgumentStack context, Selection selection)
      throws ParameterException, IncompleteRegionException, CommonBukkitException {
    Player player = getPlayer(context);
    WorldEditPlugin we = WorldEditUtils.get();
    if (we == null) {
      return null;
    }
    com.sk89q.worldedit.bukkit.selections.Selection sel = we.getSelection(player);
    if (sel == null) {
      throw new IncompleteRegionException();
    }

    if (selection.value() != null && selection.value().length > 0) {
      for (Class<? extends com.sk89q.worldedit.bukkit.selections.Selection> clazz :
          selection.value()) {
        if (!sel.getClass().isAssignableFrom(clazz)) {
          throw new CommonBukkitException(WorldEdit.SELECTION_NOT_SUPPORTED, sel.getRegionSelector().getTypeName());
        }
      }
    }

    return sel.getRegionSelector().getRegion();
  }
}
