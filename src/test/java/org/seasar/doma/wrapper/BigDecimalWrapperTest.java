package org.seasar.doma.wrapper;

import java.math.BigDecimal;
import junit.framework.TestCase;

public class BigDecimalWrapperTest extends TestCase {

  /** */
  public void testSetBigDecimalAsNumber() {
    Number greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE);
    var wrapper = new BigDecimalWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(greaterThanLongMaxValue, wrapper.get());
  }

  /** */
  public void testSetBigInteger() {
    var greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE).toBigInteger();
    var wrapper = new BigDecimalWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(new BigDecimal(greaterThanLongMaxValue), wrapper.get());
  }

  /** */
  public void testIncrement() {
    var wrapper = new BigDecimalWrapper(new BigDecimal(10));
    wrapper.increment();
    assertEquals(new BigDecimal(11), wrapper.get());
  }

  /** */
  public void testDecrement() {
    var wrapper = new BigDecimalWrapper(new BigDecimal(10));
    wrapper.decrement();
    assertEquals(new BigDecimal(9), wrapper.get());
  }
}
