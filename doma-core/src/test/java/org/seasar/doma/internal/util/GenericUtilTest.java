package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.TypeVariable;
import org.junit.jupiter.api.Test;

public class GenericUtilTest {

  @Test
  public void testFieldType() throws Exception {
    Class<?> arg1 =
        GenericsUtil.inferTypeArgument(
            Bbb1.class, (TypeVariable<?>) Aaa1.class.getField("t1").getGenericType());
    assertEquals(String.class, arg1);

    Class<?> arg2 =
        GenericsUtil.inferTypeArgument(
            Bbb1.class, (TypeVariable<?>) Aaa1.class.getField("t2").getGenericType());
    assertEquals(Integer.class, arg2);

    Class<?> arg3 =
        GenericsUtil.inferTypeArgument(
            Bbb1.class, (TypeVariable<?>) Aaa1.class.getField("t3").getGenericType());
    assertNull(arg3);
  }

  @Test
  public void testInterfaceReturnType() throws Exception {
    // type argument T1 is generic
    Class<?> arg =
        GenericsUtil.inferTypeArgument(
            Ccc2.class,
            (TypeVariable<?>) Aaa2.class.getMethod("m1", Object.class).getGenericReturnType());
    assertNull(arg);

    // type argument T2 is generic
    arg =
        GenericsUtil.inferTypeArgument(
            Ccc2.class,
            (TypeVariable<?>) Aaa2.class.getMethod("m2", Object.class).getGenericReturnType());
    assertNull(arg);

    // type argument T3 is concrete
    arg =
        GenericsUtil.inferTypeArgument(
            Ccc2.class,
            (TypeVariable<?>) Aaa2.class.getMethod("m3", Object.class).getGenericReturnType());
    assertEquals(Boolean.class, arg);
  }

  @Test
  public void testClassReturnType() throws Exception {
    // type argument T1 is concrete
    Class<?> arg =
        GenericsUtil.inferTypeArgument(
            Ccc2.class,
            (TypeVariable<?>) Bbb2.class.getMethod("m1", Object.class).getGenericReturnType());
    assertEquals(String.class, arg);

    // type argument T2 is generic
    arg =
        GenericsUtil.inferTypeArgument(
            Ccc2.class,
            (TypeVariable<?>) Bbb2.class.getMethod("m2", Object.class).getGenericReturnType());
    assertNull(arg);
  }

  public static class Aaa1<T1, T2, T3> {
    public T1 t1;
    public T2 t2;
    public T3 t3;
  }

  public static class Bbb1<T3> extends Aaa1<String, Integer, T3> {}

  public interface Aaa2<T1, T2, T3> {
    T1 m1(T1 value);

    T2 m2(T2 value);

    T3 m3(T3 value);
  }

  public static class Bbb2<T1, T2> implements Aaa2<T1, T2, Boolean> {
    @Override
    public T1 m1(T1 value) {
      return value;
    }

    @Override
    public T2 m2(T2 value) {
      return value;
    }

    @Override
    public Boolean m3(Boolean value) {
      return value;
    }
  }

  public static class Ccc2<T2> extends Bbb2<String, T2> {}
}
