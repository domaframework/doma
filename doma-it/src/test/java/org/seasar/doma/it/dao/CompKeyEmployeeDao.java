package org.seasar.doma.it.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.CompKeyEmployee;

@Dao(config = ItConfig.class)
public interface CompKeyEmployeeDao extends GenericDao<CompKeyEmployee> {

    @Select
    CompKeyEmployee selectById(IdDomain employee_id1, IdDomain employee_id2);
}
