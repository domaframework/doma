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

/**
 * The UpsertContext class represents the context for executing an upsert operation in a database.
 * It holds information about the entity to be upserted, the prepared SQL statement, the entity
 * type, the duplicate key type, naming and dialect information, and the values to be set for the
 * entity.
 */
public class UpsertContext {
  public final PreparedSqlBuilder buf;
  public final EntityType<?> entityType;
  public final DuplicateKeyType duplicateKeyType;
  public final Naming naming;
  public final Dialect dialect;

  /**
   * conflicting keys
   *
   * @see EntityPropertyType
   */
  public final List<EntityPropertyType<?, ?>> keys;

  /** values clause property-parameter pair list */
  public final List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues;

  /** set clause property-value pair list */
  public final List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues;

  @SuppressWarnings("unchecked")
  public static <ENTITY> UpsertContext fromEntity(
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

    return new UpsertContext(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        (List<EntityPropertyType<?, ?>>) (Object) keys,
        insertValues,
        setValues);
  }

  public UpsertContext(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<?, ?>> keys,
      List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues,
      List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {
    this.buf = buf;
    this.entityType = entityType;
    this.duplicateKeyType = duplicateKeyType;
    this.naming = naming;
    this.dialect = dialect;
    this.keys = keys;
    this.insertValues = insertValues;
    this.setValues = setValues;
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
