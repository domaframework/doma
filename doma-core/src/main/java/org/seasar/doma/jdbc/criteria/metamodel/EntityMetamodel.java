package org.seasar.doma.jdbc.criteria.metamodel;

import java.util.List;
import org.seasar.doma.jdbc.entity.EntityType;

public interface EntityMetamodel<ENTITY> {

  EntityType<ENTITY> asType();

  List<PropertyMetamodel<?>> allPropertyMetamodels();
}
