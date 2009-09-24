package example.domain;

import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.domain.DomainTypeFactory;
import org.seasar.doma.wrapper.StringWrapper;

public class PhoneNumber_ implements DomainTypeFactory<PhoneNumber> {

    @Override
    public DomainType<PhoneNumber> createDomainType() {
        return new PhoneNumberType();
    }

    @Override
    public DomainType<PhoneNumber> createDomainType(PhoneNumber domain) {
        return new PhoneNumberType(domain);
    }

    private static class PhoneNumberType implements DomainType<PhoneNumber> {

        public PhoneNumberType() {
        }

        public PhoneNumberType(PhoneNumber domain) {
            super();
        }

        private StringWrapper wrapper;
    }

}
