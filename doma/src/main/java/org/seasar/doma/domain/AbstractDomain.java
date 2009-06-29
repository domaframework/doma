package org.seasar.doma.domain;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractDomain<V, D extends AbstractDomain<V, D>>
        implements Domain<V, D> {

    protected V value;

    protected boolean changed;

    public AbstractDomain(V v) {
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
    public boolean isNull() {
        return value == null;
    }

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
