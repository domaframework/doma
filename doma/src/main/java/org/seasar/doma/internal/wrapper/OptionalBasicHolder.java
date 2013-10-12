package org.seasar.doma.internal.wrapper;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicHolder<V> implements Holder<V, Optional<V>> {

    protected final Wrapper<V> wrapper;

    public OptionalBasicHolder(Supplier<Wrapper<V>> supplier) {
        AssertionUtil.assertNotNull(supplier);
        this.wrapper = supplier.get();
        AssertionUtil.assertNotNull(wrapper);
    }

    @Override
    public Optional<V> get() {
        return Optional.ofNullable(wrapper.get());
    }

    @Override
    public Optional<V> getDefault() {
        return Optional.empty();
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
