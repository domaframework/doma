package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp;
import org.seasar.doma.jdbc.BatchResult;

@Dao
public interface IgnoreGeneratedKeysDao {

  @BatchInsert(ignoreGeneratedKeys = true)
  int[] insert(List<Emp2> employees);

  @BatchInsert(ignoreGeneratedKeys = true)
  BatchResult<ImmutableEmp> batchInsert(List<ImmutableEmp> employees);
}
