package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PrimitiveFloatWrapperTest {

  @Test
  void set() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.set(10);
    assertEquals(10f, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals(20f, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.set(null);
    assertEquals(0f, wrapper.get());
    wrapper.set((Number) null);
    assertEquals(0f, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    assertEquals(0f, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper(1);
    wrapper.increment();
    assertEquals(2f, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.increment();
    assertEquals(1f, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper(1);
    wrapper.decrement();
    assertEquals(0f, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.decrement();
    assertEquals(-1f, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
