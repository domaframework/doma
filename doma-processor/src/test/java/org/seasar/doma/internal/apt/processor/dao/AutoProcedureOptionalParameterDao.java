package org.seasar.doma.internal.apt.processor.dao;

import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.Out;
import org.seasar.doma.Procedure;
import org.seasar.doma.jdbc.Reference;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Dao(config = MyConfig.class)
public interface AutoProcedureOptionalParameterDao {

  @Procedure
  void executeProcedure(@In Optional<Integer> arg1, @Out Reference<Optional<Integer>> arg2);
}
