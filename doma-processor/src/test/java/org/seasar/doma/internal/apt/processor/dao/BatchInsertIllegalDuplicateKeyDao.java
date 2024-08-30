package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface BatchInsertIllegalDuplicateKeyDao {

  @MultiInsert(duplicateKeys = {"id", "unknown"})
  int[] insert(List<Emp> entities);
}
