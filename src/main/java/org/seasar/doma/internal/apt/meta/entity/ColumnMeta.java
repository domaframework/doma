package org.seasar.doma.internal.apt.meta.entity;

public class ColumnMeta {

  protected String name;

  protected boolean insertable = true;

  protected boolean updatable = true;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isInsertable() {
    return insertable;
  }

  public void setInsertable(boolean insertable) {
    this.insertable = insertable;
  }

  public boolean isUpdatable() {
    return updatable;
  }

  public void setUpdatable(boolean updatable) {
    this.updatable = updatable;
  }
}
