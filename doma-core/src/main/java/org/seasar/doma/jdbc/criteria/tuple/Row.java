package org.seasar.doma.jdbc.criteria.tuple;

import java.util.Collection;
import java.util.Set;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface Row {

  boolean containsKey(PropertyMetamodel<?> key);

  <PROPERTY> PROPERTY get(PropertyMetamodel<PROPERTY> key);

  Set<PropertyMetamodel<?>> keySet();

  Collection<Object> values();

  int size();
}
