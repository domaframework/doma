package org.seasar.doma.jdbc.criteria.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.seasar.doma.def.DefaultPropertyDef;
import org.seasar.doma.def.EntityDef;
import org.seasar.doma.def.PropertyDef;

public class Emp_ implements EntityDef<Emp> {

  private final _Emp entityType = new _Emp();

  public final PropertyDef<Integer> id = new DefaultPropertyDef<>(Integer.class, entityType, "id");

  public final PropertyDef<String> name =
      new DefaultPropertyDef<>(String.class, entityType, "name");

  public final PropertyDef<BigDecimal> salary =
      new DefaultPropertyDef<>(BigDecimal.class, entityType, "salary");

  public final PropertyDef<Integer> version =
      new DefaultPropertyDef<>(Integer.class, entityType, "version");

  public _Emp asType() {
    return entityType;
  }

  @Override
  public List<PropertyDef<?>> allPropertyDefs() {
    return Arrays.asList(id, name, salary, version);
  }
}
