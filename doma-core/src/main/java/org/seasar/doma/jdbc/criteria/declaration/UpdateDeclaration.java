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
import org.seasar.doma.jdbc.criteria.context.UpdateContext;

public class UpdateDeclaration {

  private final UpdateContext context;

  public UpdateDeclaration(UpdateContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public UpdateContext getContext() {
    return context;
  }

  public void set(Consumer<SetDeclaration> block) {
    Objects.requireNonNull(block);
    SetDeclaration declaration = new SetDeclaration(context);
    block.accept(declaration);
  }

  public void where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    WhereDeclaration declaration = new WhereDeclaration(context);
    block.accept(declaration);
  }
}
