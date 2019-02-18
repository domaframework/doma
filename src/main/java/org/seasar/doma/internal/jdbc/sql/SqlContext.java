package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.InParameter;

public interface SqlContext {

  <BASIC> void appendParameter(InParameter<BASIC> parameter);

  void appendSql(String sql);

  void cutBackSql(int length);
}
