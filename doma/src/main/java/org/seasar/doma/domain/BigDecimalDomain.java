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
import java.math.BigDecimal;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link BigDecimal} を値の型とするドメインの骨格実装です。
 * 
 * @author taedium
 * 
 * @param <D>
 *            ドメインの型
 */
public abstract class BigDecimalDomain<D extends BigDecimalDomain<D>>
        extends AbstractComparableDomain<BigDecimal, D> implements
        NumberDomain<BigDecimal, D>, SerializableDomain<BigDecimal, D> {

    private static final long serialVersionUID = 1L;

    /**
     * デフォルトの値でインスタンス化します。
     */
    protected BigDecimalDomain() {
        this(null);
    }

    /**
     * 値を指定してインスタンス化します。
     * 
     * @param value
     *            値
     */
    protected BigDecimalDomain(BigDecimal value) {
        super(BigDecimal.class, value);
    }

    @Override
    public void set(Number v) {
        setInternal(new BigDecimal(v.doubleValue()));
    }

    @Override
    public void setDomain(NumberDomain<BigDecimal, D> other) {
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
        if (BigDecimalDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            BigDecimalDomainVisitor<R, P, TH> v = BigDecimalDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractBigDecimalDomain(this, p);
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
        BigDecimalDomain<?> other = BigDecimalDomain.class
                .cast(o);
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
        return value != null ? value.toPlainString() : null;
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream inputStream) throws IOException,
            ClassNotFoundException {
        inputStream.defaultReadObject();
        valueClass = (Class<BigDecimal>) inputStream.readObject();
        value = BigDecimal.class.cast(inputStream.readObject());
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
