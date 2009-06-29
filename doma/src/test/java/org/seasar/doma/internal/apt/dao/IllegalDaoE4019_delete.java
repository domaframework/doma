package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.internal.apt.entity.Emp;


/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface IllegalDaoE4019_delete {

    @Delete(sqlFile = true)
    int delete(Emp emp);
}
