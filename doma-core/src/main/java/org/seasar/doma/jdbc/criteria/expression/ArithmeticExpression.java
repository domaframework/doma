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
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface ArithmeticExpression<PROPERTY> extends PropertyMetamodel<PROPERTY> {

  abstract class AbstractArithmeticExpression<PROPERTY> implements ArithmeticExpression<PROPERTY> {
    private final PropertyMetamodel<?> propertyMetamodel;
    public final Operand left;
    public final Operand right;

    protected AbstractArithmeticExpression(
        PropertyMetamodel<?> propertyMetamodel, Operand left, Operand right) {
      this.propertyMetamodel = Objects.requireNonNull(propertyMetamodel);
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
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
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      AbstractArithmeticExpression<?> that = (AbstractArithmeticExpression<?>) o;
      return propertyMetamodel.equals(that.propertyMetamodel)
          && left.equals(that.left)
          && right.equals(that.right);
    }

    @Override
    public int hashCode() {
      return Objects.hash(propertyMetamodel, left, right);
    }
  }

  class Add<PROPERTY> extends AbstractArithmeticExpression<PROPERTY> {

    public Add(PropertyMetamodel<?> propertyMetamodel, Operand left, Operand right) {
      super(propertyMetamodel, left, right);
    }

    @Override
    public String getName() {
      return "+";
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof ArithmeticExpression.Visitor) {
        ArithmeticExpression.Visitor v = (ArithmeticExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Sub<PROPERTY> extends AbstractArithmeticExpression<PROPERTY> {

    public Sub(PropertyMetamodel<?> propertyMetamodel, Operand left, Operand right) {
      super(propertyMetamodel, left, right);
    }

    @Override
    public String getName() {
      return "-";
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof ArithmeticExpression.Visitor) {
        ArithmeticExpression.Visitor v = (ArithmeticExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Mul<PROPERTY> extends AbstractArithmeticExpression<PROPERTY> {

    public Mul(PropertyMetamodel<?> propertyMetamodel, Operand left, Operand right) {
      super(propertyMetamodel, left, right);
    }

    @Override
    public String getName() {
      return "*";
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof ArithmeticExpression.Visitor) {
        ArithmeticExpression.Visitor v = (ArithmeticExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Div<PROPERTY> extends AbstractArithmeticExpression<PROPERTY> {

    public Div(PropertyMetamodel<?> propertyMetamodel, Operand left, Operand right) {
      super(propertyMetamodel, left, right);
    }

    @Override
    public String getName() {
      return "/";
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof ArithmeticExpression.Visitor) {
        ArithmeticExpression.Visitor v = (ArithmeticExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Mod<PROPERTY> extends AbstractArithmeticExpression<PROPERTY> {

    public Mod(PropertyMetamodel<?> propertyMetamodel, Operand left, Operand right) {
      super(propertyMetamodel, left, right);
    }

    @Override
    public String getName() {
      return "%";
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof ArithmeticExpression.Visitor) {
        ArithmeticExpression.Visitor v = (ArithmeticExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  interface Visitor {
    void visit(Add<?> add);

    void visit(Sub<?> sub);

    void visit(Mul<?> mul);

    void visit(Div<?> div);

    void visit(Mod<?> mod);
  }
}
