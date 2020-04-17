package org.seasar.doma.internal.apt.processor.dao.experimental;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.experimental.Sql;

@Dao
public interface ModifySqlFileElementConflictDao {

  @Sql("insert into emp (name) values (/* name */'')")
  @Insert(sqlFile = true)
  int insert(String name);
}
