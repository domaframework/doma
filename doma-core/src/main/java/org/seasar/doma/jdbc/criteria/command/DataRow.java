package org.seasar.doma.jdbc.criteria.command;

import java.util.List;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface DataRow {

  <ENTITY> ENTITY get(EntityMetamodel<ENTITY> entityEntityMetamodel);

  <ENTITY> ENTITY get(
      EntityMetamodel<ENTITY> entityEntityMetamodel, List<PropertyMetamodel<?>> propertyMetamodels);

  <PROPERTY> PROPERTY get(PropertyMetamodel<PROPERTY> propertyMetamodel);
}
