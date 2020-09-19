package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface StringExpression<PROPERTY> extends PropertyMetamodel<PROPERTY> {

  abstract class OneArgumentStringExpression<PROPERTY> implements StringExpression<PROPERTY> {
    private final String name;
    public final PropertyMetamodel<?> argument;

    protected OneArgumentStringExpression(String name, PropertyMetamodel<?> argument) {
      this.name = Objects.requireNonNull(name);
      this.argument = Objects.requireNonNull(argument);
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Class<?> asClass() {
      return argument.asClass();
    }

    @Override
    public EntityPropertyType<?, ?> asType() {
      return argument.asType();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof StringExpression.OneArgumentStringExpression)) return false;
      OneArgumentStringExpression<?> that = (OneArgumentStringExpression<?>) o;
      return argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
      return Objects.hash(argument);
    }
  }

  abstract class TwoArgumentsStringExpression<PROPERTY> implements StringExpression<PROPERTY> {
    private final String name;
    private final PropertyMetamodel<?> propertyMetamodel;
    public final Operand first;
    public final Operand second;

    protected TwoArgumentsStringExpression(
        @SuppressWarnings("SameParameterValue") String name,
        PropertyMetamodel<?> propertyMetamodel,
        Operand first,
        Operand second) {
      this.name = Objects.requireNonNull(name);
      this.propertyMetamodel = Objects.requireNonNull(propertyMetamodel);
      this.first = Objects.requireNonNull(first);
      this.second = Objects.requireNonNull(second);
    }

    @Override
    public String getName() {
      return name;
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
      if (!(o instanceof StringExpression.TwoArgumentsStringExpression)) return false;
      TwoArgumentsStringExpression<?> that = (TwoArgumentsStringExpression<?>) o;
      return propertyMetamodel.equals(that.propertyMetamodel)
          && first.equals(that.first)
          && second.equals(that.second);
    }

    @Override
    public int hashCode() {
      return Objects.hash(propertyMetamodel, first, second);
    }
  }

  class Concat<PROPERTY> extends TwoArgumentsStringExpression<PROPERTY> {

    public Concat(PropertyMetamodel<?> propertyMetamodel, Operand left, Operand right) {
      super("concat", propertyMetamodel, left, right);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof StringExpression.Visitor) {
        StringExpression.Visitor v = (StringExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Lower<PROPERTY> extends OneArgumentStringExpression<PROPERTY> {

    public Lower(PropertyMetamodel<?> propertyMetamodel) {
      super("lower", propertyMetamodel);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof StringExpression.Visitor) {
        StringExpression.Visitor v = (StringExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Ltrim<PROPERTY> extends OneArgumentStringExpression<PROPERTY> {

    public Ltrim(PropertyMetamodel<?> propertyMetamodel) {
      super("ltrim", propertyMetamodel);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof StringExpression.Visitor) {
        StringExpression.Visitor v = (StringExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Rtrim<PROPERTY> extends OneArgumentStringExpression<PROPERTY> {

    public Rtrim(PropertyMetamodel<?> propertyMetamodel) {
      super("rtrim", propertyMetamodel);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof StringExpression.Visitor) {
        StringExpression.Visitor v = (StringExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Trim<PROPERTY> extends OneArgumentStringExpression<PROPERTY> {

    public Trim(PropertyMetamodel<?> propertyMetamodel) {
      super("trim", propertyMetamodel);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof StringExpression.Visitor) {
        StringExpression.Visitor v = (StringExpression.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Upper<PROPERTY> extends OneArgumentStringExpression<PROPERTY> {

    public Upper(PropertyMetamodel<?> propertyMetamodel) {
      super("upper", propertyMetamodel);
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

    void visit(StringExpression.Lower<?> lower);

    void visit(StringExpression.Ltrim<?> ltrim);

    void visit(StringExpression.Rtrim<?> rtrim);

    void visit(StringExpression.Trim<?> trim);

    void visit(StringExpression.Upper<?> upper);
  }
}
