package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp;

@Dao
public interface IllegalBatchModifyImmutableEmpDao {

  @BatchInsert
  int batchInsert(List<ImmutableEmp> emp);
}
