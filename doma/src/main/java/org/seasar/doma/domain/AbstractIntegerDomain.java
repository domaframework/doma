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

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractIntegerDomain<D extends AbstractIntegerDomain<D>>
        extends AbstractComparableDomain<Integer, D> implements
        NumberDomain<Integer, D>, SerializableDomain<Integer, D> {

    private static final long serialVersionUID = 1L;

    protected AbstractIntegerDomain() {
        this(null);
    }

    protected AbstractIntegerDomain(Integer value) {
        super(Integer.class, value);
    }

    @Override
    public void set(Number v) {
        setInternal(v.intValue());
    }

    @Override
    public void set(NumberDomain<Integer, D> other) {
        if (other == null) {
            throw new DomaIllegalArgumentException("other", other);
        }
        setInternal(other.get());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractIntegerDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractIntegerDomainVisitor<R, P, TH> v = AbstractIntegerDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractIntegerDomain(this, p);
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
        AbstractIntegerDomain<?> other = AbstractIntegerDomain.class.cast(o);
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

    private void readObject(ObjectInputStream inputStream) throws IOException,
            ClassNotFoundException {
        inputStream.defaultReadObject();
        value = Integer.class.cast(inputStream.readObject());
        changed = inputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream outputStream)
            throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeObject(value);
        outputStream.writeBoolean(changed);
    }
}
