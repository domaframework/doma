package org.seasar.doma.it.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Department;

@Dao(config = ItConfig.class)
public interface DepartmentDao extends GenericDao<Department> {

    @Select
    Department selectById(IdDomain department_id);
}
