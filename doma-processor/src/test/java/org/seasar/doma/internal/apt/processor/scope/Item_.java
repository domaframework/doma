package org.seasar.doma.internal.apt.processor.scope;

public final class Item_ implements org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel<Item> {

  private final String __qualifiedTableName;

  private final _Item __entityType = _Item.getSingletonInternal();

  private final java.util.List<org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel<?>>
      __allPropertyMetamodels;

  public final org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel<Long> id =
      new org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel<>(
          Long.class, __entityType, "id");

  public final org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel<String> name =
      new org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel<>(
          String.class, __entityType, "name");

  public Item_() {
    this("");
  }

  public Item_(String qualifiedTableName) {
    this.__qualifiedTableName = java.util.Objects.requireNonNull(qualifiedTableName);
    java.util.ArrayList<org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel<?>> __list =
        new java.util.ArrayList<>(7);
    __list.add(id);
    __list.add(name);
    __allPropertyMetamodels = java.util.Collections.unmodifiableList(__list);
  }

  @Override
  public org.seasar.doma.jdbc.entity.EntityType<Item> asType() {
    return __qualifiedTableName.isEmpty()
        ? __entityType
        : new org.seasar.doma.jdbc.criteria.metamodel.EntityTypeProxy<>(
            __entityType, __qualifiedTableName);
  }

  @Override
  public java.util.List<org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel<?>>
      allPropertyMetamodels() {
    return __allPropertyMetamodels;
  }
}
