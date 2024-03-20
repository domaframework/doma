package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface AutoInsertDao {

  @Insert
  int insert(Emp entity);

  @Insert(duplicateKeyType = DuplicateKeyType.UPDATE)
  int insertOnDuplicateKeyUpdate(Emp entity);

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertOnDuplicateKeyIgnore(Emp entity);
}
