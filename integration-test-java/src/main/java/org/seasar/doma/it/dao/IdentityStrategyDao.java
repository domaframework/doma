package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.IdentityStrategy;

@Dao
public interface IdentityStrategyDao {

  @Select
  List<IdentityStrategy> selectAll();

  @Insert
  int insert(IdentityStrategy entity);

  @BatchInsert
  int[] insert(List<IdentityStrategy> entities);

  @BatchInsert(ignoreGeneratedKeys = true)
  int[] insertWithoutRetrievingGeneratedKeys(List<IdentityStrategy> entities);
}
