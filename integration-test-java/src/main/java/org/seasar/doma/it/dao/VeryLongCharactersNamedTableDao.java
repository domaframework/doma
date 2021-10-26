package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.it.entity.VeryLongCharactersNamedTable;

@Dao
public interface VeryLongCharactersNamedTableDao {

  @Insert
  int insert(VeryLongCharactersNamedTable entity);

  @BatchInsert
  int[] insert(List<VeryLongCharactersNamedTable> entities);
}
