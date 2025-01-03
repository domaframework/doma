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
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public class LiteralExpression<PROPERTY> implements PropertyMetamodel<PROPERTY> {

  private final PROPERTY value;
  private final BasicPropertyType<PROPERTY> propertyType;

  public LiteralExpression(
      PROPERTY value, Function<PROPERTY, BasicPropertyType<PROPERTY>> factory) {
    this.value = Objects.requireNonNull(value);
    Objects.requireNonNull(factory);
    this.propertyType = factory.apply(value);
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return propertyType;
  }

  @Override
  public Class<?> asClass() {
    return value.getClass();
  }

  @Override
  public String getName() {
    return value.toString();
  }

  @Override
  public void accept(PropertyMetamodel.Visitor visitor) {
    if (visitor instanceof LiteralExpression.Visitor) {
      LiteralExpression.Visitor v = (LiteralExpression.Visitor) visitor;
      v.visit(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LiteralExpression<?> that = (LiteralExpression<?>) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  public interface Visitor {

    void visit(LiteralExpression<?> expression);
  }
}
