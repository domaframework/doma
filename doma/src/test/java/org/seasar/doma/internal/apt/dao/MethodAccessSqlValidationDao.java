package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao(config = MyConfig.class)
public interface MethodAccessSqlValidationDao {

    @Select
    Emp select(BuiltinIntegerDomain id);
}
