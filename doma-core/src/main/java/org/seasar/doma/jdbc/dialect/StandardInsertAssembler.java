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
import org.seasar.doma.jdbc.query.InsertAssembler;
import org.seasar.doma.jdbc.query.InsertAssemblerContext;

public class StandardInsertAssembler<ENTITY> implements InsertAssembler {
  private final InsertAssemblerContext<ENTITY> context;
  private final DefaultInsertAssembler<ENTITY> defaultInsertAssembler;

  public StandardInsertAssembler(InsertAssemblerContext<ENTITY> context) {
    this.context = Objects.requireNonNull(context);
    this.defaultInsertAssembler = new DefaultInsertAssembler<>(context);
  }

  @Override
  public void assemble() {
    defaultInsertAssembler.assemble();

    if (context.returning) {
      StandardAssemblerUtil.assembleReturning(
          context.buf, context.entityType, context.naming, context.dialect);
    }
  }
}
