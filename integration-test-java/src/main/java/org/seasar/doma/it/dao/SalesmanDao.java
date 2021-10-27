package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Salesman;

/** @author nakamura */
@Dao
public interface SalesmanDao {

  @Select
  Salesman selectById(Integer id);

  @Select
  List<Salesman> selectAll();

  @Update
  int update(Salesman salesman);

  @BatchUpdate
  int[] update(List<Salesman> salesman);

  @Delete
  int delete(Salesman salesman);

  @BatchDelete
  int[] delete(List<Salesman> salesman);
}
