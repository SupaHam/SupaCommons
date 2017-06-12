package com.supaham.commons.bukkit.commands.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Chars;

import com.sk89q.intake.Command;
import com.sk89q.intake.CommandCallable;
import com.sk89q.intake.Description;
import com.sk89q.intake.ImmutableDescription;
import com.sk89q.intake.completion.CommandCompleter;
import com.sk89q.intake.parametric.AbstractParametricCallable;
import com.sk89q.intake.parametric.ArgumentParser;
import com.sk89q.intake.parametric.IllegalParameterException;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.ParametricException;
import com.sk89q.intake.parametric.handler.InvokeListener;
import com.supaham.commons.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Ali on 25/01/2016.
 */
public class CommandUtils {

  private static Constructor ctor;
  private static Method setCommandAnnotations;
  private static Method setIgnoreUnusedFlags;
  private static Method setUnusedFlags;

  private static Method getInvokeListeners;

  static {
    try {
      ctor = Class.forName("com.sk89q.intake.parametric.MethodCallable")
          .getDeclaredConstructor(ParametricBuilder.class, ArgumentParser.class, Object.class, Method.class,
                                  Description.class, List.class, CommandCompleter.class);
      ctor.setAccessible(true);

      setCommandAnnotations = ReflectionUtils.getMethod(AbstractParametricCallable.class, "setCommandAnnotations", 
                                                        List.class);
      setIgnoreUnusedFlags = ReflectionUtils.getMethod(AbstractParametricCallable.class, "setIgnoreUnusedFlags", 
                                                       boolean.class);
      setUnusedFlags = ReflectionUtils.getMethod(AbstractParametricCallable.class, "setUnusedFlags", Set.class);

      getInvokeListeners = ReflectionUtils.getMethod(ParametricBuilder.class, "getInvokeListeners");
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

/*
 * Intake, a command processing library
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) Intake team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

  public static void registerMethod(CommonCommandData commonCommandData) throws ParametricException {
    Preconditions.checkNotNull(commonCommandData, "commonCommandData cannot be null.");

    CommandCallable callable = create(commonCommandData);
    commonCommandData.getManager().dispatcher().registerCommand(callable, commonCommandData.getAliasesArray());
  }

  public static AbstractParametricCallable create(CommonCommandData data) throws IllegalParameterException {
    checkNotNull(data, "data");

    ParametricBuilder builder = data.getManager().config();
    Object object = data.getClassInstance();
    Method method = data.getMethod();
    Set<Annotation> commandAnnotations = ImmutableSet.copyOf(method.getAnnotations());

    Command definition = method.getAnnotation(Command.class);
    checkNotNull(definition, "Method lacks a @Command annotation");

    boolean ignoreUnusedFlags = definition.anyFlags();
    Set<Character> unusedFlags = ImmutableSet.copyOf(Chars.asList(definition.flags().toCharArray()));

    Annotation[][] annotations = method.getParameterAnnotations();
    Type[] types = method.getGenericParameterTypes();

    ArgumentParser.Builder parserBuilder = new ArgumentParser.Builder(builder.getInjector());
    for (int i = 0; i < types.length; i++) {
      parserBuilder.addParameter(types[i], Arrays.asList(annotations[i]));
    }
    ArgumentParser parser = parserBuilder.build();

    ImmutableDescription.Builder descBuilder = new ImmutableDescription.Builder()
        .setParameters(parser.getUserParameters())
        .setShortDescription(!definition.desc().isEmpty() ? definition.desc() : null)
        .setHelp(!definition.help().isEmpty() ? definition.help() : null)
        .setUsageOverride(!definition.usage().isEmpty() ? definition.usage() : null);

    List<String> permissions = data.getPermissions();

    try {
      for (InvokeListener listener : (List<InvokeListener>) getInvokeListeners.invoke(builder)) {
        listener.updateDescription(commandAnnotations, parser, descBuilder);
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    Description description = descBuilder.build();

    try {
      AbstractParametricCallable o =
          (AbstractParametricCallable) ctor.newInstance(builder, parser, object, method, description, permissions, null); // TODO specify CommandCompleter
      setCommandAnnotations.invoke(o, ImmutableList.copyOf(method.getAnnotations()));
      setIgnoreUnusedFlags.invoke(o, ignoreUnusedFlags);
      setUnusedFlags.invoke(o, unusedFlags);
      return o;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
