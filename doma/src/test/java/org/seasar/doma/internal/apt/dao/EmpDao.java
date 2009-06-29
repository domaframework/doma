package org.seasar.doma.internal.apt.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;

/**
 * 
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface EmpDao {

    @Select
    Emp selectById(IntegerDomain id, SelectOptions options);

    @Select
    List<Emp> selectByNameAndSalary(StringDomain name, BigDecimalDomain salary,
            SelectOptions options);

    @Insert
    int insert(Emp entity);

    @Update
    int update(Emp entity);

    @Delete
    int delete(Emp entity);
}
