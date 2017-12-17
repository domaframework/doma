package org.seasar.doma.wrapper;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BigIntegerWrapperTest extends TestCase {

    /**
     * 
     */
    public void testSetBigIntegerAsNumber() {
        Number greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE)
                .toBigInteger();
        BigIntegerWrapper wrapper = new BigIntegerWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(greaterThanLongMaxValue, wrapper.get());
    }

    /**
     * 
     */
    public void testSetBigDecimal() {
        BigDecimal greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE);
        BigIntegerWrapper wrapper = new BigIntegerWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(greaterThanLongMaxValue.toBigInteger(), wrapper.get());
    }

    /**
     * 
     */
    public void testIncrement() {
        BigIntegerWrapper wrapper = new BigIntegerWrapper(new BigInteger("10"));
        wrapper.increment();
        assertEquals(new BigInteger("11"), wrapper.get());
    }

    /**
     * 
     */
    public void testDecrement() {
        BigIntegerWrapper wrapper = new BigIntegerWrapper(new BigInteger("10"));
        wrapper.decrement();
        assertEquals(new BigInteger("9"), wrapper.get());
    }
}
