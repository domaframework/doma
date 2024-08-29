package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;

@Dao
public interface SqlFileBatchUpdateDao {

  @BatchUpdate(sqlFile = true, batchSize = 10)
  int[] update(List<String> names);
}
