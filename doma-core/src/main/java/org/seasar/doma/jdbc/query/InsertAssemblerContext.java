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

/**
 * An assembler for insert statements.
 *
 * @param <ENTITY> the entity type
 */
public class InsertAssemblerContext<ENTITY> {
  public final PreparedSqlBuilder buf;
  public final EntityType<ENTITY> entityType;
  public final Naming naming;
  public final Dialect dialect;
  public final List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes;
  public final ENTITY entity;
  public ReturningProperties returning;

  /**
   * Creates an instance.
   *
   * @param buf the SQL buffer
   * @param entityType the entity type
   * @param naming the naming convention
   * @param dialect the SQL dialect
   * @param insertPropertyTypes the property types that are targets for the insert
   * @param entity the entity
   * @param returning the properties to be returned after insert execution
   */
  InsertAssemblerContext(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> insertPropertyTypes,
      ENTITY entity,
      ReturningProperties returning) {
    this.buf = Objects.requireNonNull(buf);
    this.entityType = Objects.requireNonNull(entityType);
    this.naming = Objects.requireNonNull(naming);
    this.dialect = Objects.requireNonNull(dialect);
    this.insertPropertyTypes = Objects.requireNonNull(insertPropertyTypes);
    this.entity = Objects.requireNonNull(entity);
    this.returning = Objects.requireNonNull(returning);
  }
}
