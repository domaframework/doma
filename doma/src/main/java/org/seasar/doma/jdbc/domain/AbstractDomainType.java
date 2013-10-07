package org.seasar.doma.jdbc.domain;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractDomainType<V, D> implements DomainType<V, D> {

    protected final Supplier<Wrapper<V>> wrapperSupplier;

    protected AbstractDomainType(Supplier<Wrapper<V>> wrapperSupplier) {
        AssertionUtil.assertNotNull(wrapperSupplier);
        this.wrapperSupplier = wrapperSupplier;
    }

    @Override
    public DomainState<V, D> createState() {
        return new DomainState<V, D>() {

            Wrapper<V> wrapper = wrapperSupplier.get();

            @Override
            public D get() {
                return newDomain(wrapper.get());
            }

            @Override
            public void set(D domain) {
                V value = getValue(domain);
                wrapper.set(value);
            }

            @Override
            public Wrapper<V> getWrapper() {
                return wrapper;
            }
        };
    }

    @Override
    public OptionalDomainState<V, D> createOptionalState() {
        return new OptionalDomainState<V, D>() {

            Wrapper<V> wrapper = wrapperSupplier.get();

            @Override
            public Optional<D> get() {
                V value = wrapper.get();
                return value == null ? Optional.empty() : Optional
                        .of(newDomain(value));
            }

            @Override
            public void set(Optional<D> domain) {
                if (domain.isPresent()) {
                    wrapper.set(getValue(domain.get()));
                } else {
                    wrapper.set(null);
                }
            }

            @Override
            public Wrapper<V> getWrapper() {
                return wrapper;
            }

        };
    }
}
