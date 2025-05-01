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

public class UpdateAssemblerContextBuilder {

  public static <ENTITY> UpdateAssemblerContext<ENTITY> build(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      UpdateQueryHelper<ENTITY> updateQueryHelper,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      List<EntityPropertyType<ENTITY, ?>> updatePropertyTypes,
      VersionPropertyType<ENTITY, ?, ?> versionPropertyType,
      TenantIdPropertyType<ENTITY, ?, ?> tenantIdPropertyType,
      boolean versionIgnored,
      ENTITY entity,
      ReturningProperties returning) {

    return new UpdateAssemblerContext<>(
        Objects.requireNonNull(buf),
        Objects.requireNonNull(entityType),
        Objects.requireNonNull(naming),
        Objects.requireNonNull(dialect),
        Objects.requireNonNull(updateQueryHelper),
        Objects.requireNonNull(idPropertyTypes),
        Objects.requireNonNull(updatePropertyTypes),
        versionPropertyType,
        tenantIdPropertyType,
        versionIgnored,
        Objects.requireNonNull(entity),
        Objects.requireNonNull(returning));
  }
}
