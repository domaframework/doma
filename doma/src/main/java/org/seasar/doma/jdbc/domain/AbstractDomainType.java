package org.seasar.doma.jdbc.domain;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.internal.wrapper.Holder;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractDomainType<V, D> implements DomainType<V, D> {

    protected final Supplier<Wrapper<V>> wrapperSupplier;

    protected AbstractDomainType(Supplier<Wrapper<V>> wrapperSupplier) {
        AssertionUtil.assertNotNull(wrapperSupplier);
        this.wrapperSupplier = wrapperSupplier;
    }

    @Override
    public DomainHolder createDomainHolder() {
        return new DomainHolder();
    }

    @Override
    public OptionalDomainHolder createOptionalDomainHolder() {
        return new OptionalDomainHolder();
    }

    protected class DomainHolder implements Holder<V, D> {

        Wrapper<V> wrapper = wrapperSupplier.get();

        @Override
        public D get() {
            return newDomain(wrapper.get());
        }

        @SuppressWarnings("unchecked")
        @Override
        public void set(Object domain) {
            V value = getValue((D) domain);
            wrapper.set(value);
        }

        @Override
        public Wrapper<V> getWrapper() {
            return wrapper;
        }
    }

    protected class OptionalDomainHolder implements Holder<V, Optional<D>> {

        Wrapper<V> wrapper = wrapperSupplier.get();

        @Override
        public Optional<D> get() {
            V value = wrapper.get();
            return value == null ? Optional.empty() : Optional
                    .of(newDomain(value));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void set(Object domain) {
            Optional<D> optional = (Optional<D>) domain;
            if (optional.isPresent()) {
                wrapper.set(getValue(optional.get()));
            } else {
                wrapper.set(null);
            }
        }

        @Override
        public Wrapper<V> getWrapper() {
            return wrapper;
        }
    }

}
