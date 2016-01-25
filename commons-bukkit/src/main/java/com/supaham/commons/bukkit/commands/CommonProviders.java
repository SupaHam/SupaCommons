package com.supaham.commons.bukkit.commands;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.AbstractModule;
import com.sk89q.intake.parametric.IllegalParameterException;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import com.supaham.commons.bukkit.serializers.MaterialDataSerializer;
import com.supaham.commons.bukkit.utils.SerializationUtils;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

/**
 * @since 0.1
 */
public class CommonProviders extends AbstractModule {

  protected static <T> T _checkNotNull(T object, String message) throws IllegalParameterException {
    if (object == null) {
      throw new IllegalParameterException(message);
    }
    return object;
  }

  @Override protected void configure() {
    bind(MaterialData.class).toProvider(new MaterialDataProvider());
  }
  
  private static final class MaterialDataProvider implements Provider<MaterialData> {

    @Override public boolean isProvided() {
      return true;
    }

    @Nullable @Override public MaterialData get(CommandArgs arguments, List<? extends Annotation> modifiers)
        throws ArgumentException, ProvisionException {
      String input = _checkNotNull(arguments.next(), "Please specify a material data.");
      MaterialData serializer =
          SerializationUtils.deserializeWith(input, MaterialDataSerializer.class);
      return _checkNotNull(serializer, "'" + input + "' is not a valid arena.");
    }

    @Override public List<String> getSuggestions(String prefix) {
      if(prefix.contains(":")) {
        String uppercase = prefix.toLowerCase();
        return Arrays.stream(Material.values())
            .filter(material ->  material.name().startsWith(uppercase))
            .map(Enum::name)
            .collect(Collectors.toList());
      } else {
        return Collections.emptyList();
      }
    }
  }
}
