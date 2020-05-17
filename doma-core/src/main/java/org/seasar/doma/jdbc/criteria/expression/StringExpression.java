package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface StringExpression<PROPERTY> extends PropertyMetamodel<PROPERTY> {

  abstract class AbstractStringExpression<PROPERTY> implements StringExpression<PROPERTY> {
    private final PropertyMetamodel<?> propertyMetamodel;
    public final Operand left;
    public final Operand right;

    protected AbstractStringExpression(
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
      if (!(o instanceof StringExpression.AbstractStringExpression)) return false;
      AbstractStringExpression<?> that = (AbstractStringExpression<?>) o;
      return propertyMetamodel.equals(that.propertyMetamodel)
          && left.equals(that.left)
          && right.equals(that.right);
    }

    @Override
    public int hashCode() {
      return Objects.hash(propertyMetamodel, left, right);
    }
  }

  class Concat<PROPERTY> extends AbstractStringExpression<PROPERTY> {

    public Concat(PropertyMetamodel<?> propertyDef, Operand left, Operand right) {
      super(propertyDef, left, right);
    }

    @Override
    public String getName() {
      return "concat";
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof StringExpression.Visitor) {
        StringExpression.Visitor v = (StringExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  interface Visitor {
    void visit(StringExpression.Concat<?> concat);
  }
}
