package org.seasar.doma.internal.apt.processor.dao;

import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;

/**
 * 
 * @author nakamura-to
 * 
 */
@Dao(config = MyConfig.class)
public interface OptionalParameterDao {

    @Select
    Emp selectById(Optional<Integer> id, SelectOptions options);
}
