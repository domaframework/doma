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
 * A wrapper for the {@link Byte} class.
 */
public class ByteWrapper extends AbstractWrapper<Byte> implements NumberWrapper<Byte> {

    public ByteWrapper() {
        super(Byte.class);
    }

    public ByteWrapper(Byte value) {
        super(Byte.class, value);
    }

    @Override
    public void set(Number v) {
        super.set(v.byteValue());
    }

    @Override
    public Byte getDefault() {
        return Byte.valueOf((byte) 0);
    }

    @Override
    public void increment() {
        Byte value = doGet();
        if (value != null) {
            doSet((byte) (value.byteValue() + 1));
        }
    }

    @Override
    public void decrement() {
        Byte value = doGet();
        if (value != null) {
            doSet((byte) (value.byteValue() - 1));
        }
    }

    @Override
    public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitByteWrapper(this, p, q);
    }
}
