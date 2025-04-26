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
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * An assembler for update statements.
 *
 * @param <ENTITY> the entity type
 */
public class UpdateAssemblerContext<ENTITY> {
  public final PreparedSqlBuilder buf;
  public final EntityType<ENTITY> entityType;
  public final Naming naming;
  public final Dialect dialect;
  public final UpdateQueryHelper<ENTITY> updateQueryHelper;
  public final List<EntityPropertyType<ENTITY, ?>> idPropertyTypes;
  public final List<EntityPropertyType<ENTITY, ?>> updatePropertyTypes;
  public final VersionPropertyType<ENTITY, ?, ?> versionPropertyType;
  public final EntityPropertyType<ENTITY, ?> tenantIdPropertyType;
  public boolean versionIgnored;
  public final ENTITY entity;
  public ReturningProperties returning;

  UpdateAssemblerContext(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      UpdateQueryHelper<ENTITY> updateQueryHelper,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> updatePropertyTypes,
      VersionPropertyType<ENTITY, ?, ?> versionPropertyType,
      EntityPropertyType<ENTITY, ?> tenantIdPropertyType,
      boolean versionIgnored,
      ENTITY entity,
      ReturningProperties returning) {
    this.buf = Objects.requireNonNull(buf);
    this.entityType = Objects.requireNonNull(entityType);
    this.naming = Objects.requireNonNull(naming);
    this.dialect = Objects.requireNonNull(dialect);
    this.updateQueryHelper = Objects.requireNonNull(updateQueryHelper);
    this.idPropertyTypes = Objects.requireNonNull(idPropertyTypes);
    this.updatePropertyTypes = Objects.requireNonNull(updatePropertyTypes);
    this.versionPropertyType = versionPropertyType;
    this.tenantIdPropertyType = tenantIdPropertyType;
    this.versionIgnored = versionIgnored;
    this.entity = Objects.requireNonNull(entity);
    this.returning = Objects.requireNonNull(returning);
  }
}
