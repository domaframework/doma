package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.it.entity.SequenceStrategy;

@Dao
public interface SequenceStrategyDao {

  @Insert
  int insert(SequenceStrategy entity);

  @BatchInsert
  int[] insert(List<SequenceStrategy> entities);

  @MultiInsert
  int insertMultiRows(List<SequenceStrategy> entities);
}
