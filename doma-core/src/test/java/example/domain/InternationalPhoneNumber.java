package example.domain;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class InternationalPhoneNumber extends PhoneNumber {

  public InternationalPhoneNumber(String value) {
    super(value);
  }
}
