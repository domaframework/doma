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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Float} を値の型とするドメインの骨格実装です。
 * 
 * @author taedium
 * 
 * @param <D>
 *            ドメインの型
 */
public abstract class FloatDomain<D extends FloatDomain<D>>
        extends AbstractComparableDomain<Float, D> implements
        NumberDomain<Float, D>, SerializableDomain<Float, D> {

    private static final long serialVersionUID = 1L;

    /**
     * デフォルトの値でインスタンス化します。
     */
    protected FloatDomain() {
        this(null);
    }

    /**
     * 値を指定してインスタンス化します。
     * 
     * @param value
     *            値
     */
    protected FloatDomain(Float value) {
        super(Float.class, value);
    }

    @Override
    public void set(Number v) {
        setInternal(v.floatValue());
    }

    @Override
    public void setDomain(NumberDomain<Float, D> other) {
        if (other == null) {
            throw new DomaNullPointerException("other");
        }
        setInternal(other.get());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (FloatDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            FloatDomainVisitor<R, P, TH> v = FloatDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractFloatDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        FloatDomain<?> other = FloatDomain.class.cast(o);
        if (value == null) {
            return other.value == null;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream inputStream) throws IOException,
            ClassNotFoundException {
        inputStream.defaultReadObject();
        valueClass = (Class<Float>) inputStream.readObject();
        value = Float.class.cast(inputStream.readObject());
        changed = inputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream outputStream)
            throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeObject(valueClass);
        outputStream.writeObject(value);
        outputStream.writeBoolean(changed);
    }

}
