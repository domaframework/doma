package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface SqlFileBatchUpdateEntityDao {

  @BatchUpdate(sqlFile = true, batchSize = 10)
  int[] update(List<Emp> entities);
}
