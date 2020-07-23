package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.EmpDto;

@Dao(config = MyConfig.class)
public interface EmpDtoParameterDao {

  @Select
  String select(EmpDto dto);

  @Insert(sqlFile = true)
  int insert(EmpDto dto);

  @BatchInsert(sqlFile = true, batchSize = 10)
  int[] insert(List<EmpDto> dto);
}
