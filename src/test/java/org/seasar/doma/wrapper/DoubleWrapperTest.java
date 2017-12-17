package org.seasar.doma.wrapper;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DoubleWrapperTest extends TestCase {

    /**
     * 
     */
    public void testIncrement() {
        DoubleWrapper wrapper = new DoubleWrapper(10d);
        wrapper.increment();
        assertEquals(Double.valueOf(11d), wrapper.get());
    }

    /**
     * 
     */
    public void testDecrement() {
        DoubleWrapper wrapper = new DoubleWrapper(10d);
        wrapper.decrement();
        assertEquals(Double.valueOf(9d), wrapper.get());
    }
}
