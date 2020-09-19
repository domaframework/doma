package example.domain;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.StringWrapper;

public class _PhoneNumber extends AbstractDomainType<String, PhoneNumber> {

  private static final _PhoneNumber singleton = new _PhoneNumber();

  private _PhoneNumber() {
    super(StringWrapper::new);
  }

  @Override
  public PhoneNumber newDomain(String value) {
    return new PhoneNumber(value);
  }

  @Override
  public String getBasicValue(PhoneNumber domain) {
    if (domain == null) {
      return null;
    }
    return domain.getValue();
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<PhoneNumber> getDomainClass() {
    return PhoneNumber.class;
  }

  public static _PhoneNumber getSingletonInternal() {
    return singleton;
  }
}
