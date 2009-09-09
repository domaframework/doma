package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.internal.apt.domain.MyIntegerDomain;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao(config = MyConfig.class)
public interface SqlValidationDao {

    // fail
    @Select
    Emp bindVariableNotFound(BuiltinIntegerDomain id);

    // fail
    @Select
    Emp ifExpressionContainsUnknownVariable(BuiltinIntegerDomain id);

    // success
    @Select
    Emp methodAccess(BuiltinIntegerDomain id);

    // success
    @Select
    Emp customDomain(MyIntegerDomain id);

    // fail
    @Select
    Emp customDomainMethodTypeParameter(MyIntegerDomain id);

}
