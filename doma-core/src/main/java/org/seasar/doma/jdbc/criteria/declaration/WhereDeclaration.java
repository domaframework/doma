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
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;

/** The where declaration. */
public class WhereDeclaration extends ComparisonDeclaration {

  public WhereDeclaration(SelectContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(DeleteContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(UpdateContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }
}
