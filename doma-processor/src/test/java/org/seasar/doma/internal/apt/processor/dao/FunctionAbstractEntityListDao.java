package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.internal.apt.processor.entity.AbstractEntity;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface FunctionAbstractEntityListDao {

  @Function
  List<AbstractEntity> execute();
}
