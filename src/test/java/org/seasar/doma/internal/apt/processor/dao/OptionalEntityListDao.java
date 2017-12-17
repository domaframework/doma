package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import example.entity.Emp;

/**
 * @author nakamura-to
 *
 */
@Dao(config = MyConfig.class)
public interface OptionalEntityListDao {

    @Select
    List<Optional<Emp>> selectAll();

}
