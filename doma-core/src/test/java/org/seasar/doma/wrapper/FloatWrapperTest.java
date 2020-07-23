package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FloatWrapperTest {

  /** */
  @Test
  public void testIncrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.increment();
    assertEquals(new Float(11f), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.decrement();
    assertEquals(new Float(9f), wrapper.get());
  }
}
