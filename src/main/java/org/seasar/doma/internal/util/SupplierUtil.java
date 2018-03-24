package org.seasar.doma.internal.util;

import java.util.function.Supplier;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

public final class SupplierUtil {

    /**
     * An utility method for Immediately-invoked function expression.
     */
    public static <T> T iife(Supplier<T> supplier) {
        assertNotNull(supplier);
        return supplier.get();
    }

}
