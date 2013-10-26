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
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 値を適切な {@link Scalar} でラップするクラスです。
 * 
 * @author taedium
 * 
 */
public final class Scalars {

    public static Supplier<Scalar<?, ?>> wrap(Object value,
            Class<?> valueClass, boolean optional, ClassHelper classHelper) {
        assertNotNull(valueClass, classHelper);
        Class<?> boxedClass = ClassUtil
                .toBoxedPrimitiveTypeIfPossible(valueClass);
        assertTrue(value == null || boxedClass.isInstance(value));

        if (Scalar.class.isAssignableFrom(boxedClass)) {
            return () -> (Scalar<?, ?>) value;
        }
        Supplier<Scalar<?, ?>> result = wrapBasicObject(value, boxedClass,
                optional, valueClass.isPrimitive());
        if (result == null) {
            result = wrapDomainObject(value, boxedClass, optional, classHelper);
            if (result == null) {
                result = wrapEnumObject(value, boxedClass, optional);
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
     * @param optional
     *            オプション型かどうか
     * @param primitive
     *            プリミティブかどうか
     * @return ラッパー、値が基本型（ただし列挙型を除く）でない場合 {@code null}
     */
    protected static Supplier<Scalar<?, ?>> wrapBasicObject(Object value,
            Class<?> valueClass, boolean optional, boolean primitive) {
        assertNotNull(valueClass);
        if (valueClass == String.class) {
            Supplier<Wrapper<String>> supplier = () -> new org.seasar.doma.wrapper.StringWrapper(
                    (String) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Integer.class) {
            Supplier<Wrapper<Integer>> supplier = () -> new org.seasar.doma.wrapper.IntegerWrapper(
                    (Integer) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Long.class) {
            Supplier<Wrapper<Long>> supplier = () -> new org.seasar.doma.wrapper.LongWrapper(
                    (Long) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == BigDecimal.class) {
            Supplier<Wrapper<BigDecimal>> supplier = () -> new org.seasar.doma.wrapper.BigDecimalWrapper(
                    (BigDecimal) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == java.util.Date.class) {
            Supplier<Wrapper<java.util.Date>> supplier = () -> new org.seasar.doma.wrapper.UtilDateWrapper(
                    (java.util.Date) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Date.class) {
            Supplier<Wrapper<Date>> supplier = () -> new org.seasar.doma.wrapper.DateWrapper(
                    (Date) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Timestamp.class) {
            Supplier<Wrapper<Timestamp>> supplier = () -> new org.seasar.doma.wrapper.TimestampWrapper(
                    (Timestamp) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Time.class) {
            Supplier<Wrapper<Time>> supplier = () -> new org.seasar.doma.wrapper.TimeWrapper(
                    (Time) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Boolean.class) {
            Supplier<Wrapper<Boolean>> supplier = () -> new org.seasar.doma.wrapper.BooleanWrapper(
                    (Boolean) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Array.class) {
            Supplier<Wrapper<Array>> supplier = () -> new org.seasar.doma.wrapper.ArrayWrapper(
                    (Array) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == BigInteger.class) {
            Supplier<Wrapper<BigInteger>> supplier = () -> new org.seasar.doma.wrapper.BigIntegerWrapper(
                    (BigInteger) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Blob.class) {
            Supplier<Wrapper<Blob>> supplier = () -> new org.seasar.doma.wrapper.BlobWrapper(
                    (Blob) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == byte[].class) {
            Supplier<Wrapper<byte[]>> supplier = () -> new org.seasar.doma.wrapper.BytesWrapper(
                    (byte[]) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Byte.class) {
            Supplier<Wrapper<Byte>> supplier = () -> new org.seasar.doma.wrapper.ByteWrapper(
                    (Byte) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Clob.class) {
            Supplier<Wrapper<Clob>> supplier = () -> new org.seasar.doma.wrapper.ClobWrapper(
                    (Clob) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Double.class) {
            Supplier<Wrapper<Double>> supplier = () -> new org.seasar.doma.wrapper.DoubleWrapper(
                    (Double) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Float.class) {
            Supplier<Wrapper<Float>> supplier = () -> new org.seasar.doma.wrapper.FloatWrapper(
                    (Float) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == NClob.class) {
            Supplier<Wrapper<NClob>> supplier = () -> new org.seasar.doma.wrapper.NClobWrapper(
                    (NClob) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        if (valueClass == Short.class) {
            Supplier<Wrapper<Short>> supplier = () -> new org.seasar.doma.wrapper.ShortWrapper(
                    (Short) value);
            return createBasicScalarSupplier(supplier, optional, primitive);
        }
        return null;
    }

    protected static <BASIC> Supplier<Scalar<?, ?>> createBasicScalarSupplier(
            Supplier<Wrapper<BASIC>> wrapperSupplier, boolean optional,
            boolean primitive) {
        if (optional) {
            return () -> new OptionalBasicScalar<BASIC>(wrapperSupplier);
        } else {
            return () -> new BasicScalar<BASIC>(wrapperSupplier, primitive);
        }
    }

    /**
     * 列挙型の値をラップします。
     * 
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @param optional
     *            オプション型かどうか
     * @return ラッパー、値が列挙型でない場合 {@code null}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static Supplier<Scalar<?, ?>> wrapEnumObject(Object value,
            Class<?> valueClass, boolean optional) {
        assertNotNull(valueClass);
        if (valueClass.isEnum() || Enum.class.isAssignableFrom(valueClass)) {
            Supplier<EnumWrapper<?>> supplier = () -> new org.seasar.doma.wrapper.EnumWrapper(
                    valueClass, (Enum) value);
            if (optional) {
                return () -> new OptionalBasicScalar(supplier);
            } else {
                return () -> new BasicScalar(supplier, false);
            }
        }
        return null;
    }

    /**
     * ドメインクラスのオブジェクトをラップします。
     * 
     * @param <BASIC>
     *            基本型
     * @param <DOMAIN>
     *            ドメイン型
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @param optional
     *            オプション型かどうか
     * @param classHelper
     *            クラスヘルパー
     * @return ラッパー、値がドメインクラスのオブジェクトでない場合 {@code null}
     */
    protected static <BASIC, DOMAIN> Supplier<Scalar<?, ?>> wrapDomainObject(
            Object value, Class<DOMAIN> valueClass, boolean optional,
            ClassHelper classHelper) {
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
        DOMAIN domain = valueClass.cast(value);
        if (optional) {
            return () -> domainType.createOptionalScalar(domain);
        } else {
            return () -> domainType.createScalar(domain);
        }
    }
}
