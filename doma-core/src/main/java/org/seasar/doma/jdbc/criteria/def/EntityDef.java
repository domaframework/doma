package org.seasar.doma.jdbc.criteria.def;

import java.util.List;
import org.seasar.doma.jdbc.entity.EntityType;

public interface EntityDef<ENTITY> {

  EntityType<ENTITY> asType();

  List<PropertyDef<?>> allPropertyDefs();
}
