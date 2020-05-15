package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface Row {

  <PROPERTY> PROPERTY get(PropertyMetamodel<PROPERTY> propertyMetamodel);
}
