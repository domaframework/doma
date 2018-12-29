package org.seasar.doma.internal.apt.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.entity.AbstractEntity;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface ProcedureAbstractEntityListDao {

  @Procedure
  void execute(@ResultSet List<AbstractEntity> list);
}
