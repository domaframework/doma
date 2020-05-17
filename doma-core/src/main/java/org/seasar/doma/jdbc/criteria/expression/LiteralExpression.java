package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface LiteralExpression<PROPERTY> extends PropertyMetamodel<PROPERTY> {

  String toString();

  abstract class AbstractLiteralExpression<PROPERTY> implements LiteralExpression<PROPERTY> {
    protected final PROPERTY value;

    protected AbstractLiteralExpression(PROPERTY value) {
      this.value = Objects.requireNonNull(value);
    }

    @Override
    public Class<?> asClass() {
      return value.getClass();
    }

    @Override
    public EntityPropertyType<?, ?> asType() {
      return null;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof AbstractLiteralExpression)) return false;
      AbstractLiteralExpression<?> that = (AbstractLiteralExpression<?>) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  class StringLiteral extends AbstractLiteralExpression<String> {

    private static final char QUOTATION = '\'';

    public StringLiteral(String value) {
      super(Objects.requireNonNull(value));
      if (value.indexOf(QUOTATION) > -1) {
        throw new DomaIllegalArgumentException(
            "value", "The value must not contain the single quotation.");
      }
    }

    @Override
    public String toString() {
      return QUOTATION + value + QUOTATION;
    }

    @Override
    public String getName() {
      return value;
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof LiteralExpression.Visitor) {
        LiteralExpression.Visitor v = (LiteralExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class IntLiteral extends AbstractLiteralExpression<Integer> {

    public IntLiteral(int value) {
      super(value);
    }

    @Override
    public String toString() {
      return Integer.toString(value);
    }

    @Override
    public String getName() {
      return Integer.toString(value);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof LiteralExpression.Visitor) {
        LiteralExpression.Visitor v = (LiteralExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  interface Visitor {
    void visit(StringLiteral stringLiteral);

    void visit(IntLiteral intLiteral);
  }
}
