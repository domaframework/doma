package org.seasar.doma.jdbc.domain;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.internal.wrapper.Scalar;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractDomainType<BASIC, DOMAIN> implements
        DomainType<BASIC, DOMAIN> {

    protected final Supplier<Wrapper<BASIC>> wrapperSupplier;

    protected AbstractDomainType(Supplier<Wrapper<BASIC>> wrapperSupplier) {
        AssertionUtil.assertNotNull(wrapperSupplier);
        this.wrapperSupplier = wrapperSupplier;
    }

    protected abstract DOMAIN newDomain(BASIC value);

    protected abstract BASIC getBasicValue(DOMAIN domain);

    @Override
    public DomainScalar createScalar() {
        return new DomainScalar();
    }

    @Override
    public OptionalDomainScalar createOptionalScalar() {
        return new OptionalDomainScalar();
    }

    protected class DomainScalar implements Scalar<BASIC, DOMAIN> {

        Wrapper<BASIC> wrapper = wrapperSupplier.get();

        @Override
        public Class<BASIC> getBasicClass() {
            return wrapper.getBasicClass();
        }

        @Override
        public Class<?> getDomainClass() {
            return AbstractDomainType.this.getDomainClass();
        }

        @Override
        public boolean isOptional() {
            return false;
        }

        @Override
        public DOMAIN cast(Object value) {
            return AbstractDomainType.this.getDomainClass().cast(value);
        }

        @Override
        public DOMAIN get() {
            return newDomain(wrapper.get());
        }

        @Override
        public DOMAIN getDefault() {
            return null;
        }

        @Override
        public void set(DOMAIN domain) {
            BASIC value = getBasicValue(domain);
            wrapper.set(value);
        }

        @Override
        public Wrapper<BASIC> getWrapper() {
            return wrapper;
        }
    }

    protected class OptionalDomainScalar implements
            Scalar<BASIC, Optional<DOMAIN>> {

        Wrapper<BASIC> wrapper = wrapperSupplier.get();

        @Override
        public Class<BASIC> getBasicClass() {
            return wrapper.getBasicClass();
        }

        @Override
        public Class<?> getDomainClass() {
            return AbstractDomainType.this.getDomainClass();
        }

        @Override
        public boolean isOptional() {
            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Optional<DOMAIN> cast(Object value) {
            return (Optional<DOMAIN>) value;
        }

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

        @Override
        public void set(Optional<DOMAIN> optional) {
            if (optional.isPresent()) {
                wrapper.set(getBasicValue(optional.get()));
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
