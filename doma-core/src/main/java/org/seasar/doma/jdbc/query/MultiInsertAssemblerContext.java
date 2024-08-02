package org.seasar.doma.jdbc.query;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * An assembler for multi insert statements.
 *
 * @param <ENTITY> the entity type
 */
public class MultiInsertAssemblerContext<ENTITY> {
  public final PreparedSqlBuilder buf;
  public final EntityType<ENTITY> entityType;
  public final Naming naming;
  public final Dialect dialect;
  public final List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes;
  public final List<ENTITY> entities;

  /**
   * Creates an instance.
   *
   * @param buf the SQL buffer
   * @param entityType the entity type
   * @param naming the naming convention
   * @param dialect the SQL dialect
   * @param insertPropertyTypes the property types that are targets for the insert
   * @param entities the entities
   */
  MultiInsertAssemblerContext(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      List<ENTITY> entities) {
    Objects.requireNonNull(buf);
    Objects.requireNonNull(entityType);
    Objects.requireNonNull(naming);
    Objects.requireNonNull(dialect);
    Objects.requireNonNull(insertPropertyTypes);
    Objects.requireNonNull(entities);
    if (entities.isEmpty()) {
      throw new DomaIllegalArgumentException(
          "entities", "The entities must not be empty when performing an insert.");
    }
    this.buf = buf;
    this.entityType = entityType;
    this.naming = naming;
    this.dialect = dialect;
    this.insertPropertyTypes = insertPropertyTypes;
    this.entities = entities;
  }
}
