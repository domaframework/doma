package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.Wrapper;

public interface AggregateFunction<PROPERTY> extends PropertyDef<PROPERTY> {
  Asterisk Asterisk = new Asterisk();

  PropertyDef<?> argument();

  abstract class AbstractFunction<PROPERTY> implements AggregateFunction<PROPERTY> {
    private final String name;
    private final PropertyDef<?> argument;

    protected AbstractFunction(String name, PropertyDef<?> argument) {
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
    public EntityPropertyType<?, ?> asType() {
      return argument.asType();
    }

    @Override
    public PropertyDef<?> argument() {
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

    private final PropertyDef<PROPERTY> argument;

    public Avg(PropertyDef<PROPERTY> argument) {
      super("avg", argument);
      this.argument = argument;
    }

    @Override
    public Class<PROPERTY> asClass() {
      return argument.asClass();
    }
  }

  class Count extends AbstractFunction<Long> {
    private final LongEntityPropertyType propertyType = new LongEntityPropertyType();

    public Count(PropertyDef<?> argument) {
      super("count", argument);
    }

    @Override
    public Class<Long> asClass() {
      return Long.class;
    }

    @Override
    public EntityPropertyType<?, Long> asType() {
      return propertyType;
    }
  }

  class Max<PROPERTY> extends AbstractFunction<PROPERTY> {
    private final PropertyDef<PROPERTY> argument;

    public Max(PropertyDef<PROPERTY> argument) {
      super("max", argument);
      this.argument = argument;
    }

    @Override
    public Class<PROPERTY> asClass() {
      return argument.asClass();
    }
  }

  class Min<PROPERTY> extends AbstractFunction<PROPERTY> {
    private final PropertyDef<PROPERTY> argument;

    public Min(PropertyDef<PROPERTY> argument) {
      super("min", argument);
      this.argument = argument;
    }

    @Override
    public Class<PROPERTY> asClass() {
      return argument.asClass();
    }
  }

  class Sum<PROPERTY> extends AbstractFunction<PROPERTY> {
    private final PropertyDef<PROPERTY> argument;

    public Sum(PropertyDef<PROPERTY> argument) {
      super("sum", argument);
      this.argument = argument;
    }

    @Override
    public Class<PROPERTY> asClass() {
      return argument.asClass();
    }
  }

  class Asterisk implements PropertyDef<Long> {
    private final LongEntityPropertyType propertyType = new LongEntityPropertyType();

    private Asterisk() {}

    @Override
    public String getName() {
      return "*";
    }

    @Override
    public Class<Long> asClass() {
      return Long.class;
    }

    @Override
    public EntityPropertyType<?, ?> asType() {
      return propertyType;
    }
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
