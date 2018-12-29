package org.seasar.doma.jdbc;

public interface OutParameter<BASIC> extends SqlParameter, JdbcMappable<BASIC> {

  void updateReference();
}
