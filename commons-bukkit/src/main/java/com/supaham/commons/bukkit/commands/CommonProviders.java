package com.supaham.commons.bukkit.commands;

import com.supaham.commons.bukkit.serializers.MaterialDataSerializer;
import com.supaham.commons.bukkit.utils.SerializationUtils;

import org.bukkit.material.MaterialData;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;

/**
 * @since 0.1
 */
public class CommonProviders {

  protected static <T> T _checkNotNull(T object, String message) throws InvalidCommandArgument {
    if (object == null) {
      throw new InvalidCommandArgument(message);
    }
    return object;
  }
  
  public static void registerAll(CommandContexts<BukkitCommandExecutionContext> context) {
    registerMaterialData(context);
  }
  
  public static void registerMaterialData(CommandContexts<BukkitCommandExecutionContext> context) {
    context.registerContext(MaterialData.class, new MaterialDataContextResolver());
  }

  private static final class MaterialDataContextResolver
      implements ContextResolver<MaterialData, BukkitCommandExecutionContext> {

    @Override public MaterialData getContext(BukkitCommandExecutionContext context)
        throws InvalidCommandArgument {
      String input = _checkNotNull(context.popFirstArg(), "Please specify a material data.");
      MaterialData serializer = SerializationUtils.deserializeWith(input, MaterialDataSerializer.class);
      return _checkNotNull(serializer, "'" + input + "' is not a valid data.");
    }
  }
}
