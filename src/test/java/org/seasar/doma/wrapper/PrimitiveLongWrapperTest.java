package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PrimitiveLongWrapperTest {

  @Test
  void set() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.set(10);
    assertEquals(10L, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals(20L, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.set(null);
    assertEquals(0L, wrapper.get());
    wrapper.set((Number) null);
    assertEquals(0L, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    assertEquals(0L, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper(1);
    wrapper.increment();
    assertEquals(2L, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.increment();
    assertEquals(1L, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper(1);
    wrapper.decrement();
    assertEquals(0L, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.decrement();
    assertEquals(-1L, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
