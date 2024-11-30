package org.seasar.doma.jdbc.criteria.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;

public class Emp_ implements EntityMetamodel<Emp> {
  private final String qualifiedTableName;

  public Emp_() {
    this.qualifiedTableName = "";
  }

  public Emp_(String qualifiedTableName) {
    this.qualifiedTableName = java.util.Objects.requireNonNull(qualifiedTableName);
  }

  private final _Emp entityType = new _Emp();

  public final PropertyMetamodel<Integer> id =
      new DefaultPropertyMetamodel<>(Integer.class, entityType, "id");

  public final PropertyMetamodel<String> name =
      new DefaultPropertyMetamodel<>(String.class, entityType, "name");

  public final PropertyMetamodel<BigDecimal> salary =
      new DefaultPropertyMetamodel<>(BigDecimal.class, entityType, "salary");

  public final PropertyMetamodel<Integer> version =
      new DefaultPropertyMetamodel<>(Integer.class, entityType, "version");

  @Override
  public EntityType<Emp> asType() {
    return qualifiedTableName.isEmpty()
        ? entityType
        : new org.seasar.doma.jdbc.criteria.metamodel.EntityTypeProxy<>(
            entityType, qualifiedTableName);
  }

  @Override
  public List<PropertyMetamodel<?>> allPropertyMetamodels() {
    return Arrays.asList(id, name, salary, version);
  }
}
