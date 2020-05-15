package org.seasar.doma.jdbc.criteria.entity;

import java.util.Arrays;
import java.util.List;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class Dept_ implements EntityMetamodel<Dept> {

  private final _Dept entityType = new _Dept();

  public final PropertyMetamodel<Integer> id =
      new DefaultPropertyMetamodel<>(Integer.class, entityType, "id");

  public final PropertyMetamodel<String> name =
      new DefaultPropertyMetamodel<>(String.class, entityType, "name");

  public _Dept asType() {
    return entityType;
  }

  @Override
  public List<PropertyMetamodel<?>> allPropertyMetamodels() {
    return Arrays.asList(id, name);
  }
}
