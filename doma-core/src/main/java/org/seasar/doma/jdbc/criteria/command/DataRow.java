package org.seasar.doma.jdbc.criteria.command;

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface DataRow {

  <ENTITY> ENTITY get(EntityMetamodel<ENTITY> entityEntityMetamodel);

  <PROPERTY> PROPERTY get(PropertyMetamodel<PROPERTY> propertyMetamodel);
}
