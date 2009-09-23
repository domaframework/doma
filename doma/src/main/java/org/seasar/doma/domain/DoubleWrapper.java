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
package org.seasar.doma.domain;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Double} を値の型とするドメインの骨格実装です。
 * 
 * @author taedium
 * 
 */
public class DoubleWrapper extends AbstractWrapper<Double, DoubleWrapper>
        implements NumberWrapper<Double, DoubleWrapper> {

    public DoubleWrapper() {
    }

    public DoubleWrapper(Double value) {
        set(value);
    }

    @Override
    public void set(Number v) {
        super.set(v.doubleValue());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (DoubleWrapperVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            DoubleWrapperVisitor<R, P, TH> v = DoubleWrapperVisitor.class
                    .cast(visitor);
            return v.visitDoubleWrapper(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
