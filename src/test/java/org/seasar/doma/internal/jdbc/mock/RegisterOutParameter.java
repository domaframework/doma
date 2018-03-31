package org.seasar.doma.internal.jdbc.mock;

/** @author taedium */
public class RegisterOutParameter {

  protected final int index;

  protected final int sqlType;

  public RegisterOutParameter(int index, int sqlType) {
    this.index = index;
    this.sqlType = sqlType;
  }

  public int getIndex() {
    return index;
  }

  public int getSqlType() {
    return sqlType;
  }
}
