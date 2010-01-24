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

import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class BasicInOutParameter<V> implements InParameter, OutParameter<V> {

    protected final Wrapper<V> wrapper;

    protected final Reference<V> reference;

    public BasicInOutParameter(Wrapper<V> wrapper, Reference<V> reference) {
        assertNotNull(wrapper, reference);
        wrapper.set(reference.get());
        this.wrapper = wrapper;
        this.reference = reference;
    }

    @Override
    public V getValue() {
        return wrapper.get();
    }

    @Override
    public Wrapper<V> getWrapper() {
        return wrapper;
    }

    @Override
    public void update() {
        reference.set(wrapper.get());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitBasicInOutParameter(this, p);
    }

}
