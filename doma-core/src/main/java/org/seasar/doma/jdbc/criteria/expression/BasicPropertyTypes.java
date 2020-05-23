package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

abstract class BasicPropertyType<BASIC> implements EntityPropertyType<Object, BASIC> {

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
  public void copy(Object dest, Object src) {
    throw new UnsupportedOperationException();
  }
}

abstract class BasicProperty<BASIC> implements Property<Object, BASIC> {
  private final Scalar<BASIC, BASIC> scalar;

  protected BasicProperty(Scalar<BASIC, BASIC> scalar) {
    this.scalar = Objects.requireNonNull(scalar);
  }

  @Override
  public Object get() {
    return scalar.get();
  }

  @Override
  public Property<Object, BASIC> load(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Property<Object, BASIC> save(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public InParameter<BASIC> asInParameter() {
    return new ScalarInParameter<>(scalar);
  }

  @Override
  public Wrapper<BASIC> getWrapper() {
    return scalar.getWrapper();
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return scalar.getDomainClass();
  }
}

class StringPropertyType extends BasicPropertyType<String> {

  static final StringPropertyType INSTANCE = new StringPropertyType();

  @Override
  public Property<Object, String> createProperty() {
    return new StringProperty();
  }

  static class StringProperty extends BasicProperty<String> {

    protected StringProperty() {
      super(new BasicScalar<>(new StringWrapper()));
    }
  }
}

class IntegerPropertyType extends BasicPropertyType<Integer> {

  static final IntegerPropertyType INSTANCE = new IntegerPropertyType();

  @Override
  public Property<Object, Integer> createProperty() {
    return new IntegerProperty();
  }

  static class IntegerProperty extends BasicProperty<Integer> {

    protected IntegerProperty() {
      super(new BasicScalar<>(new IntegerWrapper()));
    }
  }
}

class LongPropertyType extends BasicPropertyType<Long> {

  static final LongPropertyType INSTANCE = new LongPropertyType();

  @Override
  public Property<Object, Long> createProperty() {
    return new LongProperty();
  }

  static class LongProperty extends BasicProperty<Long> {

    protected LongProperty() {
      super(new BasicScalar<>(new LongWrapper()));
    }
  }
}
