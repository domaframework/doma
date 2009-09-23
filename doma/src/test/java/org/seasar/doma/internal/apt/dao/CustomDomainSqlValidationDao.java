package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.wrapper.IntegerWrapper;

@Dao(config = MyConfig.class)
public interface CustomDomainSqlValidationDao {

    @Select
    Emp select(IntegerWrapper id);
}
