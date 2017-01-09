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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;

import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 
 * @author nakamura-to
 * 
 * @param <BASIC>
 *            基本型
 * @param <CONTAINER>
 *            基本型のコンテナとなる型
 */
public class ScalarInParameter<BASIC, CONTAINER> implements InParameter<BASIC> {

    protected final Scalar<BASIC, CONTAINER> scalar;

    public ScalarInParameter(Scalar<BASIC, CONTAINER> holder) {
        assertNotNull(holder);
        this.scalar = holder;
    }

    public ScalarInParameter(Scalar<BASIC, CONTAINER> holder, CONTAINER value) {
        assertNotNull(holder);
        this.scalar = holder;
        holder.set(value);
    }

    @Override
    public CONTAINER getValue() {
        return scalar.get();
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
        return scalar.getWrapper();
    }

    @Override
    public Optional<Class<?>> getDomainClass() {
        return scalar.getDomainClass();
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            SqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitInParameter(this, p);
    }
}
