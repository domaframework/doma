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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.query.SelectQuery;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class OptionalBasicIterationHandler<RESULT, BASIC> extends
        AbstractIterationHandler<RESULT, Optional<BASIC>> {

    protected final Supplier<Wrapper<BASIC>> supplier;

    public OptionalBasicIterationHandler(Supplier<Wrapper<BASIC>> supplier,
            IterationCallback<RESULT, Optional<BASIC>> iterationCallback) {
        super(iterationCallback);
        assertNotNull(supplier);
        this.supplier = supplier;
    }

    @Override
    protected ResultProvider<Optional<BASIC>> createResultProvider(SelectQuery query) {
        return new BasicResultProvider<BASIC, Optional<BASIC>>(
                () -> new org.seasar.doma.internal.wrapper.OptionalBasicHolder<>(
                        supplier), query);
    }

}
