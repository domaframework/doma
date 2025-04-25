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
import org.seasar.doma.jdbc.entity.EntityType;

final class H2AssemblerUtil {

  static void assembleFinalTable(
      PreparedSqlBuilder buf,
      EntityType<?> entityType,
      Naming naming,
      Dialect dialect,
      Runnable block) {
    buf.appendSql("select ");
    for (var p : entityType.getEntityPropertyTypes()) {
      buf.appendSql(p.getColumnName(naming::apply, dialect::applyQuote));
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    buf.appendSql(" from final table (");
    block.run();
    buf.appendSql(")");
  }
}
