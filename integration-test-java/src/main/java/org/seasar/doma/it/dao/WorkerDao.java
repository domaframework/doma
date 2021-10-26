package org.seasar.doma.it.dao;

import java.util.List;
import java.util.Optional;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Worker;

@Dao
public interface WorkerDao {

  @Select
  List<Worker> selectAll();

  @Select
  Worker selectById(Optional<Integer> id);

  @Select
  List<Worker> selectByExample(Worker worker);

  @Insert
  int insert(Worker entity);

  @Update
  int update(Worker entity);

  @Delete
  int delete(Worker entity);

  @BatchInsert
  int[] insert(List<Worker> entity);

  @BatchUpdate
  int[] update(List<Worker> entity);

  @BatchDelete
  int[] delete(List<Worker> entity);
}
