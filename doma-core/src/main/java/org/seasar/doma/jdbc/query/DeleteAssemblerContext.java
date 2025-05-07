/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * An assembler for delete statements.
 *
 * @param <ENTITY> the entity type
 */
public class DeleteAssemblerContext<ENTITY> {
  /** The SQL builder used to construct the DELETE statement. */
  public final PreparedSqlBuilder buf;
  /** The entity type that represents the table to delete from. */
  public final EntityType<ENTITY> entityType;
  /** The naming convention used for converting Java names to database names. */
  public final Naming naming;
  /** The dialect that defines database-specific behaviors. */
  public final Dialect dialect;
  /** The list of property types that represent the primary key. */
  public final List<EntityPropertyType<ENTITY, ?>> idPropertyTypes;
  /** The property type that represents the version column for optimistic locking. */
  public final VersionPropertyType<ENTITY, ?, ?> versionPropertyType;
  /** The property type that represents the tenant ID column for multi-tenancy. */
  public final TenantIdPropertyType<ENTITY, ?, ?> tenantIdPropertyType;
  /** Whether to ignore the version property for optimistic locking. */
  public boolean versionIgnored;
  /** The entity instance to be deleted. */
  public final ENTITY entity;
  /** The properties to be returned from the DELETE operation. */
  public ReturningProperties returning;

  /**
   * Creates a new context for assembling DELETE statements.
   *
   * @param buf the SQL builder
   * @param entityType the entity type
   * @param naming the naming convention
   * @param dialect the SQL dialect
   * @param idPropertyTypes the list of ID property types
   * @param versionPropertyType the version property type
   * @param tenantIdPropertyType the tenant ID property type
   * @param versionIgnored whether to ignore the version property
   * @param entity the entity instance
   * @param returning the properties to be returned
   */
  DeleteAssemblerContext(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      VersionPropertyType<ENTITY, ?, ?> versionPropertyType,
      TenantIdPropertyType<ENTITY, ?, ?> tenantIdPropertyType,
      boolean versionIgnored,
      ENTITY entity,
      ReturningProperties returning) {
    this.buf = Objects.requireNonNull(buf);
    this.entityType = Objects.requireNonNull(entityType);
    this.naming = Objects.requireNonNull(naming);
    this.dialect = Objects.requireNonNull(dialect);
    this.idPropertyTypes = Objects.requireNonNull(idPropertyTypes);
    this.versionPropertyType = versionPropertyType;
    this.tenantIdPropertyType = tenantIdPropertyType;
    this.versionIgnored = versionIgnored;
    this.entity = Objects.requireNonNull(entity);
    this.returning = Objects.requireNonNull(returning);
  }
}
