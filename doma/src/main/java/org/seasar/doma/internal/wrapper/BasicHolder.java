package org.seasar.doma.internal.wrapper;

import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public class BasicHolder<V> implements Holder<V, V> {

    protected final Wrapper<V> wrapper;

    protected final boolean primitive;

    public BasicHolder(Supplier<Wrapper<V>> supplier, boolean primitive) {
        AssertionUtil.assertNotNull(supplier);
        this.wrapper = supplier.get();
        AssertionUtil.assertNotNull(this.wrapper);
        this.primitive = primitive;
    }

    @Override
    public V get() {
        V value = wrapper.get();
        if (value == null) {
            return getDefaultInternal();
        }
        return value;
    }

    @Override
    public V getDefault() {
        return getDefaultInternal();
    }

    protected V getDefaultInternal() {
        if (primitive) {
            return wrapper.getDefault();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(Object value) {
        V v = (V) value;
        if (v == null && primitive) {
            v = wrapper.getDefault();
        }
        wrapper.set(v);
    }

    @Override
    public Wrapper<V> getWrapper() {
        return wrapper;
    }

}
