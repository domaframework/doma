package org.seasar.doma.internal.apt.processor.dao;

import java.sql.Array;
import org.seasar.doma.ArrayFactory;
import org.seasar.doma.Dao;

@Dao
public interface ArrayFactoryDao {

  @ArrayFactory(typeName = "varchar")
  Array create(String[] elements);
}
