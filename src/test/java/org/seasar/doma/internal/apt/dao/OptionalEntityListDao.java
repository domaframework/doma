package org.seasar.doma.internal.apt.dao;

import example.entity.Emp;
import java.util.List;
import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

/** @author nakamura-to */
@Dao(config = MyConfig.class)
public interface OptionalEntityListDao {

  @Select
  List<Optional<Emp>> selectAll();
}
