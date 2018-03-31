package example.holder;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class _PhoneNumber extends AbstractHolderDesc<String, PhoneNumber> {

  private static final _PhoneNumber singleton = new _PhoneNumber();

  private _PhoneNumber() {
    super(() -> new org.seasar.doma.wrapper.StringWrapper());
  }

  @Override
  public PhoneNumber newHolder(String value) {
    return new PhoneNumber(value);
  }

  @Override
  public String getBasicValue(PhoneNumber holder) {
    if (holder == null) {
      return null;
    }
    return holder.getValue();
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<PhoneNumber> getHolderClass() {
    return PhoneNumber.class;
  }

  public static _PhoneNumber getSingletonInternal() {
    return singleton;
  }
}
