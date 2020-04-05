package example.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class)
public class JobType {

  private final int value;

  public JobType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
