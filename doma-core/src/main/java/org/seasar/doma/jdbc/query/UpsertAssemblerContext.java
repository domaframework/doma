package org.seasar.doma.jdbc.query;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * The UpsertAssemblerContext class represents the context for executing an upsert operation in a
 * database. It holds information about the entity to be upserted, the prepared SQL statement, the
 * entity type, the duplicate key type, naming and dialect information, and the values to be set for
 * the entity.
 */
public class UpsertAssemblerContext {
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
  public final List<? extends EntityPropertyType<?, ?>> keys;

  /** values clause property-parameter pair list */
  public final List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues;

  /** set clause property-value pair list */
  public final List<? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends UpsertSetValue>>
      setValues;

  public final boolean isKeysSpecified;

  /**
   * Constructs an instance of UpsertAssemblerContext with the specified prepared SQL builder,
   * entity
   *
   * @param buf the prepared SQL builder
   * @param entityType the entity type
   * @param duplicateKeyType the duplicate key type
   * @param naming the naming
   * @param dialect the dialect
   * @param keys the conflicting keys
   * @param insertValues the values clause property-parameter pair list
   * @param setValues the set clause property-value pair list(optional).Required in case of
   *     duplicateKeyType.UPDATE
   */
  public UpsertAssemblerContext(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<?, ?>> keys,
      List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues,
      List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {
    Objects.requireNonNull(buf);
    Objects.requireNonNull(entityType);
    Objects.requireNonNull(duplicateKeyType);
    Objects.requireNonNull(naming);
    Objects.requireNonNull(dialect);
    Objects.requireNonNull(keys);
    Objects.requireNonNull(insertValues);
    if (duplicateKeyType == DuplicateKeyType.EXCEPTION) {
      throw new DomaIllegalArgumentException(
          "duplicateKeyType",
          "The duplicateKeyType must not be set to EXCEPTION when performing an upsert.");
    }
    if (insertValues.isEmpty()) {
      throw new DomaIllegalArgumentException(
          "insertValues",
          "The insertValues must not be empty when performing an upsert. At least one insert value must be specified.");
    }
    this.buf = buf;
    this.entityType = entityType;
    this.duplicateKeyType = duplicateKeyType;
    this.naming = naming;
    this.dialect = dialect;
    this.isKeysSpecified = !keys.isEmpty();
    this.keys = resolveKeys(entityType, keys);
    this.insertValues = insertValues;
    this.setValues = resolveSetValues(duplicateKeyType, keys, insertValues, setValues);
  }

  private static List<? extends EntityPropertyType<?, ?>> resolveKeys(
      EntityType<?> entityType, List<? extends EntityPropertyType<?, ?>> keys) {
    if (!keys.isEmpty()) {
      return keys;
    }
    List<? extends EntityPropertyType<?, ?>> results = entityType.getIdPropertyTypes();
    if (results.isEmpty()) {
      throw new IllegalStateException(
          "The keys must not be empty when performing an upsert. At least one key must be resolved.");
    }
    return results;
  }

  private static List<
          ? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends UpsertSetValue>>
      resolveSetValues(
          DuplicateKeyType duplicateKeyType,
          List<? extends EntityPropertyType<?, ?>> keys,
          List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues,
          List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {
    if (!setValues.isEmpty()) {
      return setValues;
    }
    List<Tuple2<? extends EntityPropertyType<?, ?>, ? extends UpsertSetValue>> results =
        insertValues.stream()
            .map(Tuple2::component1)
            .filter(p -> !keys.contains(p))
            .filter(UpsertAssemblerContext::isUpdatePropertyTypes)
            .map(p -> new Tuple2<>(p, new UpsertSetValue.Prop(p)))
            .collect(Collectors.toList());
    if (duplicateKeyType == DuplicateKeyType.UPDATE && results.isEmpty()) {
      throw new IllegalStateException(
          "The setValues must not be empty when performing an upsert with the UPDATE duplicateKeyType. At least one set value must be resolved.");
    }
    return results;
  }

  private static boolean isUpdatePropertyTypes(EntityPropertyType<?, ?> property) {
    return property.isUpdatable() && !property.isId() && !property.isTenantId();
  }
}
