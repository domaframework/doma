package org.seasar.doma.it.dao;

import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.CompKeyDepartment;

import doma.Dao;
import doma.Select;

@Dao(config = ItConfig.class)
public interface CompKeyDepartmentDao extends GenericDao<CompKeyDepartment> {

    @Select
    CompKeyDepartment selectById(IdDomain department_id1,
            IdDomain department_id2);
}
