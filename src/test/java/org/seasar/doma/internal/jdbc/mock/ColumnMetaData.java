package org.seasar.doma.internal.jdbc.mock;

public class ColumnMetaData {

  protected final String label;

  public ColumnMetaData(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
