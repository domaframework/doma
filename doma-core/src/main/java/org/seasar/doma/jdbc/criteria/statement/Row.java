package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.def.PropertyDef;

public interface Row {

  <PROPERTY> PROPERTY get(PropertyDef<PROPERTY> propertyDef);
}
