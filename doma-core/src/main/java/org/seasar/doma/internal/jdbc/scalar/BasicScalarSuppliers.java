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

public final class BasicScalarSuppliers {

  public static Supplier<Scalar<Array, Array>> ofArray() {
    return () -> new BasicScalar<>(new ArrayWrapper());
  }

  public static Supplier<Scalar<BigDecimal, BigDecimal>> ofBigDecimal() {
    return () -> new BasicScalar<>(new BigDecimalWrapper());
  }

  public static Supplier<Scalar<BigInteger, BigInteger>> ofBigInteger() {
    return () -> new BasicScalar<>(new BigIntegerWrapper());
  }

  public static Supplier<Scalar<Blob, Blob>> ofBlob() {
    return () -> new BasicScalar<>(new BlobWrapper());
  }

  public static Supplier<Scalar<Boolean, Boolean>> ofBoolean() {
    return () -> new BasicScalar<>(new BooleanWrapper());
  }

  public static Supplier<Scalar<Byte, Byte>> ofByte() {
    return () -> new BasicScalar<>(new ByteWrapper());
  }

  public static Supplier<Scalar<byte[], byte[]>> ofBytes() {
    return () -> new BasicScalar<>(new BytesWrapper());
  }

  public static Supplier<Scalar<Clob, Clob>> ofClob() {
    return () -> new BasicScalar<>(new ClobWrapper());
  }

  public static Supplier<Scalar<Date, Date>> ofDate() {
    return () -> new BasicScalar<>(new DateWrapper());
  }

  public static Supplier<Scalar<Double, Double>> ofDouble() {
    return () -> new BasicScalar<>(new DoubleWrapper());
  }

  public static <E extends Enum<E>> Supplier<Scalar<E, E>> ofEnum(Class<E> enumClass) {
    return () -> new BasicScalar<>(new EnumWrapper<>(enumClass));
  }

  public static Supplier<Scalar<Float, Float>> ofFloat() {
    return () -> new BasicScalar<>(new FloatWrapper());
  }

  public static Supplier<Scalar<Integer, Integer>> ofInteger() {
    return () -> new BasicScalar<>(new IntegerWrapper());
  }

  public static Supplier<Scalar<LocalDate, LocalDate>> ofLocalDate() {
    return () -> new BasicScalar<>(new LocalDateWrapper());
  }

  public static Supplier<Scalar<LocalDateTime, LocalDateTime>> ofLocalDateTime() {
    return () -> new BasicScalar<>(new LocalDateTimeWrapper());
  }

  public static Supplier<Scalar<LocalTime, LocalTime>> ofLocalTime() {
    return () -> new BasicScalar<>(new LocalTimeWrapper());
  }

  public static Supplier<Scalar<Long, Long>> ofLong() {
    return () -> new BasicScalar<>(new LongWrapper());
  }

  public static Supplier<Scalar<NClob, NClob>> ofNClob() {
    return () -> new BasicScalar<>(new NClobWrapper());
  }

  public static Supplier<Scalar<Object, Object>> ofObject() {
    return () -> new BasicScalar<>(new ObjectWrapper());
  }

  public static Supplier<Scalar<Boolean, Boolean>> ofPrimitiveBoolean() {
    return () -> new BasicScalar<>(new PrimitiveBooleanWrapper());
  }

  public static Supplier<Scalar<Byte, Byte>> ofPrimitiveByte() {
    return () -> new BasicScalar<>(new PrimitiveByteWrapper());
  }

  public static Supplier<Scalar<Double, Double>> ofPrimitiveDouble() {
    return () -> new BasicScalar<>(new PrimitiveDoubleWrapper());
  }

  public static Supplier<Scalar<Float, Float>> ofPrimitiveFloat() {
    return () -> new BasicScalar<>(new PrimitiveFloatWrapper());
  }

  public static Supplier<Scalar<Integer, Integer>> ofPrimitiveInt() {
    return () -> new BasicScalar<>(new PrimitiveIntWrapper());
  }

  public static Supplier<Scalar<Long, Long>> ofPrimitiveLong() {
    return () -> new BasicScalar<>(new PrimitiveLongWrapper());
  }

  public static Supplier<Scalar<Short, Short>> ofPrimitiveShort() {
    return () -> new BasicScalar<>(new PrimitiveShortWrapper());
  }

  public static Supplier<Scalar<Short, Short>> ofShort() {
    return () -> new BasicScalar<>(new ShortWrapper());
  }

  public static Supplier<Scalar<SQLXML, SQLXML>> ofSQLXML() {
    return () -> new BasicScalar<>(new SQLXMLWrapper());
  }

  public static Supplier<Scalar<String, String>> ofString() {
    return () -> new BasicScalar<>(new StringWrapper());
  }

  public static Supplier<Scalar<Time, Time>> ofTime() {
    return () -> new BasicScalar<>(new TimeWrapper());
  }

  public static Supplier<Scalar<Timestamp, Timestamp>> ofTimestamp() {
    return () -> new BasicScalar<>(new TimestampWrapper());
  }

  public static Supplier<Scalar<java.util.Date, java.util.Date>> ofUtilDate() {
    return () -> new BasicScalar<>(new UtilDateWrapper());
  }
}
