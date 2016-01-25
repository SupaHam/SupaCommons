package com.supaham.commons.bukkit.commands.utils;

import com.google.common.base.Preconditions;

import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import com.supaham.commons.bukkit.commands.CommonCommandsManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Represents data for creating a custom Intake command.
 */
public class CommonCommandData {

  private final CommonCommandsManager manager;
  private final Object classInstance;
  private final Method method;
  private final List<String> aliases;
  private final List<String> permissions;

  public static Builder builder() {
    return new Builder();
  }

  private CommonCommandData(CommonCommandsManager manager, Object classInstance, Method method, List<String> aliases,
                            List<String> permissions) {
    this.manager = manager;
    this.classInstance = classInstance;
    this.method = method;

    if (aliases != null) {
      this.aliases = aliases;
    } else {
      this.aliases = Arrays.asList(method.getDeclaredAnnotation(Command.class).aliases());
    }

    if (permissions != null) {
      this.permissions = permissions;
    } else if (method.isAnnotationPresent(Require.class)) {
      this.permissions = Arrays.asList(method.getDeclaredAnnotation(Require.class).value());
    } else {
      this.permissions = null;
    }
  }

  public CommonCommandsManager getManager() {
    return manager;
  }

  public Object getClassInstance() {
    return classInstance;
  }

  public Method getMethod() {
    return method;
  }

  public List<String> getAliases() {
    return this.aliases;
  }

  public String[] getAliasesArray() {
    return this.aliases.toArray(new String[this.aliases.size()]);
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public static final class Builder {

    private CommonCommandsManager manager;
    private Object classInstance;
    private Method method;
    private List<String> aliases;
    private List<String> permissions;

    public Builder manager(CommonCommandsManager manager) {
      this.manager = manager;
      return this;
    }

    public Builder classIntance(Object object) {
      this.classInstance = object;
      return this;
    }

    public Builder method(Method method) {
      this.method = method;
      return this;
    }

    public Builder methodName(@Nonnull String name) {
      Preconditions.checkNotNull(name, "name cannot be null.");
      boolean found = false;
      Method method = null;
      for (Method method1 : classInstance.getClass().getDeclaredMethods()) {
        if (method1.getName().equals(name)) {
          found = true;
          Preconditions.checkState(method == null,
                                   "There are multiple methods of the name %s. Please use method(Method).",
                                   name);
          if (method1.isAnnotationPresent(Command.class)) {
            method = method1;
          }
        }
      }
      // Debug some helpful messages.
      if (method == null) {
        if (found) {
          throw new IllegalStateException("Found method called " + name + " but it is not annotated with @Command.");
        } else {
          throw new IllegalStateException("No method exists in " + classInstance.getClass().getName()
                                          + " called " + name + ".");
        }
      }
      return method(method);
    }

    public List<String> aliases() {
      return this.aliases;
    }

    public Builder aliases(List<String> aliases) {
      this.aliases = aliases;
      return this;
    }

    public List<String> permissions() {
      return this.permissions;
    }

    public Builder permissions(List<String> permissions) {
      this.permissions = permissions;
      return this;
    }

    public CommonCommandData build() {
      Preconditions.checkNotNull(manager, "manager cannot be null.");
      Preconditions.checkNotNull(classInstance, "classInstance cannot be null.");

      if (aliases == null || permissions == null) {
        if (aliases == null && permissions == null) {
          throw new NullPointerException("Aliases or permissions must be set.");
        }
      }

      Method method = this.method;
      if (method == null) {
        for (Method method1 : this.classInstance.getClass().getDeclaredMethods()) {
          if (method1.isAnnotationPresent(Command.class)) {
            Preconditions.checkArgument(method == null,
                                        this.classInstance.getClass().getName() + " has two command methods. Please "
                                        + "specify which method");
            method = method1;
          }
        }
      }
      Preconditions.checkNotNull(method, "No command method has been set or found.");

      Command definition = method.getAnnotation(Command.class);
      Preconditions.checkNotNull(definition, "Method lacks a @Command annotation");

      return new CommonCommandData(manager, classInstance, method, aliases, permissions);
    }
    
    public void register() {
      manager.registerMethod(this);
    }

    public void register(CommonCommandsManager manager) {
      manager(manager).register();
    }
  }
}
