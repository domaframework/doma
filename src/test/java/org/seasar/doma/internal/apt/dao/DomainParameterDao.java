package org.seasar.doma.internal.apt.dao;

import example.domain.PhoneNumber;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao(config = MyConfig.class)
public interface DomainParameterDao {

  @Select
  Emp select(PhoneNumber phoneNumber);
}
