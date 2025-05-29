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
package org.seasar.doma.it.criteria;

import org.seasar.doma.jdbc.criteria.declaration.UserDefinedCriteriaContext;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

record MyExtension(UserDefinedCriteriaContext context) {
  public void regexp(PropertyMetamodel<String> propertyMetamodel, String regexp) {
    context.add(
        (b) -> {
          var dialectName = b.getDialect().getName();
          if (dialectName.startsWith("mysql")) {
            b.appendExpression(propertyMetamodel);
            b.appendSql(" regexp ");
            b.appendParameter(propertyMetamodel, regexp);
          } else if (dialectName.equals("postgres")) {
            b.appendExpression(propertyMetamodel);
            b.appendSql(" ~ ");
            b.appendParameter(propertyMetamodel, regexp);
          } else if (dialectName.equals("oracle")) {
            b.appendSql("regexp_like(");
            b.appendExpression(propertyMetamodel);
            b.appendSql(",");
            b.appendParameter(propertyMetamodel, regexp);
            b.appendSql(")");
          } else {
            b.appendExpression(propertyMetamodel);
            b.appendSql(" like ");
            b.appendParameter(propertyMetamodel, "%" + regexp + "%");
          }
        });
  }

  public void eq2(PropertyMetamodel<String> propertyMetamodel, String pattern) {
    context.add(
        (b) -> {
          b.appendExpression(propertyMetamodel);
          b.appendSql(" = ");
          b.appendParameter(propertyMetamodel, pattern);
        });
  }
}
