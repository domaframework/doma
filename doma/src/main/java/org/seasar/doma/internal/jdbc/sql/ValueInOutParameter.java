/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
public class ValueInOutParameter<T> implements InParameter, OutParameter {

    protected final Wrapper<T> wrapper;

    protected final Reference<T> reference;

    public ValueInOutParameter(Wrapper<T> wrapper, Reference<T> reference) {
        assertNotNull(wrapper, reference);
        wrapper.set(reference.get());
        this.wrapper = wrapper;
        this.reference = reference;
    }

    public Wrapper<T> getWrapper() {
        return wrapper;
    }

    public void updateReference() {
        reference.set(wrapper.get());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitValueInOutParameter(this, p);
    }

}
