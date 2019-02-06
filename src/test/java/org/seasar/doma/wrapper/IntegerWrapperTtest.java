package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class IntegerWrapperTtest {

  /** */
  @Test
  public void testIncrement() {
    IntegerWrapper wrapper = new IntegerWrapper(10);
    wrapper.increment();
    assertEquals(new Integer(11), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    IntegerWrapper wrapper = new IntegerWrapper(10);
    wrapper.decrement();
    assertEquals(new Integer(9), wrapper.get());
  }
}
