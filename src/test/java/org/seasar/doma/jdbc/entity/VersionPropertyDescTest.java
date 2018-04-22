package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;
import org.seasar.doma.wrapper.IntegerWrapper;

public class VersionPropertyDescTest extends TestCase {

  public void testValueSetter() {
    var valueSetter = new VersionPropertyDesc.ValueSetter();
    var integerWrapper = new IntegerWrapper(0);
    var accepted = integerWrapper.accept(valueSetter, 1, null);
    assertTrue(accepted);
    assertEquals(Integer.valueOf(1), integerWrapper.get());
  }
}
