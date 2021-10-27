package org.seasar.doma.it.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Script;
import org.seasar.doma.jdbc.SqlLogType;

@Dao
public interface ScriptDao {

  @Script(sqlLog = SqlLogType.NONE)
  void create();

  @Script(haltOnError = false, sqlLog = SqlLogType.NONE)
  void drop();
}
