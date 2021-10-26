package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.NoId;

@Dao
public interface NoIdDao {

  @Insert
  int insert(NoId entity);

  @Update
  int update(NoId entity);

  @Delete
  int delete(NoId entity);

  @BatchInsert
  int[] insert(List<NoId> entities);

  @BatchUpdate
  int[] update(List<NoId> entities);

  @BatchDelete
  int[] delete(List<NoId> entities);
}
