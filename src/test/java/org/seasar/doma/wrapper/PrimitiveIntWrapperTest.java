package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PrimitiveIntWrapperTest {

  @Test
  void set() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper();
    wrapper.set(10);
    assertEquals(10, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals(20, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper();
    wrapper.set(null);
    assertEquals(0, wrapper.get());
    wrapper.set((Number) null);
    assertEquals(0, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper();
    assertEquals(0, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper(1);
    wrapper.increment();
    assertEquals(2, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper();
    wrapper.increment();
    assertEquals(1, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper(1);
    wrapper.decrement();
    assertEquals(0, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper();
    wrapper.decrement();
    assertEquals(-1, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveIntWrapper wrapper = new PrimitiveIntWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
