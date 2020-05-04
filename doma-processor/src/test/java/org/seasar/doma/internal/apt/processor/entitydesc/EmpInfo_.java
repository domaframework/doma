package org.seasar.doma.internal.apt.processor.entitydesc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.seasar.doma.jdbc.criteria.def.DefaultPropertyDef;
import org.seasar.doma.jdbc.criteria.def.EmbeddableDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.entity.EntityType;

public class EmpInfo_ implements EmbeddableDef {

  public final PropertyDef<LocalDate> hiredate;

  public EmpInfo_(EntityType<?> entityType, String name) {
    hiredate = new DefaultPropertyDef<>(LocalDate.class, entityType, name + ".hiredate");
  }

  @Override
  public List<PropertyDef<?>> allPropertyDefs() {
    return Arrays.asList(hiredate);
  }
}
