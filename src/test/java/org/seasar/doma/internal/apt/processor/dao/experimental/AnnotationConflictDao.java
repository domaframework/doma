package org.seasar.doma.internal.apt.processor.dao.experimental;

import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.experimental.Sql;

@Dao
public interface AnnotationConflictDao {

  @Sql("")
  @Function
  int execute();
}
