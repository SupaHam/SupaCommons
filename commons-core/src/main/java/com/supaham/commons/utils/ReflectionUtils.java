package com.supaham.commons.utils;

import com.google.common.base.Preconditions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class ReflectionUtils {

  public static List<Method> getUniqueDeclaredMethods(@Nonnull Class<?> clazz) {
    return getUniqueDeclaredMethods(clazz, Collections.emptyList());
  }

  public static List<Method> getUniqueDeclaredMethods(@Nonnull Class<?> clazz,
                                                      @Nonnull Collection ignoreClasses) {
    Preconditions.checkNotNull(clazz, "class cannot be null.");
    Preconditions.checkNotNull(ignoreClasses, "ignoreClasses cannot be null.");

    List<Method> result = new ArrayList<>();
    getUniqueDeclaredMethods(clazz, ignoreClasses, result);
    return result;
  }

  private static void getUniqueDeclaredMethods(@Nonnull Class<?> clazz,
                                               @Nonnull Collection<Class<?>> ignoreClasses,
                                               @Nonnull List<Method> result) {
    Preconditions.checkNotNull(clazz, "class cannot be null.");
    Preconditions.checkNotNull(ignoreClasses, "ignoreClasses cannot be null.");
    Preconditions.checkNotNull(result, "result cannot be null.");

    for (Method method : clazz.getDeclaredMethods()) {
      boolean found = false;
      for (Method existing : result) {
        // find overridden methods by only checking name and parameters
        // using equals() checks return type too, we don't care about that.
        if (sameMethodSignature(method, existing)) {
          found = true;
          break;
        }
      }
      if (!found) {
        result.add(method);
      }
    }

    if (clazz.getSuperclass() != null) {
      if (!ignoreClasses.contains(clazz.getSuperclass())) {
        getUniqueDeclaredMethods(clazz.getSuperclass(), ignoreClasses, result);
      }
    } else if (clazz.isInterface()) {
      for (Class<?> interfaceClazz : clazz.getInterfaces()) {
        if (!ignoreClasses.contains(interfaceClazz)) {
          getUniqueDeclaredMethods(interfaceClazz, ignoreClasses, result);
        }
      }
    }
  }

  /**
   * Returns whether two {@link Method}s have similar signature. Firstly, {@code m1} and {@code m2}
   * are checked for null pointers, if true, {@code true} is returned. Otherwise, if either one is
   * null {@code false} is returned. Then, both methods are checked for the same name, if false,
   * false is returned. Lastly, both methods are checked for same exact parameter types, if true,
   * and only then, {@code true} is returned.
   *
   * @param m1 method 1 to check
   * @param m2 method 2 to check
   *
   * @return whether {@code m1} and {@code m2} are the same
   */
  public static boolean sameMethodSignature(Method m1, Method m2) {
    if (m1 == null && m2 == null) {
      return true;
    } else if (m1 == null || m2 == null) {
      return false;
    } else {
      if (m1.getName().equals(m2.getName()) && m1.getParameterCount() == m2.getParameterCount()) {
        return Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
      }
      return false;
    }
  }
}
