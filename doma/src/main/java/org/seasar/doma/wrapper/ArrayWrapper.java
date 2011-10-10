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
package org.seasar.doma.wrapper;

import java.sql.Array;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Array} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class ArrayWrapper extends AbstractWrapper<Array> {

    /**
     * インスタンスを構築します。
     */
    public ArrayWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public ArrayWrapper(Array value) {
        super(value);
    }

    @Override
    protected Array doGetCopy() {
        return null;
    }

    @Override
    protected boolean doHasEqualValue(Object otherValue) {
        return false;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof ArrayWrapperVisitor<?, ?, ?>) {
            ArrayWrapperVisitor<R, P, TH> v = (ArrayWrapperVisitor<R, P, TH>) visitor;
            return v.visitArrayWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }

}
