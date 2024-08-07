package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface AutoMultiInsertDao {

  @MultiInsert
  int insert(List<Emp> entities);

  @MultiInsert(exclude = {"name"})
  int insertWithExclude(List<Emp> entities);

  @MultiInsert(exclude = {"salary"})
  int insertWithInclude(List<Emp> entities);

  @MultiInsert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"name", "salary"})
  int insertWithDuplicateKeys(List<Emp> entities);

  @MultiInsert(queryTimeout = 10)
  int insertWithQueryTimeout(List<Emp> entities);

  @MultiInsert(sqlLog = SqlLogType.NONE)
  int insertWithSqlLog(List<Emp> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertDuplicateKeyIgnore(List<Emp> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  int insertDuplicateKeyUpdate(List<Emp> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.EXCEPTION)
  int insertDuplicateKeyException(List<Emp> entities);

  @MultiInsert
  MultiResult<ImmutableEmp> insertImmutableEntities(List<ImmutableEmp> entities);
}
