package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.IdentityStrategy2;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface IdentityStrategy2Dao {

  @Select
  List<IdentityStrategy2> selectAll();

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertOrIgnore(IdentityStrategy2 entity);

  @Insert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"uniqueValue"})
  int insertOrUpdate(IdentityStrategy2 entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int[] insertOrIgnore(List<IdentityStrategy2> entities);

  @BatchInsert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"uniqueValue"})
  int[] insertOrUpdate(List<IdentityStrategy2> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertOrIgnoreMultiRows(List<IdentityStrategy2> entities);

  @MultiInsert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"uniqueValue"})
  int insertOrUpdateMultiRows(List<IdentityStrategy2> entities);
}
