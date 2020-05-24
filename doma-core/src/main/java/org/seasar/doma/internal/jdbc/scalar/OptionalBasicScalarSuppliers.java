package org.seasar.doma.internal.jdbc.scalar;

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
import java.util.Optional;
import java.util.function.Supplier;
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
import org.seasar.doma.wrapper.SQLXMLWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;

public final class OptionalBasicScalarSuppliers {

  public static Supplier<Scalar<Array, Optional<Array>>> ofArray() {
    return () -> new OptionalBasicScalar<>(new ArrayWrapper());
  }

  public static Supplier<Scalar<BigDecimal, Optional<BigDecimal>>> ofBigDecimal() {
    return () -> new OptionalBasicScalar<>(new BigDecimalWrapper());
  }

  public static Supplier<Scalar<BigInteger, Optional<BigInteger>>> ofBigInteger() {
    return () -> new OptionalBasicScalar<>(new BigIntegerWrapper());
  }

  public static Supplier<Scalar<Blob, Optional<Blob>>> ofBlob() {
    return () -> new OptionalBasicScalar<>(new BlobWrapper());
  }

  public static Supplier<Scalar<Boolean, Optional<Boolean>>> ofBoolean() {
    return () -> new OptionalBasicScalar<>(new BooleanWrapper());
  }

  public static Supplier<Scalar<Byte, Optional<Byte>>> ofByte() {
    return () -> new OptionalBasicScalar<>(new ByteWrapper());
  }

  public static Supplier<Scalar<byte[], Optional<byte[]>>> ofBytes() {
    return () -> new OptionalBasicScalar<>(new BytesWrapper());
  }

  public static Supplier<Scalar<Clob, Optional<Clob>>> ofClob() {
    return () -> new OptionalBasicScalar<>(new ClobWrapper());
  }

  public static Supplier<Scalar<Date, Optional<Date>>> ofDate() {
    return () -> new OptionalBasicScalar<>(new DateWrapper());
  }

  public static Supplier<Scalar<Double, Optional<Double>>> ofDouble() {
    return () -> new OptionalBasicScalar<>(new DoubleWrapper());
  }

  public static <E extends Enum<E>> Supplier<Scalar<E, Optional<E>>> ofEnum(Class<E> enumClass) {
    return () -> new OptionalBasicScalar<>(new EnumWrapper<>(enumClass));
  }

  public static Supplier<Scalar<Float, Optional<Float>>> ofFloat() {
    return () -> new OptionalBasicScalar<>(new FloatWrapper());
  }

  public static Supplier<Scalar<Integer, Optional<Integer>>> ofInteger() {
    return () -> new OptionalBasicScalar<>(new IntegerWrapper());
  }

  public static Supplier<Scalar<LocalDate, Optional<LocalDate>>> ofLocalDate() {
    return () -> new OptionalBasicScalar<>(new LocalDateWrapper());
  }

  public static Supplier<Scalar<LocalDateTime, Optional<LocalDateTime>>> ofLocalDateTime() {
    return () -> new OptionalBasicScalar<>(new LocalDateTimeWrapper());
  }

  public static Supplier<Scalar<LocalTime, Optional<LocalTime>>> ofLocalTime() {
    return () -> new OptionalBasicScalar<>(new LocalTimeWrapper());
  }

  public static Supplier<Scalar<Long, Optional<Long>>> ofLong() {
    return () -> new OptionalBasicScalar<>(new LongWrapper());
  }

  public static Supplier<Scalar<NClob, Optional<NClob>>> ofNClob() {
    return () -> new OptionalBasicScalar<>(new NClobWrapper());
  }

  public static Supplier<Scalar<Object, Optional<Object>>> ofObject() {
    return () -> new OptionalBasicScalar<>(new ObjectWrapper());
  }

  public static Supplier<Scalar<Short, Optional<Short>>> ofShort() {
    return () -> new OptionalBasicScalar<>(new ShortWrapper());
  }

  public static Supplier<Scalar<SQLXML, Optional<SQLXML>>> ofSQLXML() {
    return () -> new OptionalBasicScalar<>(new SQLXMLWrapper());
  }

  public static Supplier<Scalar<String, Optional<String>>> ofString() {
    return () -> new OptionalBasicScalar<>(new StringWrapper());
  }

  public static Supplier<Scalar<Time, Optional<Time>>> ofTime() {
    return () -> new OptionalBasicScalar<>(new TimeWrapper());
  }

  public static Supplier<Scalar<Timestamp, Optional<Timestamp>>> ofTimestamp() {
    return () -> new OptionalBasicScalar<>(new TimestampWrapper());
  }

  public static Supplier<Scalar<java.util.Date, Optional<java.util.Date>>> ofUtilDate() {
    return () -> new OptionalBasicScalar<>(new UtilDateWrapper());
  }
}
