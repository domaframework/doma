package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.Procedure;
import org.seasar.doma.jdbc.Reference;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface ReferenceWildcardTypeParamDao {

  @Procedure
  void select(@In Reference<Height<?>> height);
}
