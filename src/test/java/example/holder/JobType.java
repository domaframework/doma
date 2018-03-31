package example.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class)
public class JobType {

  private final int value;

  public JobType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
