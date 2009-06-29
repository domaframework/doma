package org.seasar.doma.it.dao;

import java.util.List;

import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.Emp;

import doma.Dao;
import doma.Delete;
import doma.Insert;
import doma.Select;
import doma.Update;
import doma.domain.IntegerDomain;

/**
 * @author taedium
 * 
 */
@Dao(config = ItConfig.class)
public interface EmpDao {

    @Select
    List<Emp> selectAll();

    @Select
    Emp selectById(IntegerDomain id);

    @Update(sqlFile = true)
    int createTable();

    @Insert
    int insert(Emp e);

    @Update
    int update(Emp e);

    @Delete
    int delete(Emp e);

}
