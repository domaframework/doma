package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import example.entity.Emp;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface IllegalParameterNameDao {

    @Select
    Emp select(String __illegalName);
}
