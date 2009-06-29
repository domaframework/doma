package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;


/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface AutoInsertDao {

    @Insert
    int insert(Emp entity);
}
