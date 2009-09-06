package org.seasar.doma.it.dao;

import java.util.List;

import org.seasar.doma.ArrayFactory;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.domain.BuiltinArrayDomain;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.SalEmp;

@Dao(config = ItConfig.class)
public interface SalEmpDao {

    @Select
    List<SalEmp> selectAll();

    @Insert
    int insert(SalEmp entity);

    @Update
    int update(SalEmp entity);

    @ArrayFactory(typeName = "integer")
    BuiltinArrayDomain<Integer> createIntegerArray(Integer[] elements);

    @ArrayFactory(typeName = "text")
    BuiltinArrayDomain<String[]> createString2DArray(String[][] elements);
}
