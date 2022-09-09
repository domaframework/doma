package org.seasar.doma.template;

import java.util.Objects;

/** Represents a SQL argument. */
public class SqlArgument {
  private final Class<?> type;
  private final Object value;

  /**
   * @param type the variable type. Must not be null.
   * @param value the value. Can be null.
   */
  public SqlArgument(Class<?> type, Object value) {
    this.type = Objects.requireNonNull(type);
    this.value = value;
  }

  /**
   * Returns the value type.
   *
   * @return the value type. Must not be null.
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Returns the value.
   *
   * @return the value. Can be null.
   */
  public Object getValue() {
    return value;
  }
}
