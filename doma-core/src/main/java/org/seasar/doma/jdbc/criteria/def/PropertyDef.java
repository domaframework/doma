package org.seasar.doma.jdbc.criteria.def;

import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface PropertyDef<PROPERTY> {

  Class<PROPERTY> asClass();

  EntityPropertyType<?, ?> asType();

  String getName();
}
