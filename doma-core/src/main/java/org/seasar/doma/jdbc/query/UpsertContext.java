package org.seasar.doma.jdbc.query;

import java.util.List;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

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

  public UpsertContext(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      DuplicateKeyType duplicateKeyType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<?, ?>> keys,
      List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues,
      List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues) {
    if (duplicateKeyType == DuplicateKeyType.EXCEPTION) {
      throw new DomaIllegalArgumentException(
          "duplicateKeyType",
          "The duplicateKeyType must not be set to EXCEPTION when performing an upsert.");
    }
    this.buf = buf;
    this.entityType = entityType;
    this.duplicateKeyType = duplicateKeyType;
    this.naming = naming;
    this.dialect = dialect;
    this.keys = keys;
    this.insertValues = insertValues;
    this.setValues = setValues;
  }
}
