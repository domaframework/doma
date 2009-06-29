package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;


/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class, implementedBy = AbstractImplementedByDao.class)
public interface ImplementedByDao extends GenericDao<Emp> {

    @Insert
    int aaa(Emp entity);

    @Delete
    int bbb(Emp entity);

}
