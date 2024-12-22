package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp2;

@Dao
public interface OptionalEntityListDao {

  @Select
  List<Optional<Emp2>> selectAll();
}
