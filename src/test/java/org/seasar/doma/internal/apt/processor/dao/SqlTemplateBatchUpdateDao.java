package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Sql;

@Dao(config = MyConfig.class)
public interface SqlTemplateBatchUpdateDao {

  @Sql(useFile = true)
  @BatchUpdate(batchSize = 10)
  int[] update(List<String> names);
}
