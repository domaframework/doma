package org.seasar.doma.def;

import org.seasar.doma.jdbc.entity.EntityType;

public interface EntityDef<ENTITY> {

  EntityType<ENTITY> asType();
}
