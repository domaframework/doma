package org.seasar.doma.criteria.entity;

import org.seasar.doma.def.DefaultPropertyDef;
import org.seasar.doma.def.EntityDef;
import org.seasar.doma.def.PropertyDef;

public class Dept_ implements EntityDef<Dept> {

  private final _Dept entityType = new _Dept();

  public final PropertyDef<Integer> id = new DefaultPropertyDef<>(Integer.class, entityType, "id");

  public final PropertyDef<String> name =
      new DefaultPropertyDef<>(String.class, entityType, "name");

  public _Dept asType() {
    return entityType;
  }
}
