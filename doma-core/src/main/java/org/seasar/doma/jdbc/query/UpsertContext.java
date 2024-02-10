package org.seasar.doma.jdbc.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
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

  /** inserting properties */
  public final List<EntityPropertyType<?, ?>> insertPropertyTypes;

  /** Updating properties. */
  public final List<EntityPropertyType<?, ?>> updatePropertyTypes;

  /** The values to be set for the entity. */
  public final Map<EntityPropertyType<?, ?>, InParameter<?>> propertyValuePairs;

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
    List<EntityPropertyType<?, ?>> castInsertPropertyTypes =
        (List<EntityPropertyType<?, ?>>) (Object) insertPropertyTypes;
    return new UpsertContext(
        buf,
        entityType,
        duplicateKeyType,
        naming,
        dialect,
        (List<EntityPropertyType<?, ?>>) (Object) keys,
        castInsertPropertyTypes,
        filterUpdatePropertyTypes(castInsertPropertyTypes),
        toPropertyValuePairs(entityType, entity));
  }

  public UpsertContext(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<?, ?>> keys,
      List<EntityPropertyType<?, ?>> insertPropertyTypes,
      List<EntityPropertyType<?, ?>> updatePropertyTypes,
      Map<EntityPropertyType<?, ?>, InParameter<?>> propertyValuePairs) {
    this.buf = buf;
    this.entityType = entityType;
    this.duplicateKeyType = duplicateKeyType;
    this.naming = naming;
    this.dialect = dialect;
    this.keys = keys;
    this.insertPropertyTypes = insertPropertyTypes;
    this.updatePropertyTypes = updatePropertyTypes;
    this.propertyValuePairs = propertyValuePairs;
  }

  private static List<EntityPropertyType<?, ?>> filterUpdatePropertyTypes(
      List<EntityPropertyType<?, ?>> propertyTypes) {
    return propertyTypes.stream()
        .filter(UpsertContext::isUpdatePropertyTypes)
        .collect(Collectors.toList());
  }

  private static boolean isUpdatePropertyTypes(EntityPropertyType<?, ?> property) {
    return property.isUpdatable() && !property.isId() && !property.isTenantId();
  }

  private static <ENTITY> Map<EntityPropertyType<?, ?>, InParameter<?>> toPropertyValuePairs(
      EntityType<ENTITY> entityType, ENTITY entity) {
    Map<EntityPropertyType<?, ?>, InParameter<?>> entityPropertyTypeToInParameter = new HashMap<>();
    for (EntityPropertyType<ENTITY, ?> propertyType : entityType.getEntityPropertyTypes()) {
      Property<ENTITY, ?> property = propertyType.createProperty();
      property.load(entity);
      entityPropertyTypeToInParameter.put(propertyType, property.asInParameter());
    }
    return entityPropertyTypeToInParameter;
  }
}
