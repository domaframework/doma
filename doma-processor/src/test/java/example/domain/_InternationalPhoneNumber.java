package example.domain;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class _InternationalPhoneNumber
    extends AbstractDomainType<String, InternationalPhoneNumber> {

  private static final _InternationalPhoneNumber singleton = new _InternationalPhoneNumber();

  private _InternationalPhoneNumber() {
    super(() -> new org.seasar.doma.wrapper.StringWrapper());
  }

  @Override
  public InternationalPhoneNumber newDomain(String value) {
    return new InternationalPhoneNumber(value);
  }

  @Override
  public String getBasicValue(InternationalPhoneNumber domain) {
    return domain.getValue();
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<InternationalPhoneNumber> getDomainClass() {
    return InternationalPhoneNumber.class;
  }

  public static _InternationalPhoneNumber getSingletonInternal() {
    return singleton;
  }
}
