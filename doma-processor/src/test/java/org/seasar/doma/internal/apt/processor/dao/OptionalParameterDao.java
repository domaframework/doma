package org.seasar.doma.internal.apt.processor.dao;

import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.jdbc.SelectOptions;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
@Dao
public interface OptionalParameterDao {

  @Select
  Emp2 selectById(Optional<Integer> id, SelectOptions options);
}
