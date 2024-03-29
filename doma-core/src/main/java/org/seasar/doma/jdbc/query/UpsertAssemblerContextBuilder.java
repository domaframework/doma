package org.seasar.doma.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

public class UpsertAssemblerContextBuilder {
  @SuppressWarnings("unchecked")
  public static <ENTITY> UpsertAssemblerContext fromEntity(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> keys,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      ENTITY entity) {
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues =
        toInsertValues(insertPropertyTypes, entity);
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues =
        toSetValues(filterUpdatePropertyTypes(insertValues));

    return new UpsertAssemblerContext(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        (List<EntityPropertyType<?, ?>>) (Object) keys,
        insertValues,
        setValues);
  }

  private static List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> filterUpdatePropertyTypes(
      List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> propertyValuePairs) {
    return propertyValuePairs.stream()
        .filter(p -> isUpdatePropertyTypes(p.component1()))
        .collect(Collectors.toList());
  }

  private static boolean isUpdatePropertyTypes(EntityPropertyType<?, ?> property) {
    return property.isUpdatable() && !property.isId() && !property.isTenantId();
  }

  private static List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> toSetValues(
      List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> propertyValuePairs) {
    List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues = new ArrayList<>();
    for (Tuple2<EntityPropertyType<?, ?>, InParameter<?>> propertyValuePair : propertyValuePairs) {
      setValues.add(
          new Tuple2<>(
              propertyValuePair.component1(),
              new UpsertSetValue.Prop(propertyValuePair.component1())));
    }
    return setValues;
  }

  private static <ENTITY> List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> toInsertValues(
      List<EntityPropertyType<ENTITY, ?>> propertyTypes, ENTITY entity) {
    List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> entityPropertyTypeToInParameter =
        new ArrayList<>();
    for (EntityPropertyType<ENTITY, ?> propertyType : propertyTypes) {
      Property<ENTITY, ?> property = propertyType.createProperty();
      property.load(entity);
      entityPropertyTypeToInParameter.add(new Tuple2<>(propertyType, property.asInParameter()));
    }
    return entityPropertyTypeToInParameter;
  }
}
