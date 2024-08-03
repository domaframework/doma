package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface MultiInsertIllegalDuplicateKeyDao {

  @MultiInsert(duplicateKeys = {"id", "unknown"})
  int insert(List<Emp> entities);
}
