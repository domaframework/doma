package org.seasar.doma.jdbc.criteria.context;

public class ForUpdate {
  public boolean nowait;

  public ForUpdate(boolean nowait) {
    this.nowait = nowait;
  }
}
