package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.entity.Emp;

import example.domain.PhoneNumber;

@Dao(config = MyConfig.class)
public interface DomainParameterDao {

    @Select
    Emp select(PhoneNumber phoneNumber);
}
