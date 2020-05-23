package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface AggregateFunction<PROPERTY> extends PropertyMetamodel<PROPERTY> {
  Asterisk Asterisk = new Asterisk();

  PropertyMetamodel<?> argument();

  abstract class AbstractFunction<PROPERTY> implements AggregateFunction<PROPERTY> {
    private final String name;
    private final PropertyMetamodel<?> argument;

    protected AbstractFunction(String name, PropertyMetamodel<?> argument) {
      Objects.requireNonNull(name);
      Objects.requireNonNull(argument);
      this.name = name;
      this.argument = argument;
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
    public PropertyMetamodel<?> argument() {
      return argument;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof AbstractFunction)) return false;
      AbstractFunction<?> that = (AbstractFunction<?>) o;
      return name.equals(that.name) && argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, argument);
    }
  }

  class Avg<PROPERTY> extends AbstractFunction<PROPERTY> {

    public Avg(PropertyMetamodel<PROPERTY> argument) {
      super("avg", argument);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof AggregateFunction.Visitor) {
        AggregateFunction.Visitor v = (AggregateFunction.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Count extends AbstractFunction<Long> {

    public final boolean distinct;

    public Count(PropertyMetamodel<?> argument) {
      this(argument, false);
    }

    public Count(PropertyMetamodel<?> argument, boolean distinct) {
      super("count", argument);
      this.distinct = distinct;
    }

    @Override
    public Class<?> asClass() {
      return Long.class;
    }

    @Override
    public EntityPropertyType<?, Long> asType() {
      return LongPropertyType.INSTANCE;
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof AggregateFunction.Visitor) {
        AggregateFunction.Visitor v = (AggregateFunction.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Max<PROPERTY> extends AbstractFunction<PROPERTY> {

    public Max(PropertyMetamodel<PROPERTY> argument) {
      super("max", argument);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof AggregateFunction.Visitor) {
        AggregateFunction.Visitor v = (AggregateFunction.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Min<PROPERTY> extends AbstractFunction<PROPERTY> {

    public Min(PropertyMetamodel<PROPERTY> argument) {
      super("min", argument);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof AggregateFunction.Visitor) {
        AggregateFunction.Visitor v = (AggregateFunction.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Sum<PROPERTY> extends AbstractFunction<PROPERTY> {
    public Sum(PropertyMetamodel<PROPERTY> argument) {
      super("sum", argument);
    }

    @Override
    public void accept(PropertyMetamodel.Visitor visitor) {
      if (visitor instanceof AggregateFunction.Visitor) {
        AggregateFunction.Visitor v = (AggregateFunction.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  class Asterisk implements PropertyMetamodel<Long> {

    private Asterisk() {}

    @Override
    public String getName() {
      return "*";
    }

    @Override
    public Class<?> asClass() {
      return Long.class;
    }

    @Override
    public EntityPropertyType<?, ?> asType() {
      return LongPropertyType.INSTANCE;
    }

    @Override
    public void accept(Visitor visitor) {
      if (visitor instanceof AggregateFunction.Visitor) {
        AggregateFunction.Visitor v = (AggregateFunction.Visitor) visitor;
        v.visit(this);
      }
    }
  }

  interface Visitor {
    void visit(AggregateFunction.Avg<?> avg);

    void visit(AggregateFunction.Count count);

    void visit(AggregateFunction.Max<?> max);

    void visit(AggregateFunction.Min<?> min);

    void visit(AggregateFunction.Sum<?> sum);

    void visit(AggregateFunction.Asterisk asterisk);
  }
}
