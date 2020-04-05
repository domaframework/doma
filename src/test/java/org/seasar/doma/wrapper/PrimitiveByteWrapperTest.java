package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PrimitiveByteWrapperTest {

  @Test
  void set() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.set((byte) 10);
    assertEquals((byte) 10, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals((byte) 20, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.set(null);
    assertEquals((byte) 0, wrapper.get());
    wrapper.set((Number) null);
    assertEquals((byte) 0, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    assertEquals((byte) 0, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper((byte) 1);
    wrapper.increment();
    assertEquals((byte) 2, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.increment();
    assertEquals((byte) 1, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper((byte) 1);
    wrapper.decrement();
    assertEquals((byte) 0, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.decrement();
    assertEquals((byte) -1, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
