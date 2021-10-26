package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.it.entity.PrimitiveIdentityStrategy;

@Dao
public interface PrimitiveIdentityStrategyDao {

  @Insert
  int insert(PrimitiveIdentityStrategy entity);

  @BatchInsert
  int[] insert(List<PrimitiveIdentityStrategy> entities);
}
