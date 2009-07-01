package org.seasar.doma.it.dao;

import java.util.List;

import org.seasar.doma.ArrayFactory;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.SalEmp;
import org.seasar.doma.jdbc.domain.ArrayDomain;

@Dao(config = ItConfig.class)
public interface SalEmpDao extends GenericDao<SalEmp> {

    @Select
    List<SalEmp> selectAll();

    @ArrayFactory(typeName = "integer")
    ArrayDomain<Integer> createIntegerArray(Integer[] elements);

    @ArrayFactory(typeName = "text")
    ArrayDomain<String[]> createString2DArray(String[][] elements);
}
