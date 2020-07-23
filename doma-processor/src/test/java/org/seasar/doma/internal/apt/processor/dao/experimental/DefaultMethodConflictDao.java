package org.seasar.doma.internal.apt.processor.dao.experimental;

import org.seasar.doma.Dao;
import org.seasar.doma.Sql;

@Dao
public interface DefaultMethodConflictDao {

  @Sql("")
  default int execute() {
    return 0;
  }
}
