package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.Sql;

public abstract class AbstractCreateQuery<RESULT> extends AbstractQuery
    implements CreateQuery<RESULT> {

  @Override
  public int getQueryTimeout() {
    return -1;
  }

  @Override
  public Sql<?> getSql() {
    return null;
  }

  @Override
  public void complete() {}
}
