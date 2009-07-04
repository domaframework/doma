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
public abstract class AbstractStringDomain<D extends AbstractStringDomain<D>>
        extends AbstractComparableDomain<String, D> implements CharSequence,
        SerializableDomain<String, D> {

    private static final long serialVersionUID = 1L;

    public AbstractStringDomain() {
    }

    public AbstractStringDomain(String value) {
        super(value);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractStringDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractStringDomainVisitor<R, P, TH> v = AbstractStringDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractStringDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

    public boolean isEmpty() {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.isEmpty();
    }

    public boolean startsWith(String prefix) {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.startsWith(prefix);
    }

    public boolean endsWith(String suffix) {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.endsWith(suffix);
    }

    public int length() {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.length();
    }

    public boolean matches(String regex) {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.matches(regex);
    }

    public boolean contains(CharSequence s) {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.contains(s);
    }

    @Override
    public char charAt(int index) {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (value == null) {
            throw new DomainValueNullPointerException();
        }
        return value.subSequence(start, end);
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
        AbstractStringDomain<?> other = AbstractStringDomain.class.cast(o);
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
        value = String.class.cast(inputStream.readObject());
        changed = inputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream outputStream)
            throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeObject(value);
        outputStream.writeBoolean(changed);
    }
}
