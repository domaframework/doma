package org.seasar.doma.jdbc.criteria.def;

import java.util.List;

public interface EmbeddableDef {
  List<PropertyDef<?>> allPropertyDefs();
}
