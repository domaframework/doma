package org.seasar.doma.internal.wrapper;

import java.util.Optional;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalValueHolder<V> implements Holder<V, Optional<V>> {

    protected final Wrapper<V> wrapper;

    public OptionalValueHolder(Wrapper<V> wrapper) {
        AssertionUtil.assertNotNull(wrapper);
        this.wrapper = wrapper;
    }

    @Override
    public Optional<V> get() {
        return Optional.ofNullable(wrapper.get());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(Object container) {
        Optional<V> optional = (Optional<V>) container;
        wrapper.set(optional.orElse(null));
    }

    @Override
    public Wrapper<V> getWrapper() {
        return wrapper;
    }
}
