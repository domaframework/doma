/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.scalar;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.function.Supplier;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.util.ClassUtil;
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
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.NClobWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 値を適切な {@link Scalar} でラップするクラスです。
 * 
 * @author taedium
 * 
 */
public final class Scalars {

    /**
     * 値をラップします。
     * 
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @param classHelper
     *            クラスヘルパー
     * @return ラッパー
     * @throws ScalarException
     *             ラップに失敗した場合
     */
    public static Supplier<Scalar<?, ?>> wrap(Object value,
            Class<?> valueClass, ClassHelper classHelper) {
        assertNotNull(valueClass, classHelper);
        Class<?> boxedClass = ClassUtil
                .toBoxedPrimitiveTypeIfPossible(valueClass);
        assertTrue(value == null || boxedClass.isInstance(value));

        if (Scalar.class.isAssignableFrom(boxedClass)) {
            return () -> (Scalar<?, ?>) value;
        }
        Supplier<Scalar<?, ?>> result = wrapBasicObject(value, boxedClass);
        if (result == null) {
            result = wrapDomainObject(value, boxedClass, classHelper);
            if (result == null) {
                result = wrapEnumObject(value, boxedClass);
                if (result == null) {
                    throw new ScalarException(Message.DOMA1007,
                            valueClass.getName(), value);
                }
            }
        }
        return result;
    }

    /**
     * 基本型（ただし列挙型を除く）の値をラップします。
     * 
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @return ラッパー、値が基本型（ただし列挙型を除く）でない場合 {@code null}
     */
    protected static Supplier<Scalar<?, ?>> wrapBasicObject(Object value,
            Class<?> valueClass) {
        assertNotNull(valueClass);
        if (valueClass == String.class) {
            Wrapper<String> wrapper = new StringWrapper((String) value);
            return () -> new BasicScalar<String>(() -> wrapper, false);
        }
        if (valueClass == Integer.class) {
            IntegerWrapper wrapper = new IntegerWrapper((Integer) value);
            return () -> new BasicScalar<Integer>(() -> wrapper, false);
        }
        if (valueClass == Long.class) {
            LongWrapper wrapper = new LongWrapper((Long) value);
            return () -> new BasicScalar<Long>(() -> wrapper, false);
        }
        if (valueClass == BigDecimal.class) {
            BigDecimalWrapper wrapper = new BigDecimalWrapper(
                    (BigDecimal) value);
            return () -> new BasicScalar<BigDecimal>(() -> wrapper, false);
        }
        if (valueClass == java.util.Date.class) {
            UtilDateWrapper wrapper = new UtilDateWrapper(
                    (java.util.Date) value);
            return () -> new BasicScalar<java.util.Date>(() -> wrapper, false);
        }
        if (valueClass == Date.class) {
            DateWrapper wrapper = new DateWrapper((Date) value);
            return () -> new BasicScalar<Date>(() -> wrapper, false);
        }
        if (valueClass == Timestamp.class) {
            TimestampWrapper wrapper = new TimestampWrapper((Timestamp) value);
            return () -> new BasicScalar<Timestamp>(() -> wrapper, false);
        }
        if (valueClass == Time.class) {
            TimeWrapper wrapper = new TimeWrapper((Time) value);
            return () -> new BasicScalar<Time>(() -> wrapper, false);
        }
        if (valueClass == Boolean.class) {
            BooleanWrapper wrapper = new BooleanWrapper((Boolean) value);
            return () -> new BasicScalar<Boolean>(() -> wrapper, false);
        }
        if (valueClass == Array.class) {
            ArrayWrapper wrapper = new ArrayWrapper((Array) value);
            return () -> new BasicScalar<Array>(() -> wrapper, false);
        }
        if (valueClass == BigInteger.class) {
            BigIntegerWrapper wrapper = new BigIntegerWrapper(
                    (BigInteger) value);
            return () -> new BasicScalar<BigInteger>(() -> wrapper, false);
        }
        if (valueClass == Blob.class) {
            BlobWrapper wrapper = new BlobWrapper((Blob) value);
            return () -> new BasicScalar<Blob>(() -> wrapper, false);
        }
        if (valueClass == byte[].class) {
            BytesWrapper wrapper = new BytesWrapper((byte[]) value);
            return () -> new BasicScalar<byte[]>(() -> wrapper, false);
        }
        if (valueClass == Byte.class) {
            ByteWrapper wrapper = new ByteWrapper((Byte) value);
            return () -> new BasicScalar<Byte>(() -> wrapper, false);
        }
        if (valueClass == Clob.class) {
            ClobWrapper wrapper = new ClobWrapper((Clob) value);
            return () -> new BasicScalar<Clob>(() -> wrapper, false);
        }
        if (valueClass == Double.class) {
            DoubleWrapper wrapper = new DoubleWrapper((Double) value);
            return () -> new BasicScalar<Double>(() -> wrapper, false);
        }
        if (valueClass == Float.class) {
            FloatWrapper wrapper = new FloatWrapper((Float) value);
            return () -> new BasicScalar<Float>(() -> wrapper, false);
        }
        if (valueClass == NClob.class) {
            NClobWrapper wrapper = new NClobWrapper((NClob) value);
            return () -> new BasicScalar<NClob>(() -> wrapper, false);
        }
        if (valueClass == Short.class) {
            ShortWrapper wrapper = new ShortWrapper((Short) value);
            return () -> new BasicScalar<Short>(() -> wrapper, false);
        }
        return null;
    }

    /**
     * 列挙型の値をラップします。
     * 
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @return ラッパー、値が列挙型でない場合 {@code null}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static Supplier<Scalar<?, ?>> wrapEnumObject(Object value,
            Class<?> valueClass) {
        assertNotNull(valueClass);
        if (valueClass.isEnum() || Enum.class.isAssignableFrom(valueClass)) {
            EnumWrapper<?> wrapper = new EnumWrapper(valueClass, (Enum) value);
            return () -> new BasicScalar(() -> wrapper, false);
        }
        return null;
    }

    /**
     * ドメインクラスのオブジェクトをラップします。
     * 
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @param classHelper
     *            クラスヘルパー
     * @return ラッパー、値がドメインクラスのオブジェクトでない場合 {@code null}
     */
    protected static <BASIC, DOMAIN> Supplier<Scalar<?, ?>> wrapDomainObject(
            Object value, Class<DOMAIN> valueClass, ClassHelper classHelper) {
        DomainType<BASIC, DOMAIN> domainType;
        if (valueClass.isAnnotationPresent(Domain.class)) {
            domainType = DomainTypeFactory.getDomainType(valueClass,
                    classHelper);
        } else {
            domainType = DomainTypeFactory.getExternalDomainType(valueClass,
                    classHelper);
        }
        if (domainType == null) {
            return null;
        }
        return () -> {
            Scalar<BASIC, DOMAIN> scalar = domainType.createScalar();
            scalar.set(scalar.cast(value));
            return scalar;
        };
    }
}
