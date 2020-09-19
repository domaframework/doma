package example.domain;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class PhoneNumber {

  private final String value;

  public PhoneNumber(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    PhoneNumber other = (PhoneNumber) obj;
    if (value == null) {
      return other.value == null;
    } else return value.equals(other.value);
  }
}
