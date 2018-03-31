package org.seasar.doma.wrapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import junit.framework.TestCase;

public class BigIntegerWrapperTest extends TestCase {

  /** */
  public void testSetBigIntegerAsNumber() {
    Number greaterThanLongMaxValue =
        new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE).toBigInteger();
    var wrapper = new BigIntegerWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(greaterThanLongMaxValue, wrapper.get());
  }

  /** */
  public void testSetBigDecimal() {
    var greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE);
    var wrapper = new BigIntegerWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(greaterThanLongMaxValue.toBigInteger(), wrapper.get());
  }

  /** */
  public void testIncrement() {
    var wrapper = new BigIntegerWrapper(new BigInteger("10"));
    wrapper.increment();
    assertEquals(new BigInteger("11"), wrapper.get());
  }

  /** */
  public void testDecrement() {
    var wrapper = new BigIntegerWrapper(new BigInteger("10"));
    wrapper.decrement();
    assertEquals(new BigInteger("9"), wrapper.get());
  }
}
