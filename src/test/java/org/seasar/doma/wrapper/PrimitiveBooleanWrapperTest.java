package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrimitiveBooleanWrapperTest {

  @Test
  void set() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    wrapper.set(true);
    assertEquals(true, wrapper.get());
    wrapper.set(false);
    assertEquals(false, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    wrapper.set(null);
    assertEquals(false, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    assertEquals(false, wrapper.getDefault());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
