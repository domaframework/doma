package example.holder;

import org.seasar.doma.Holder;

@Holder(valueType = String.class)
public class InternationalPhoneNumber extends PhoneNumber {

  public InternationalPhoneNumber(String value) {
    super(value);
  }
}
