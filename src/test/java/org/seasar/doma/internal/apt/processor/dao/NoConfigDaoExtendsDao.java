package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface NoConfigDaoExtendsDao extends NoConfigEmpDao {

    @Override
    @Select
    public Emp selectById(Integer id, SelectOptions options);

    @Update
    public int update2(Emp entity);
}
