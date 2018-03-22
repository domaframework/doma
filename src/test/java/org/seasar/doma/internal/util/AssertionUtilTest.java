package org.seasar.doma.internal.util;

import junit.framework.TestCase;

public class AssertionUtilTest extends TestCase {

    public void testAssertEquals_equals() {
        AssertionUtil.assertEquals(1, 1);
        AssertionUtil.assertEquals(null, null);
    }

    public void testAssertEquals_notEquals() {
        try {
            AssertionUtil.assertEquals(1, 2);
            fail();
        } catch (AssertionError expected) {
        }
        try {
            AssertionUtil.assertEquals(null, 2);
            fail();
        } catch (AssertionError expected) {
        }
        try {
            AssertionUtil.assertEquals(1, null);
            fail();
        } catch (AssertionError expected) {
        }
    }

}