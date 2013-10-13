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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.wrapper.Holder;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractInOutParameter<BASIC, CONTAINER> implements
        InParameter<BASIC>, OutParameter<BASIC> {

    protected final Holder<BASIC, CONTAINER> holder;

    protected final Reference<CONTAINER> reference;

    public AbstractInOutParameter(Holder<BASIC, CONTAINER> holder,
            Reference<CONTAINER> reference) {
        assertNotNull(holder, reference);
        this.holder = holder;
        this.reference = reference;
        holder.set(reference.get());
    }

    @Override
    public CONTAINER getValue() {
        return holder.get();
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
        return holder.getWrapper();
    }

    @Override
    public void update() {
        reference.set(holder.get());
    }

}
