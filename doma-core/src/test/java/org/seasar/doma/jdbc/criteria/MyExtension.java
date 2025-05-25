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
package org.seasar.doma.jdbc.criteria;

import org.seasar.doma.jdbc.criteria.declaration.UserDefinedCriteriaContext;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

record MyExtension(UserDefinedCriteriaContext context) {
  public void likeMultiple(PropertyMetamodel<String> entityMetamodel, String... patterns) {
    context.add(
        (b) -> {
          for (String pattern : patterns) {
            b.appendExpression(entityMetamodel);
            b.appendSql(" like ");
            b.appendParameter(entityMetamodel, "%" + pattern + "%");
            b.appendSql(" or ");
          }
          b.cutBackSql(4);
        });
  }

  public void eq2(PropertyMetamodel<String> entityMetamodel, String pattern) {
    context.add(
        (b) -> {
          b.appendExpression(entityMetamodel);
          b.appendSql(" = ");
          b.appendParameter(entityMetamodel, pattern);
        });
  }
}
