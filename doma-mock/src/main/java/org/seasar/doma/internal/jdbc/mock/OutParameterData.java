package org.seasar.doma.internal.jdbc.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutParameterData {

  protected final List<Object> values = new ArrayList<>();

  public OutParameterData(Object... values) {
    this.values.addAll(Arrays.asList(values));
  }

  public Object get(int index) {
    return values.get(index - 1);
  }

  public int size() {
    return values.size();
  }
}
