package org.seasar.doma.jdbc.criteria.expression;

import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.Wrapper;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

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
    private final LongEntityPropertyType propertyType = new LongEntityPropertyType();

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
      return propertyType;
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
    private final LongEntityPropertyType propertyType = new LongEntityPropertyType();

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
      return propertyType;
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

class LongEntityPropertyType implements EntityPropertyType<Object, Long> {
  @Override
  public String getName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getColumnName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getColumnName(Function<String, String> quoteFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getColumnName(BiFunction<NamingType, String, String> namingFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getColumnName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }

  @Override
  public boolean isId() {
    return false;
  }

  @Override
  public boolean isVersion() {
    return false;
  }

  @Override
  public boolean isTenantId() {
    return false;
  }

  @Override
  public boolean isInsertable() {
    return false;
  }

  @Override
  public boolean isUpdatable() {
    return false;
  }

  @Override
  public Property<Object, Long> createProperty() {
    return new LongProperty();
  }

  @Override
  public void copy(Object dest, Object src) {
    throw new UnsupportedOperationException();
  }
}

class LongProperty implements Property<Object, Long> {
  private final Scalar<Long, Long> scalar = new BasicScalar<>(LongWrapper::new);

  @Override
  public Object get() {
    return scalar.get();
  }

  @Override
  public Property<Object, Long> load(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Property<Object, Long> save(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public InParameter<Long> asInParameter() {
    return new ScalarInParameter<>(scalar);
  }

  @Override
  public Wrapper<Long> getWrapper() {
    return scalar.getWrapper();
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return scalar.getDomainClass();
  }
}
