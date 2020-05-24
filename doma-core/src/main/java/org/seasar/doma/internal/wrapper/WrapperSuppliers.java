package org.seasar.doma.internal.wrapper;

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

public final class WrapperSuppliers {

  public static Supplier<Wrapper<Array>> ofArray() {
    return ArrayWrapper::new;
  }

  public static Supplier<Wrapper<BigDecimal>> ofBigDecimal() {
    return BigDecimalWrapper::new;
  }

  public static Supplier<Wrapper<BigInteger>> ofBigInteger() {
    return BigIntegerWrapper::new;
  }

  public static Supplier<Wrapper<Blob>> ofBlob() {
    return BlobWrapper::new;
  }

  public static Supplier<Wrapper<Boolean>> ofBoolean() {
    return BooleanWrapper::new;
  }

  public static Supplier<Wrapper<Byte>> ofByte() {
    return ByteWrapper::new;
  }

  public static Supplier<Wrapper<byte[]>> ofBytes() {
    return BytesWrapper::new;
  }

  public static Supplier<Wrapper<Clob>> ofClob() {
    return ClobWrapper::new;
  }

  public static Supplier<Wrapper<Date>> ofDate() {
    return DateWrapper::new;
  }

  public static Supplier<Wrapper<Double>> ofDouble() {
    return DoubleWrapper::new;
  }

  public static <E extends Enum<E>> Supplier<Wrapper<E>> ofEnum(Class<E> enumClass) {
    return () -> new EnumWrapper<>(enumClass);
  }

  public static Supplier<Wrapper<Float>> ofFloat() {
    return FloatWrapper::new;
  }

  public static Supplier<Wrapper<Integer>> ofInteger() {
    return IntegerWrapper::new;
  }

  public static Supplier<Wrapper<LocalDate>> ofLocalDate() {
    return LocalDateWrapper::new;
  }

  public static Supplier<Wrapper<LocalDateTime>> ofLocalDateTime() {
    return LocalDateTimeWrapper::new;
  }

  public static Supplier<Wrapper<LocalTime>> ofLocalTime() {
    return LocalTimeWrapper::new;
  }

  public static Supplier<Wrapper<Long>> ofLong() {
    return LongWrapper::new;
  }

  public static Supplier<Wrapper<NClob>> ofNClob() {
    return NClobWrapper::new;
  }

  public static Supplier<Wrapper<Object>> ofObject() {
    return ObjectWrapper::new;
  }

  public static Supplier<Wrapper<Boolean>> ofPrimitiveBoolean() {
    return PrimitiveBooleanWrapper::new;
  }

  public static Supplier<Wrapper<Byte>> ofPrimitiveByte() {
    return PrimitiveByteWrapper::new;
  }

  public static Supplier<Wrapper<Double>> ofPrimitiveDouble() {
    return PrimitiveDoubleWrapper::new;
  }

  public static Supplier<Wrapper<Float>> ofPrimitiveFloat() {
    return PrimitiveFloatWrapper::new;
  }

  public static Supplier<Wrapper<Integer>> ofPrimitiveInt() {
    return PrimitiveIntWrapper::new;
  }

  public static Supplier<Wrapper<Long>> ofPrimitiveLong() {
    return PrimitiveLongWrapper::new;
  }

  public static Supplier<Wrapper<Short>> ofPrimitiveShort() {
    return PrimitiveShortWrapper::new;
  }

  public static Supplier<Wrapper<Short>> ofShort() {
    return ShortWrapper::new;
  }

  public static Supplier<Wrapper<SQLXML>> ofSQLXML() {
    return SQLXMLWrapper::new;
  }

  public static Supplier<Wrapper<String>> ofString() {
    return StringWrapper::new;
  }

  public static Supplier<Wrapper<Time>> ofTime() {
    return TimeWrapper::new;
  }

  public static Supplier<Wrapper<Timestamp>> ofTimestamp() {
    return TimestampWrapper::new;
  }

  public static Supplier<Wrapper<java.util.Date>> ofUtilDate() {
    return UtilDateWrapper::new;
  }
}
