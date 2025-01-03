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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.BigIntegerWrapper;
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.ByteWrapper;
import org.seasar.doma.wrapper.DoubleWrapper;
import org.seasar.doma.wrapper.FloatWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.LocalDateTimeWrapper;
import org.seasar.doma.wrapper.LocalDateWrapper;
import org.seasar.doma.wrapper.LocalTimeWrapper;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

abstract class BasicPropertyType<BASIC> implements EntityPropertyType<Object, BASIC> {

  protected final BASIC value;

  protected BasicPropertyType(BASIC value) {
    this.value = value;
  }

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
  public Object getAsNonOptional() {
    return scalar.getAsNonOptional();
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

  @Override
  public Optional<JdbcType<Object>> getJdbcType() {
    return scalar.getJdbcType();
  }
}

class BigDecimalPropertyType extends BasicPropertyType<BigDecimal> {

  BigDecimalPropertyType(BigDecimal value) {
    super(value);
  }

  @Override
  public Property<Object, BigDecimal> createProperty() {
    return new BigDecimalProperty(value);
  }

  static class BigDecimalProperty extends BasicProperty<BigDecimal> {

    BigDecimalProperty(BigDecimal value) {
      super(new BasicScalar<>(new BigDecimalWrapper(value)));
    }
  }
}

class BigIntegerPropertyType extends BasicPropertyType<BigInteger> {

  BigIntegerPropertyType(BigInteger value) {
    super(value);
  }

  @Override
  public Property<Object, BigInteger> createProperty() {
    return new BigIntegerProperty(value);
  }

  static class BigIntegerProperty extends BasicProperty<BigInteger> {

    BigIntegerProperty(BigInteger value) {
      super(new BasicScalar<>(new BigIntegerWrapper(value)));
    }
  }
}

class BooleanPropertyType extends BasicPropertyType<Boolean> {

  BooleanPropertyType(Boolean value) {
    super(value);
  }

  @Override
  public Property<Object, Boolean> createProperty() {
    return new BooleanProperty(value);
  }

  static class BooleanProperty extends BasicProperty<Boolean> {

    BooleanProperty(Boolean value) {
      super(new BasicScalar<>(new BooleanWrapper(value)));
    }
  }
}

class BytePropertyType extends BasicPropertyType<Byte> {

  BytePropertyType(Byte value) {
    super(value);
  }

  @Override
  public Property<Object, Byte> createProperty() {
    return new ByteProperty(value);
  }

  static class ByteProperty extends BasicProperty<Byte> {

    ByteProperty(Byte value) {
      super(new BasicScalar<>(new ByteWrapper(value)));
    }
  }
}

class DoublePropertyType extends BasicPropertyType<Double> {

  DoublePropertyType(Double value) {
    super(value);
  }

  @Override
  public Property<Object, Double> createProperty() {
    return new DoubleProperty(value);
  }

  static class DoubleProperty extends BasicProperty<Double> {

    DoubleProperty(Double value) {
      super(new BasicScalar<>(new DoubleWrapper(value)));
    }
  }
}

class FloatPropertyType extends BasicPropertyType<Float> {

  FloatPropertyType(Float value) {
    super(value);
  }

  @Override
  public Property<Object, Float> createProperty() {
    return new FloatProperty(value);
  }

  static class FloatProperty extends BasicProperty<Float> {

    FloatProperty(Float value) {
      super(new BasicScalar<>(new FloatWrapper(value)));
    }
  }
}

class IntegerPropertyType extends BasicPropertyType<Integer> {

  IntegerPropertyType(Integer value) {
    super(value);
  }

  @Override
  public Property<Object, Integer> createProperty() {
    return new IntegerProperty(value);
  }

  static class IntegerProperty extends BasicProperty<Integer> {

    IntegerProperty(Integer value) {
      super(new BasicScalar<>(new IntegerWrapper(value)));
    }
  }
}

class LocalDatePropertyType extends BasicPropertyType<LocalDate> {

  LocalDatePropertyType(LocalDate value) {
    super(value);
  }

  @Override
  public Property<Object, LocalDate> createProperty() {
    return new LocalDateProperty(value);
  }

  static class LocalDateProperty extends BasicProperty<LocalDate> {

    LocalDateProperty(LocalDate value) {
      super(new BasicScalar<>(new LocalDateWrapper(value)));
    }
  }
}

class LocalDateTimePropertyType extends BasicPropertyType<LocalDateTime> {

  LocalDateTimePropertyType(LocalDateTime value) {
    super(value);
  }

  @Override
  public Property<Object, LocalDateTime> createProperty() {
    return new LocalDateTimeProperty(value);
  }

  static class LocalDateTimeProperty extends BasicProperty<LocalDateTime> {

    LocalDateTimeProperty(LocalDateTime value) {
      super(new BasicScalar<>(new LocalDateTimeWrapper(value)));
    }
  }
}

class LocalTimePropertyType extends BasicPropertyType<LocalTime> {

  LocalTimePropertyType(LocalTime value) {
    super(value);
  }

  @Override
  public Property<Object, LocalTime> createProperty() {
    return new LocalTimeProperty(value);
  }

  static class LocalTimeProperty extends BasicProperty<LocalTime> {

    LocalTimeProperty(LocalTime value) {
      super(new BasicScalar<>(new LocalTimeWrapper(value)));
    }
  }
}

class LongPropertyType extends BasicPropertyType<Long> {

  LongPropertyType(Long value) {
    super(value);
  }

  @Override
  public Property<Object, Long> createProperty() {
    return new LongProperty(value);
  }

  static class LongProperty extends BasicProperty<Long> {

    LongProperty(Long value) {
      super(new BasicScalar<>(new LongWrapper(value)));
    }
  }
}

class ShortPropertyType extends BasicPropertyType<Short> {

  ShortPropertyType(Short value) {
    super(value);
  }

  @Override
  public Property<Object, Short> createProperty() {
    return new ShortProperty(value);
  }

  static class ShortProperty extends BasicProperty<Short> {

    ShortProperty(Short value) {
      super(new BasicScalar<>(new ShortWrapper(value)));
    }
  }
}

class StringPropertyType extends BasicPropertyType<String> {

  StringPropertyType(String value) {
    super(value);
  }

  @Override
  public Property<Object, String> createProperty() {
    return new StringProperty(value);
  }

  static class StringProperty extends BasicProperty<String> {

    StringProperty(String value) {
      super(new BasicScalar<>(new StringWrapper(value)));
    }
  }
}
