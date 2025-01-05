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
package org.seasar.doma.jdbc;

import java.util.Optional;
import org.seasar.doma.jdbc.type.JdbcType;
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
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * A visitor that converts the {@link Wrapper} values to the SQL log formats.
 *
 * <p>The implementation class must be thread safe.
 */
public interface SqlLogFormattingVisitor
    extends WrapperVisitor<String, SqlLogFormattingFunction, JdbcMappingHint, RuntimeException> {

  @Deprecated
  default String visitArrayWrapper(
      ArrayWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitBigDecimalWrapper(
      BigDecimalWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitBigIntegerWrapper(
      BigIntegerWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitBlobWrapper(
      BlobWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitBooleanWrapper(
      BooleanWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitBytesWrapper(
      BytesWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitByteWrapper(
      ByteWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitClobWrapper(
      ClobWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitDateWrapper(
      DateWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitDoubleWrapper(
      DoubleWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default <E extends Enum<E>> String visitEnumWrapper(
      EnumWrapper<E> wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitFloatWrapper(
      FloatWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitIntegerWrapper(
      IntegerWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitLocalDateWrapper(
      LocalDateWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitLocalDateTimeWrapper(
      LocalDateTimeWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitLocalTimeWrapper(
      LocalTimeWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitLongWrapper(
      LongWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitNClobWrapper(
      NClobWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitObjectWrapper(
      ObjectWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitPrimitiveBooleanWrapper(
      PrimitiveBooleanWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      Void unused)
      throws RuntimeException {
    return visitBooleanWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Deprecated
  default String visitPrimitiveByteWrapper(
      PrimitiveByteWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return visitByteWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Deprecated
  default String visitPrimitiveDoubleWrapper(
      PrimitiveDoubleWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      Void unused)
      throws RuntimeException {
    return visitDoubleWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Deprecated
  default String visitPrimitiveFloatWrapper(
      PrimitiveFloatWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return visitFloatWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Deprecated
  default String visitPrimitiveIntWrapper(
      PrimitiveIntWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return visitIntegerWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Deprecated
  default String visitPrimitiveLongWrapper(
      PrimitiveLongWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return visitLongWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Deprecated
  default String visitPrimitiveShortWrapper(
      PrimitiveShortWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return visitShortWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Deprecated
  default String visitShortWrapper(
      ShortWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitStringWrapper(
      StringWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitSQLXMLWrapper(
      SQLXMLWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitTimestampWrapper(
      TimestampWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitTimeWrapper(
      TimeWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Deprecated
  default String visitUtilDateWrapper(
      UtilDateWrapper wrapper, SqlLogFormattingFunction sqlLogFormattingFunction, Void unused)
      throws RuntimeException {
    return null;
  }

  @Override
  default String visitArrayWrapper(
      ArrayWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitArrayWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitBigDecimalWrapper(
      BigDecimalWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitBigDecimalWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitBigIntegerWrapper(
      BigIntegerWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitBigIntegerWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitBlobWrapper(
      BlobWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitBlobWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitBooleanWrapper(
      BooleanWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitBooleanWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitBytesWrapper(
      BytesWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitBytesWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitByteWrapper(
      ByteWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitByteWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitClobWrapper(
      ClobWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitClobWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitDateWrapper(
      DateWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitDateWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitDoubleWrapper(
      DoubleWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitDoubleWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default <E extends Enum<E>> String visitEnumWrapper(
      EnumWrapper<E> wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitEnumWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitFloatWrapper(
      FloatWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitFloatWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitIntegerWrapper(
      IntegerWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitIntegerWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitLocalDateWrapper(
      LocalDateWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitLocalDateWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitLocalDateTimeWrapper(
      LocalDateTimeWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitLocalDateTimeWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitLocalTimeWrapper(
      LocalTimeWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitLocalTimeWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitLongWrapper(
      LongWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitLongWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitNClobWrapper(
      NClobWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitNClobWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  /**
   * Processes the given ObjectWrapper using the provided SqlLogFormattingFunction and evaluates an
   * optional JdbcMappingHint for JDBC type handling.
   *
   * @param wrapper the ObjectWrapper to be processed
   * @param sqlLogFormattingFunction the function to be applied for SQL log formatting
   * @param jdbcMappingHint an optional hint for mapping JDBC types, may be null
   * @return the result of applying the SqlLogFormattingFunction to the ObjectWrapper
   * @throws RuntimeException if an error occurs during processing
   */
  @Override
  default String visitObjectWrapper(
      ObjectWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    if (jdbcMappingHint != null) {
      Optional<JdbcType<Object>> jdbcType = jdbcMappingHint.getJdbcType();
      if (jdbcType.isPresent()) {
        return sqlLogFormattingFunction.apply(wrapper, jdbcType.get());
      }
    }
    return visitObjectWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitPrimitiveBooleanWrapper(
      PrimitiveBooleanWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitPrimitiveBooleanWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitPrimitiveByteWrapper(
      PrimitiveByteWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitPrimitiveByteWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitPrimitiveDoubleWrapper(
      PrimitiveDoubleWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitPrimitiveDoubleWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitPrimitiveFloatWrapper(
      PrimitiveFloatWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitPrimitiveFloatWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitPrimitiveIntWrapper(
      PrimitiveIntWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitPrimitiveIntWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitPrimitiveLongWrapper(
      PrimitiveLongWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitPrimitiveLongWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitPrimitiveShortWrapper(
      PrimitiveShortWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitPrimitiveShortWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitShortWrapper(
      ShortWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitShortWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitStringWrapper(
      StringWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitStringWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitSQLXMLWrapper(
      SQLXMLWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitSQLXMLWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitTimestampWrapper(
      TimestampWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitTimestampWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitTimeWrapper(
      TimeWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitTimeWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }

  @Override
  default String visitUtilDateWrapper(
      UtilDateWrapper wrapper,
      SqlLogFormattingFunction sqlLogFormattingFunction,
      JdbcMappingHint jdbcMappingHint)
      throws RuntimeException {
    return visitUtilDateWrapper(wrapper, sqlLogFormattingFunction, (Void) null);
  }
}
