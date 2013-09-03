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

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Float} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class FloatWrapper extends AbstractWrapper<Float> implements
        NumberWrapper<Float> {

    /**
     * インスタンスを構築します。
     */
    public FloatWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public FloatWrapper(Float value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        super.set(v.floatValue());
    }

    @Override
    public Float getDefault() {
        return Float.valueOf(0f);
    }

    @Override
    public void increment() {
        Float value = getIncrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public void decrement() {
        Float value = getDecrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public Float getIncrementedValue() {
        Float value = doGet();
        if (value != null) {
            return Float.valueOf((value.floatValue() + 1f));
        }
        return null;
    }

    @Override
    public Float getDecrementedValue() {
        Float value = doGet();
        if (value != null) {
            return Float.valueOf(value.floatValue() - 1f);
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof FloatWrapperVisitor<?, ?, ?>) {
            FloatWrapperVisitor<R, P, TH> v = (FloatWrapperVisitor<R, P, TH>) visitor;
            return v.visitFloatWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }
}
