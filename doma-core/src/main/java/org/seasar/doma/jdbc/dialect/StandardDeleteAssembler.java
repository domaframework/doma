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
import org.seasar.doma.jdbc.query.DeleteAssembler;
import org.seasar.doma.jdbc.query.DeleteAssemblerContext;

public class StandardDeleteAssembler<ENTITY> implements DeleteAssembler {
  private final DeleteAssemblerContext<ENTITY> context;
  private final DefaultDeleteAssembler<ENTITY> deleteAssembler;

  public StandardDeleteAssembler(DeleteAssemblerContext<ENTITY> context) {
    this.context = Objects.requireNonNull(context);
    this.deleteAssembler = new DefaultDeleteAssembler<>(context);
  }

  @Override
  public void assemble() {
    deleteAssembler.assemble();

    if (context.returning) {
      StandardAssemblerUtil.assembleReturning(
          context.buf, context.entityType, context.naming, context.dialect);
    }
  }
}
