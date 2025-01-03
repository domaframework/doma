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
package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;

public class InsertDeclaration {

  private final InsertContext context;

  public InsertDeclaration(InsertContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public InsertContext getContext() {
    return context;
  }

  public void values(Consumer<ValuesDeclaration> block) {
    Objects.requireNonNull(block);
    ValuesDeclaration declaration = new ValuesDeclaration(context);
    block.accept(declaration);
  }

  public void upsertSetValues(Consumer<InsertOnDuplicateKeyUpdateSetValuesDeclaration> block) {
    Objects.requireNonNull(block);
    InsertOnDuplicateKeyUpdateSetValuesDeclaration declaration =
        new InsertOnDuplicateKeyUpdateSetValuesDeclaration(context);
    block.accept(declaration);
    if (context.onDuplicateContext.setValues.isEmpty()) {
      context.onDuplicateContext.setValues.putAll(context.values);
    }
  }

  public void select(Function<InsertSelectDeclaration, SubSelectContext<?>> block) {
    InsertSelectDeclaration declaration = new InsertSelectDeclaration();
    SubSelectContext<?> subSelectContext = block.apply(declaration);
    context.selectContext = subSelectContext.get();
  }
}
