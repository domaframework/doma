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
package org.seasar.doma.wrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@code byte[]} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class BytesWrapper extends AbstractWrapper<byte[]> implements
        Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します。
     */
    public BytesWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public BytesWrapper(byte[] value) {
        super(value);
    }

    @Override
    public BytesWrapper copy() {
        if (value == null) {
            return new BytesWrapper();
        }
        return new BytesWrapper(Arrays.copyOf(value, value.length));
    }

    @Override
    public boolean isEqual(Wrapper<?> other) {
        if (other == null) {
            throw new DomaNullPointerException("other");
        }
        if (other.get() instanceof byte[]) {
            byte[] otherValue = (byte[]) other.get();
            return Arrays.equals(value, otherValue);
        }
        return false;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (BytesWrapperVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            BytesWrapperVisitor<R, P, TH> v = (BytesWrapperVisitor) visitor;
            return v.visitBytesWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }

    private void readObject(ObjectInputStream inputStream) throws IOException,
            ClassNotFoundException {
        inputStream.defaultReadObject();
        value = (byte[]) inputStream.readObject();
    }

    private void writeObject(ObjectOutputStream outputStream)
            throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeObject(value);
    }

}
