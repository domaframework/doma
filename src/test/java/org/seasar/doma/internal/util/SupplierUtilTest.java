package org.seasar.doma.internal.util;

import junit.framework.TestCase;

import static org.seasar.doma.internal.util.SupplierUtil.iife;

public class SupplierUtilTest extends TestCase {

    public void testIife() {
        assertEquals("a", iife(() -> "a"));
    }
}
