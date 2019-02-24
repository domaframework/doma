package org.seasar.doma.internal.jdbc.scalar;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Supplier;
import org.seasar.doma.Domain;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainTypeFactory;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.ArrayWrapper;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.BigIntegerWrapper;
import org.seasar.doma.wrapper.BlobWrapper;
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.ByteWrapper;
import org.seasar.doma.wrapper.BytesWrapper;
import org.seasar.doma.wrapper.ClobWrapper;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.DoubleWrapper;
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.FloatWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.LocalDateTimeWrapper;
import org.seasar.doma.wrapper.LocalDateWrapper;
import org.seasar.doma.wrapper.LocalTimeWrapper;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.NClobWrapper;
import org.seasar.doma.wrapper.ObjectWrapper;
import org.seasar.doma.wrapper.PrimitiveBooleanWrapper;
import org.seasar.doma.wrapper.PrimitiveByteWrapper;
import org.seasar.doma.wrapper.PrimitiveDoubleWrapper;
import org.seasar.doma.wrapper.PrimitiveFloatWrapper;
import org.seasar.doma.wrapper.PrimitiveIntWrapper;
import org.seasar.doma.wrapper.PrimitiveLongWrapper;
import org.seasar.doma.wrapper.PrimitiveShortWrapper;
import org.seasar.doma.wrapper.SQLXMLWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;
import org.seasar.doma.wrapper.Wrapper;

public final class Scalars {

  public static Supplier<Scalar<?, ?>> wrap(
      Object value, Class<?> valueClass, boolean optional, ClassHelper classHelper) {
    assertNotNull(valueClass, classHelper);

    if (Scalar.class.isAssignableFrom(valueClass)) {
      return () -> (Scalar<?, ?>) value;
    }
    Supplier<Scalar<?, ?>> result = wrapBasicObject(value, valueClass, optional);
    if (result == null) {
      result = wrapDomainObject(value, valueClass, optional, classHelper);
      if (result == null) {
        result = wrapEnumObject(value, valueClass, optional);
        if (result == null) {
          throw new ScalarException(Message.DOMA1007, valueClass.getName());
        }
      }
    }
    return result;
  }

  protected static Supplier<Scalar<?, ?>> wrapBasicObject(
      Object value, Class<?> valueClass, boolean optional) {
    assertNotNull(valueClass);
    if (valueClass == String.class) {
      Supplier<Wrapper<String>> supplier = () -> new StringWrapper((String) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == boolean.class) {
      Supplier<Wrapper<Boolean>> supplier = () -> new PrimitiveBooleanWrapper((boolean) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == byte.class) {
      Supplier<Wrapper<Byte>> supplier = () -> new PrimitiveByteWrapper((byte) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == double.class) {
      Supplier<Wrapper<Double>> supplier = () -> new PrimitiveDoubleWrapper((double) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == float.class) {
      Supplier<Wrapper<Float>> supplier = () -> new PrimitiveFloatWrapper((float) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == int.class) {
      Supplier<Wrapper<Integer>> supplier = () -> new PrimitiveIntWrapper((int) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == long.class) {
      Supplier<Wrapper<Long>> supplier = () -> new PrimitiveLongWrapper((long) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == short.class) {
      Supplier<Wrapper<Short>> supplier = () -> new PrimitiveShortWrapper((short) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Integer.class) {
      Supplier<Wrapper<Integer>> supplier = () -> new IntegerWrapper((Integer) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Long.class) {
      Supplier<Wrapper<Long>> supplier = () -> new LongWrapper((Long) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == BigDecimal.class) {
      Supplier<Wrapper<BigDecimal>> supplier = () -> new BigDecimalWrapper((BigDecimal) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == java.util.Date.class) {
      Supplier<Wrapper<java.util.Date>> supplier =
          () -> new UtilDateWrapper((java.util.Date) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == LocalDate.class) {
      Supplier<Wrapper<LocalDate>> supplier = () -> new LocalDateWrapper((LocalDate) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == LocalTime.class) {
      Supplier<Wrapper<LocalTime>> supplier = () -> new LocalTimeWrapper((LocalTime) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == LocalDateTime.class) {
      Supplier<Wrapper<LocalDateTime>> supplier =
          () -> new LocalDateTimeWrapper((LocalDateTime) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Date.class) {
      Supplier<Wrapper<Date>> supplier = () -> new DateWrapper((Date) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Timestamp.class) {
      Supplier<Wrapper<Timestamp>> supplier = () -> new TimestampWrapper((Timestamp) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Time.class) {
      Supplier<Wrapper<Time>> supplier = () -> new TimeWrapper((Time) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Boolean.class) {
      Supplier<Wrapper<Boolean>> supplier = () -> new BooleanWrapper((Boolean) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Array.class) {
      Supplier<Wrapper<Array>> supplier = () -> new ArrayWrapper((Array) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == BigInteger.class) {
      Supplier<Wrapper<BigInteger>> supplier = () -> new BigIntegerWrapper((BigInteger) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Blob.class) {
      Supplier<Wrapper<Blob>> supplier = () -> new BlobWrapper((Blob) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == byte[].class) {
      Supplier<Wrapper<byte[]>> supplier = () -> new BytesWrapper((byte[]) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Byte.class) {
      Supplier<Wrapper<Byte>> supplier = () -> new ByteWrapper((Byte) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Clob.class) {
      Supplier<Wrapper<Clob>> supplier = () -> new ClobWrapper((Clob) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Double.class) {
      Supplier<Wrapper<Double>> supplier = () -> new DoubleWrapper((Double) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Float.class) {
      Supplier<Wrapper<Float>> supplier = () -> new FloatWrapper((Float) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == NClob.class) {
      Supplier<Wrapper<NClob>> supplier = () -> new NClobWrapper((NClob) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Short.class) {
      Supplier<Wrapper<Short>> supplier = () -> new ShortWrapper((Short) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == SQLXML.class) {
      Supplier<Wrapper<SQLXML>> supplier = () -> new SQLXMLWrapper((SQLXML) value);
      return createBasicScalarSupplier(supplier, optional);
    }
    if (valueClass == Object.class) {
      Supplier<Wrapper<Object>> supplier = () -> new ObjectWrapper(value);
      return createBasicScalarSupplier(supplier, optional);
    }
    return null;
  }

  protected static <BASIC> Supplier<Scalar<?, ?>> createBasicScalarSupplier(
      Supplier<Wrapper<BASIC>> wrapperSupplier, boolean optional) {
    if (optional) {
      return () -> new OptionalBasicScalar<BASIC>(wrapperSupplier);
    } else {
      return () -> new BasicScalar<BASIC>(wrapperSupplier);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  protected static Supplier<Scalar<?, ?>> wrapEnumObject(
      Object value, Class<?> valueClass, boolean optional) {
    assertNotNull(valueClass);
    if (valueClass.isEnum() || Enum.class.isAssignableFrom(valueClass)) {
      Supplier<EnumWrapper<?>> supplier = () -> new EnumWrapper(valueClass, (Enum) value);
      if (optional) {
        return () -> new OptionalBasicScalar(supplier);
      } else {
        return () -> new BasicScalar(supplier);
      }
    }
    return null;
  }

  protected static <BASIC, DOMAIN> Supplier<Scalar<?, ?>> wrapDomainObject(
      Object value, Class<DOMAIN> valueClass, boolean optional, ClassHelper classHelper) {
    DomainType<BASIC, DOMAIN> domainType;
    if (valueClass.isAnnotationPresent(Domain.class)) {
      domainType = DomainTypeFactory.getDomainType(valueClass, classHelper);
    } else {
      domainType = DomainTypeFactory.getExternalDomainType(valueClass, classHelper);
    }
    if (domainType == null) {
      return null;
    }
    DOMAIN domain = valueClass.cast(value);
    if (optional) {
      return () -> domainType.createOptionalScalar(domain);
    } else {
      return () -> domainType.createScalar(domain);
    }
  }
}
