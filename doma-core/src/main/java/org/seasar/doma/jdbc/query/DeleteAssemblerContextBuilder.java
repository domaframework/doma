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
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

public class DeleteAssemblerContextBuilder {

  public static <ENTITY> DeleteAssemblerContext<ENTITY> build(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> idPropertyTypes,
      VersionPropertyType<ENTITY, ?, ?> versionPropertyType,
      EntityPropertyType<ENTITY, ?> tenantIdPropertyType,
      boolean versionIgnored,
      ENTITY entity,
      ReturningProperties returning) {

    // TODO: check arguments

    return new DeleteAssemblerContext<>(
        buf,
        entityType,
        naming,
        dialect,
        idPropertyTypes,
        versionPropertyType,
        tenantIdPropertyType,
        versionIgnored,
        entity,
        returning);
  }
}
