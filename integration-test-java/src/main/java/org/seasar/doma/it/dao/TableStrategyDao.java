package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.it.entity.TableStrategy;

@Dao
public interface TableStrategyDao {

  @Insert
  int insert(TableStrategy entity);

  @BatchInsert
  int[] insert(List<TableStrategy> entities);

  @MultiInsert
  int insertMultiRows(List<TableStrategy> entities);
}
