package org.seasar.doma.internal.apt.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.entity.ImmutableEmp;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface IllegalBatchModifyImmutableEmpDao {

  @BatchInsert
  int batchInsert(List<ImmutableEmp> emp);
}
