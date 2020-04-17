package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.Out;
import org.seasar.doma.Procedure;
import org.seasar.doma.jdbc.Reference;

@Dao(config = MyConfig.class)
public interface AutoProcedureDao {

  @Procedure
  void executeProcedure(@In Integer arg1, @Out Reference<Integer> arg2);
}
