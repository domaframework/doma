package org.seasar.doma.def;

import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface PropertyDef<PROPERTY> {

  Class<PROPERTY> asClass();

  EntityPropertyType<?, ?> asType();

  String getName();
}
