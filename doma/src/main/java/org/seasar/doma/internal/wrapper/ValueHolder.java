package org.seasar.doma.internal.wrapper;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public class ValueHolder<V> implements Holder<V, V> {

    protected final Wrapper<V> wrapper;

    protected final boolean primitive;

    public ValueHolder(Wrapper<V> wrapper, boolean primitive) {
        AssertionUtil.assertNotNull(wrapper);
        this.wrapper = wrapper;
        this.primitive = primitive;
    }

    @Override
    public V get() {
        V value = wrapper.get();
        if (value == null && primitive) {
            return wrapper.getDefault();
        }
        return value;
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
