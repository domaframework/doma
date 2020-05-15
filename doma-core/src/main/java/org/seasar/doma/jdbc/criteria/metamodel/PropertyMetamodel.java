package org.seasar.doma.jdbc.criteria.metamodel;

import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface PropertyMetamodel<PROPERTY> {

  Class<?> asClass();

  EntityPropertyType<?, ?> asType();

  String getName();
}
