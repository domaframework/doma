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

import java.lang.reflect.Method;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.Methods;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public abstract class AbstractDomain<V, D extends AbstractDomain<V, D>>
        implements Domain<V, D> {

    protected Class<?> valueClass;

    protected V value;

    protected boolean changed;

    protected AbstractDomain() {
    }

    protected AbstractDomain(Class<?> valueClass, V v) {
        if (valueClass == null) {
            throw new DomaIllegalArgumentException("valueClass", valueClass);
        }
        this.valueClass = valueClass;
        setInternal(v);
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public void set(V v) {
        setInternal(v);
    }

    @Override
    public void set(D other) {
        if (other == null) {
            throw new DomaIllegalArgumentException("other", other);
        }
        setInternal(other.get());
    }

    @Override
    public void setByReflection(Object value) {
        try {
            Method setter = Methods.getMethod(getClass(), "set", Object.class);
            Methods.invoke(setter, this, value);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new DomainException(MessageCode.DOMA1004, cause, cause);
        }
    }

    @Override
    public boolean isNull() {
        return value == null;
    }

    @Override
    public boolean isNotNull() {
        return value != null;
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public Class<?> getValueClass() {
        return valueClass;
    }

    public void setNull() {
        setInternal(null);
    }

    protected void setInternal(V v) {
        if (value == null) {
            if (v == null) {
                return;
            }
        } else {
            if (value.equals(v)) {
                return;
            }
        }
        value = v;
        changed = true;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }

}
