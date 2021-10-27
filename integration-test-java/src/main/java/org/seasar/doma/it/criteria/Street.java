package org.seasar.doma.it.criteria;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class Street implements CharSequence {
  private final String value;

  public Street(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int index) {
    return value.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return value.subSequence(start, end);
  }
}
