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

import java.util.List;

import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class BasicListParameter<V> implements ListParameter<Wrapper<V>, V> {

    protected final Wrapper<V> wrapper;

    protected final List<V> values;

    public BasicListParameter(Wrapper<V> wrapper, List<V> values) {
        assertNotNull(wrapper, values);
        this.wrapper = wrapper;
        this.values = values;
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public Wrapper<V> getElementHolder() {
        return wrapper;
    }

    @Override
    public void add(V value) {
        values.add(value);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitBasicListParameter(this, p);
    }

}
