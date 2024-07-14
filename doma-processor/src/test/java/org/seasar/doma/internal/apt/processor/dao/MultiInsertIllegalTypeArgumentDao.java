package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.MultiInsert;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface MultiInsertIllegalTypeArgumentDao {

  @MultiInsert
  int insert(List<String> entities);
}
