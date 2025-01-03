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
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlSelectStarting;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

/**
 * Used to add an alias to a column in the select clause. For in adherence to standard SQL, It is
 * recommended to specify only in the {@link NativeSqlSelectStarting#select} and {@link
 * NativeSqlSelectStarting#orderBy} method.
 *
 * @param <PROPERTY> the property type
 */
public class AliasExpression<PROPERTY> implements PropertyMetamodel<PROPERTY> {

  private final PropertyMetamodel<PROPERTY> originalPropertyMetamodel;
  private final String alias;

  public AliasExpression(PropertyMetamodel<PROPERTY> originalPropertyMetamodel, String alias) {
    this.originalPropertyMetamodel = Objects.requireNonNull(originalPropertyMetamodel);
    this.alias = Objects.requireNonNull(alias);
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return originalPropertyMetamodel.asType();
  }

  @Override
  public Class<?> asClass() {
    return originalPropertyMetamodel.asClass();
  }

  @Override
  public String getName() {
    return originalPropertyMetamodel.getName();
  }

  @Override
  public void accept(PropertyMetamodel.Visitor visitor) {
    if (visitor instanceof AliasExpression.Visitor) {
      AliasExpression.Visitor v = (AliasExpression.Visitor) visitor;
      v.visit(this);
    }
  }

  public PropertyMetamodel<PROPERTY> getOriginalPropertyMetamodel() {
    return originalPropertyMetamodel;
  }

  public String getAlias() {
    return alias;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AliasExpression<?> that = (AliasExpression<?>) o;
    return originalPropertyMetamodel.equals(that.originalPropertyMetamodel)
        && alias.equals(that.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originalPropertyMetamodel, alias);
  }

  public interface Visitor {

    void visit(AliasExpression<?> expression);
  }
}
