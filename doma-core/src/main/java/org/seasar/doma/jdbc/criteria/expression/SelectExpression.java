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
package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SubSelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public class SelectExpression<PROPERTY> implements PropertyMetamodel<PROPERTY> {

  public final SelectContext context;
  public final PropertyMetamodel<?> propertyMetamodel;

  public SelectExpression(SubSelectContext.Single<PROPERTY> subSelectContext) {
    Objects.requireNonNull(subSelectContext);
    this.context = subSelectContext.get();
    this.propertyMetamodel = subSelectContext.getPropertyMetamodel();
  }

  @Override
  public Class<?> asClass() {
    return propertyMetamodel.asClass();
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return propertyMetamodel.asType();
  }

  @Override
  public String getName() {
    return propertyMetamodel.getName();
  }

  @Override
  public void accept(PropertyMetamodel.Visitor visitor) {
    if (visitor instanceof SelectExpression.Visitor) {
      SelectExpression.Visitor v = (SelectExpression.Visitor) visitor;
      v.visit(this);
    }
  }

  public static class Declaration {
    public <ENTITY> SubSelectFromDeclaration<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
      return new SubSelectFromDeclaration<>(entityMetamodel);
    }
  }

  public interface Visitor {
    void visit(SelectExpression<?> expression);
  }
}
