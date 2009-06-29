package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.entity.Emp;


/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface IllegalDaoE4019_update {

    @Update(sqlFile = true)
    int update(Emp emp);
}
