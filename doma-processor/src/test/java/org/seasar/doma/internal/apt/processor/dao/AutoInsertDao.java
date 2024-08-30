package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface AutoInsertDao {

  @Insert
  int insert(Emp entity);

  @Insert(duplicateKeyType = DuplicateKeyType.UPDATE)
  int insertOnDuplicateKeyUpdate(Emp entity);

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertOnDuplicateKeyIgnore(Emp entity);

  @Insert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"name", "salary"})
  int insertOnDuplicateKeyUpdateWithDuplicateKeys(Emp entity);
}
