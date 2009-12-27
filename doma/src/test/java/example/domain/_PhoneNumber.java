package example.domain;

import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.domain.DomainWrapper;
import org.seasar.doma.wrapper.StringWrapper;

public class _PhoneNumber implements DomainType<String, PhoneNumber> {

    private static final _PhoneNumber singleton = new _PhoneNumber();

    private _PhoneNumber() {
    }

    @Override
    public PhoneNumber newDomain(String value) {
        return new PhoneNumber(value);
    }

    @Override
    public Class<PhoneNumber> getDomainClass() {
        return PhoneNumber.class;
    }

    @Override
    public Wrapper getWrapper(PhoneNumber domain) {
        return new Wrapper(domain);
    }

    public static _PhoneNumber get() {
        return singleton;
    }

    static class Wrapper extends StringWrapper implements
            DomainWrapper<String, PhoneNumber> {

        private PhoneNumber domain;

        public Wrapper(PhoneNumber domain) {
            if (domain == null) {
                this.domain = new PhoneNumber(null);
            } else {
                this.domain = domain;
            }
        }

        @Override
        public String get() {
            return domain.getValue();
        }

        @Override
        public void set(String value) {
            domain = new PhoneNumber(value);
        }

        @Override
        public PhoneNumber getDomain() {
            return domain;
        }

    }

}
