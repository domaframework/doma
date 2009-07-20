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
import java.sql.Time;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractTimeDomain<D extends AbstractTimeDomain<D>>
        extends AbstractComparableDomain<Time, D> implements
        SerializableDomain<Time, D> {

    private static final long serialVersionUID = 1L;

    protected AbstractTimeDomain() {
        this(null);
    }

    protected AbstractTimeDomain(Time value) {
        super(Time.class, value);
    }

    @Override
    public Time get() {
        if (value == null) {
            return null;
        }
        return new Time(value.getTime());
    }

    @Override
    protected void setInternal(Time v) {
        if (v == null) {
            super.setInternal(v);
        } else {
            super.setInternal(new Time(v.getTime()));
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractTimeDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractTimeDomainVisitor<R, P, TH> v = AbstractTimeDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractTimeDomain(this, p);
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
        AbstractTimeDomain<?> other = AbstractTimeDomain.class.cast(o);
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
        valueClass = (Class<Time>) inputStream.readObject();
        value = Time.class.cast(inputStream.readObject());
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
