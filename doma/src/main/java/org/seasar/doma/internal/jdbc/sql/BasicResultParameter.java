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

import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class BasicResultParameter<V> implements ResultParameter<V> {

    protected final Wrapper<V> wrapper;

    protected final boolean primitveResult;

    public BasicResultParameter(Wrapper<V> wrapper, boolean primitveResult) {
        assertNotNull(wrapper);
        if (primitveResult) {
            assertNotNull(wrapper.getDefault());
        }
        this.wrapper = wrapper;
        this.primitveResult = primitveResult;
    }

    @Override
    public Object getValue() {
        return wrapper.get();
    }

    public Wrapper<V> getWrapper() {
        return wrapper;
    }

    @Override
    public V getResult() {
        V result = wrapper.get();
        if (result == null && primitveResult) {
            result = wrapper.getDefault();
            assertNotNull(result);
        }
        return result;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitBasicResultParameter(this, p);
    }

}
