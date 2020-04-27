package org.seasar.doma.internal.apt.processor.entitydesc;

import java.time.LocalDate;
import org.seasar.doma.def.DefaultPropertyDef;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.jdbc.entity.EntityType;

public class EmpInfo_ {

  public final PropertyDef<LocalDate> hiredate;

  public EmpInfo_(EntityType<?> entityType, String name) {
    hiredate = new DefaultPropertyDef<>(LocalDate.class, entityType, name + ".hiredate");
  }
}
