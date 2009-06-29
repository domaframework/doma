package org.seasar.doma.internal;

import org.seasar.doma.internal.ProductInfo;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ProductInfoTest extends TestCase {

    public void test() throws Exception {
        assertEquals("Doma", ProductInfo.getName());
        assertEquals("@VERSION@", ProductInfo.getVersion());
    }
}
