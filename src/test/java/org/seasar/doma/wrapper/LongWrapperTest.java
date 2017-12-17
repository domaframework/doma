package org.seasar.doma.wrapper;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class LongWrapperTest extends TestCase {

    /**
     * 
     */
    public void testIncrement() {
        LongWrapper wrapper = new LongWrapper(10L);
        wrapper.increment();
        assertEquals(Long.valueOf(11L), wrapper.get());
    }

    /**
     * 
     */
    public void testDecrement() {
        LongWrapper wrapper = new LongWrapper(10L);
        wrapper.decrement();
        assertEquals(Long.valueOf(9L), wrapper.get());
    }
}
