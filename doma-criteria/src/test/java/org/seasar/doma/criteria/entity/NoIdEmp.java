package org.seasar.doma.criteria.entity;

import java.math.BigDecimal;
import org.seasar.doma.Entity;
import org.seasar.doma.Version;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class NoIdEmp {

  Integer id;

  String name;

  BigDecimal salary;

  @Version Integer version;
}
