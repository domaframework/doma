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

import java.util.Objects;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.MultiInsertAssembler;
import org.seasar.doma.jdbc.query.MultiInsertAssemblerContext;
import org.seasar.doma.jdbc.query.ReturningProperties;

public class H2MultiInsertAssembler<ENTITY> implements MultiInsertAssembler {

  private final PreparedSqlBuilder buf;
  private final EntityType<?> entityType;
  private final Naming naming;
  private final Dialect dialect;
  private final ReturningProperties returning;
  private final DefaultMultiInsertAssembler<ENTITY> multiInsertAssembler;

  public H2MultiInsertAssembler(MultiInsertAssemblerContext<ENTITY> context) {
    Objects.requireNonNull(context);
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.naming = context.naming;
    this.dialect = context.dialect;
    this.returning = context.returning;
    this.multiInsertAssembler = new DefaultMultiInsertAssembler<>(context);
  }

  @Override
  public void assemble() {
    if (returning.isNone()) {
      multiInsertAssembler.assemble();
    } else {
      H2AssemblerUtil.assembleFinalTable(
          buf, entityType, naming, dialect, returning, multiInsertAssembler::assemble);
    }
  }
}
