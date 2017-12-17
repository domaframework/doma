package org.seasar.doma.wrapper;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BigDecimalWrapperTest extends TestCase {

    /**
     * 
     */
    public void testSetBigDecimalAsNumber() {
        Number greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE);
        BigDecimalWrapper wrapper = new BigDecimalWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(greaterThanLongMaxValue, wrapper.get());
    }

    /**
     * 
     */
    public void testSetBigInteger() {
        BigInteger greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE)
                .toBigInteger();
        BigDecimalWrapper wrapper = new BigDecimalWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(new BigDecimal(greaterThanLongMaxValue), wrapper.get());
    }

    /**
     * 
     */
    public void testIncrement() {
        BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
        wrapper.increment();
        assertEquals(new BigDecimal(11), wrapper.get());
    }

    /**
     * 
     */
    public void testDecrement() {
        BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
        wrapper.decrement();
        assertEquals(new BigDecimal(9), wrapper.get());
    }
}
