package example.holder;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class _InternationalPhoneNumber
    extends AbstractHolderDesc<String, InternationalPhoneNumber> {

  private static final _InternationalPhoneNumber singleton = new _InternationalPhoneNumber();

  private _InternationalPhoneNumber() {
    super(() -> new org.seasar.doma.wrapper.StringWrapper());
  }

  @Override
  public InternationalPhoneNumber newHolder(String value) {
    return new InternationalPhoneNumber(value);
  }

  @Override
  public String getBasicValue(InternationalPhoneNumber holder) {
    return holder.getValue();
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<InternationalPhoneNumber> getHolderClass() {
    return InternationalPhoneNumber.class;
  }

  public static _InternationalPhoneNumber getSingletonInternal() {
    return singleton;
  }
}
