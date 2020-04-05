package org.seasar.doma.internal.jdbc.mock;

import java.util.ArrayList;
import java.util.List;

public class OutParameterData {

  protected final List<Object> values = new ArrayList<Object>();

  public OutParameterData(Object... values) {
    for (Object object : values) {
      this.values.add(object);
    }
  }

  public Object get(int index) {
    return values.get(index - 1);
  }

  public int size() {
    return values.size();
  }
}
