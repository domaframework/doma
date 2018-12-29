package org.seasar.doma.internal.jdbc.mock;

public class BindValue {

  protected final String typeName;

  protected final int index;

  protected final Object value;

  protected final Integer sqlType;

  public BindValue(String typeName, int index, Object value) {
    this.typeName = typeName;
    this.index = index;
    this.value = value;
    this.sqlType = null;
  }

  public BindValue(int index, int sqlType) {
    this.typeName = null;
    this.index = index;
    this.value = null;
    this.sqlType = sqlType;
  }

  public String getTypeName() {
    return typeName;
  }

  public int getIndex() {
    return index;
  }

  public Object getValue() {
    return value;
  }

  public Integer getSqlType() {
    return sqlType;
  }
}
