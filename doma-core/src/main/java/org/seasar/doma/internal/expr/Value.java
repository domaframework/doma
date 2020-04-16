package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.util.AssertionUtil.*;

public class Value {

  protected final Class<?> type;

  protected final Object value;

  public Value(Class<?> type, Object value) {
    assertNotNull(type);
    this.type = type;
    this.value = value;
  }

  public Class<?> getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }
}
