package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;

import example.entity.Emp;

/**
 * 
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
interface PackagePrivateDao {

    @Update
    int update(Emp emp);

}
