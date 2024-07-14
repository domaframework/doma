package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp;
import org.seasar.doma.jdbc.MultiResult;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface MultiInsertIllegalReturnTypeForImmutableEntityDao {

  @MultiInsert
  MultiResult<String> insert(List<ImmutableEmp> entities);
}
