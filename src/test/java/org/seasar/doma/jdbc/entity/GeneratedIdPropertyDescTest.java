package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;
import org.seasar.doma.wrapper.IntegerWrapper;

public class GeneratedIdPropertyDescTest extends TestCase {

  public void testValueSetter() {
    var valueSetter = new GeneratedIdPropertyDesc.ValueSetter();
    var integerWrapper = new IntegerWrapper(0);
    var accepted = integerWrapper.accept(valueSetter, () -> 1L, null);
    assertTrue(accepted);
    assertEquals(Integer.valueOf(1), integerWrapper.get());
  }
}
