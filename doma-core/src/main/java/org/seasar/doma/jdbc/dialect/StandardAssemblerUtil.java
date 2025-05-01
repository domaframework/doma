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
package org.seasar.doma.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.ReturningProperties;

final class StandardAssemblerUtil {
  static void assembleReturning(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      Naming naming,
      Dialect dialect,
      ReturningProperties returning) {
    buf.appendSql(" returning ");
    for (EntityPropertyType<?, ?> propertyType : returning.resolve(entityType)) {
      buf.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
  }
}
