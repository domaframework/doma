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
import org.seasar.doma.jdbc.query.UpdateAssembler;
import org.seasar.doma.jdbc.query.UpdateAssemblerContext;

public class StandardUpdateAssembler<ENTITY> implements UpdateAssembler {
  private final UpdateAssemblerContext<ENTITY> context;
  private final DefaultUpdateAssembler<ENTITY> updateAssembler;

  public StandardUpdateAssembler(UpdateAssemblerContext<ENTITY> context) {
    this.context = Objects.requireNonNull(context);
    this.updateAssembler = new DefaultUpdateAssembler<>(context);
  }

  @Override
  public void assemble() {
    updateAssembler.assemble();

    if (context.returning) {
      StandardAssemblerUtil.assembleReturning(
          context.buf, context.entityType, context.naming, context.dialect);
    }
  }
}
