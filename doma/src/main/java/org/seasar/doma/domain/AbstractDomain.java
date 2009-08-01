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

import org.seasar.doma.DomaNullPointerException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractDomain<V, D extends AbstractDomain<V, D>>
        implements Domain<V, D> {

    protected Class<V> valueClass;

    protected V value;

    protected boolean changed;

    protected AbstractDomain() {
    }

    protected AbstractDomain(Class<V> valueClass, V v) {
        if (valueClass == null) {
            throw new DomaNullPointerException("valueClass");
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
    public void setDomain(D other) {
        if (other == null) {
            throw new DomaNullPointerException("other");
        }
        setInternal(other.get());
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
    public Class<V> getValueClass() {
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
