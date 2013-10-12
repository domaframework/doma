package org.seasar.doma.jdbc.domain;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.internal.wrapper.Holder;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractDomainType<BASIC, DOMAIN> implements
        DomainType<BASIC, DOMAIN> {

    protected final Supplier<Wrapper<BASIC>> wrapperSupplier;

    protected AbstractDomainType(Supplier<Wrapper<BASIC>> wrapperSupplier) {
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

    protected class DomainHolder implements Holder<BASIC, DOMAIN> {

        Wrapper<BASIC> wrapper = wrapperSupplier.get();

        @Override
        public DOMAIN get() {
            return newDomain(wrapper.get());
        }

        @Override
        public DOMAIN getDefault() {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void set(Object domain) {
            BASIC value = getValue((DOMAIN) domain);
            wrapper.set(value);
        }

        @Override
        public Wrapper<BASIC> getWrapper() {
            return wrapper;
        }
    }

    protected class OptionalDomainHolder implements
            Holder<BASIC, Optional<DOMAIN>> {

        Wrapper<BASIC> wrapper = wrapperSupplier.get();

        @Override
        public Optional<DOMAIN> get() {
            BASIC value = wrapper.get();
            if (value == null) {
                return getDefaultInternal();
            }
            return Optional.of(newDomain(value));
        }

        @Override
        public Optional<DOMAIN> getDefault() {
            return getDefaultInternal();
        }

        protected Optional<DOMAIN> getDefaultInternal() {
            return Optional.empty();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void set(Object domain) {
            Optional<DOMAIN> optional = (Optional<DOMAIN>) domain;
            if (optional.isPresent()) {
                wrapper.set(getValue(optional.get()));
            } else {
                wrapper.set(null);
            }
        }

        @Override
        public Wrapper<BASIC> getWrapper() {
            return wrapper;
        }
    }

}
