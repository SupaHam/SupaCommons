package com.supaham.commons.utils;

import static com.supaham.commons.utils.ReflectionUtils.getUniqueDeclaredMethods;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ReflectionUtilsTest {

  @Test
  public void testUniqueMethods() throws Exception {
    List<Method> list = getUniqueDeclaredMethods(B.class, Collections.singletonList(Object.class));
    System.out.println(list);
    Assert.assertEquals(3, list.size());
  }

  public static class A {

    public void someMethod() {}

    public void SomeSuperClassMethod() {}
  }

  public static class B extends A {

    @Override public void someMethod() { super.someMethod(); }

    public void subclassMethod() {}
  }
}
