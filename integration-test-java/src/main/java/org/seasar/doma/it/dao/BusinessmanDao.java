package org.seasar.doma.it.dao;

import java.util.List;
import java.util.OptionalInt;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Businessman;

@Dao
public interface BusinessmanDao {

  @Select
  List<Businessman> selectAll();

  @Select
  Businessman selectById(OptionalInt id);

  @Select
  List<Businessman> selectByExample(Businessman businessman);

  @Insert
  int insert(Businessman entity);

  @Update
  int update(Businessman entity);

  @Delete
  int delete(Businessman entity);

  @BatchInsert
  int[] insert(List<Businessman> entity);

  @BatchUpdate
  int[] update(List<Businessman> entity);

  @BatchDelete
  int[] delete(List<Businessman> entity);
}
