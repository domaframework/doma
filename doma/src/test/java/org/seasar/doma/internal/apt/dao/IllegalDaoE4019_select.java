package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.domain.IntegerDomain;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface IllegalDaoE4019_select {

    @Select
    IntegerDomain select();
}
