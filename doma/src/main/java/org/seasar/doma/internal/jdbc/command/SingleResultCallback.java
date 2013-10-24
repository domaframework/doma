/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Supplier;

import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

/**
 * @author nakamura-to
 * 
 */
public class SingleResultCallback<TARGET> implements
        IterationCallback<TARGET, TARGET> {

    protected final Supplier<TARGET> supplier;

    /**
     * @param supplier
     */
    public SingleResultCallback() {
        this(() -> null);
    }

    /**
     * @param supplier
     */
    public SingleResultCallback(Supplier<TARGET> supplier) {
        assertNotNull(supplier);
        this.supplier = supplier;
    }

    @Override
    public TARGET defaultResult() {
        return supplier.get();
    }

    @Override
    public TARGET iterate(TARGET target, IterationContext context) {
        context.exit();
        return target;
    }

}
