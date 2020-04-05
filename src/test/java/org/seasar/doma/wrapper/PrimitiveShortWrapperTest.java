package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PrimitiveShortWrapperTest {

  @Test
  void set() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.set((short) 10);
    assertEquals((short) 10, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals((short) 20, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.set(null);
    assertEquals((short) 0, wrapper.get());
    wrapper.set((Number) null);
    assertEquals((short) 0, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    assertEquals((short) 0, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper((short) 1);
    wrapper.increment();
    assertEquals((short) 2, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.increment();
    assertEquals((short) 1, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper((short) 1);
    wrapper.decrement();
    assertEquals((short) 0, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.decrement();
    assertEquals((short) -1, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
