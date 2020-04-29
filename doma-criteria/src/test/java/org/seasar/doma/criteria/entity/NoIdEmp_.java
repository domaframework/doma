package org.seasar.doma.criteria.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.seasar.doma.def.DefaultPropertyDef;
import org.seasar.doma.def.EntityDef;
import org.seasar.doma.def.PropertyDef;

public class NoIdEmp_ implements EntityDef<NoIdEmp> {

  private final _NoIdEmp entityType = new _NoIdEmp();

  public final PropertyDef<Integer> employeeId =
      new DefaultPropertyDef<>(Integer.class, entityType, "id");

  public final PropertyDef<String> employeeName =
      new DefaultPropertyDef<>(String.class, entityType, "name");

  public final PropertyDef<BigDecimal> managerId =
      new DefaultPropertyDef<>(BigDecimal.class, entityType, "salary");

  public final PropertyDef<Integer> version =
      new DefaultPropertyDef<>(Integer.class, entityType, "version");

  public _NoIdEmp asType() {
    return entityType;
  }

  @Override
  public List<PropertyDef<?>> allPropertyDefs() {
    return Arrays.asList(employeeId, employeeName, managerId, version);
  }
}
