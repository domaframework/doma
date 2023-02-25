package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class BigDecimalWrapperTest {

  /** */
  @Test
  public void testSetBigDecimalAsNumber() {
    Number greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE);
    BigDecimalWrapper wrapper = new BigDecimalWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(greaterThanLongMaxValue, wrapper.get());
  }

  /** */
  @Test
  public void testSetBigInteger() {
    BigInteger greaterThanLongMaxValue =
        new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE).toBigInteger();
    BigDecimalWrapper wrapper = new BigDecimalWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(new BigDecimal(greaterThanLongMaxValue), wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
    wrapper.increment();
    assertEquals(new BigDecimal(11), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
    wrapper.decrement();
    assertEquals(new BigDecimal(9), wrapper.get());
  }

  @Test
  public void testHasEqualValue() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal("10"));
    assertTrue(wrapper.hasEqualValue(new BigDecimal("10.00")));
    assertNotEquals(wrapper.get(), new BigDecimal("10.00"));
  }
}
