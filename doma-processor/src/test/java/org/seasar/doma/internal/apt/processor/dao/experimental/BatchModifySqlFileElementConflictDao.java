package org.seasar.doma.internal.apt.processor.dao.experimental;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Sql;

@Dao
public interface BatchModifySqlFileElementConflictDao {

  @Sql("insert into emp (name) values (/* names */'')")
  @BatchInsert(sqlFile = true)
  int[] insert(List<String> names);
}
