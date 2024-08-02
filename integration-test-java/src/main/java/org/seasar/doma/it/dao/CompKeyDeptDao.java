package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.CompKeyDept;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface CompKeyDeptDao {

  @Select
  CompKeyDept selectByIds(Integer departmentId1, Integer departmentId2);

  @Insert(duplicateKeyType = DuplicateKeyType.UPDATE)
  Result<CompKeyDept> insertOnDuplicateKeyUpdate(CompKeyDept entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  BatchResult<CompKeyDept> insertOnDuplicateKeyUpdate(List<CompKeyDept> entities);

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  Result<CompKeyDept> insertOnDuplicateKeyIgnore(CompKeyDept entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  BatchResult<CompKeyDept> insertOnDuplicateKeyIgnore(List<CompKeyDept> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  MultiResult<CompKeyDept> insertMultiRowsOnDuplicateKeyUpdate(List<CompKeyDept> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  MultiResult<CompKeyDept> insertMultiRowsOnDuplicateKeyIgnore(List<CompKeyDept> entities);
}
