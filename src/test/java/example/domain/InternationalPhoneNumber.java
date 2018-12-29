package example.domain;

import org.seasar.doma.Domain;

/** @author taedium */
@Domain(valueType = String.class)
public class InternationalPhoneNumber extends PhoneNumber {

  public InternationalPhoneNumber(String value) {
    super(value);
  }
}
