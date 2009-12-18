package example.domain;

import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.domain.DomainTypeFactory;
import org.seasar.doma.wrapper.StringWrapper;

public class _PhoneNumber implements DomainTypeFactory<String, PhoneNumber> {

    @Override
    public DomainType<String, PhoneNumber> createDomainType() {
        return new PhoneNumberType();
    }

    @Override
    public DomainType<String, PhoneNumber> createDomainType(PhoneNumber domain) {
        return new PhoneNumberType(domain);
    }

    private static class PhoneNumberType implements
            DomainType<String, PhoneNumber> {

        private final StringWrapper wrapper = new StringWrapper();

        private PhoneNumberType() {
        }

        private PhoneNumberType(PhoneNumber domain) {
            this.wrapper.set(domain != null ? domain.getValue() : null);
        }

        @Override
        public PhoneNumber getDomain() {
            return new PhoneNumber(wrapper.get());
        }

        @Override
        public Class<PhoneNumber> getDomainClass() {
            return PhoneNumber.class;
        }

        @Override
        public StringWrapper getWrapper() {
            return wrapper;
        }

    }

}
