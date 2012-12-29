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
package org.seasar.doma.internal.wrapper;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;

import org.seasar.doma.Domain;
import org.seasar.doma.EnumDomain;
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
 * 値を適切なラッパーでラップするためのクラスです。
 * 
 * @author taedium
 * 
 */
@SuppressWarnings("deprecation")
public final class Wrappers {

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
     * @throws WrapperException
     *             ラップに失敗した場合
     */
    public static Wrapper<?> wrap(Object value, Class<?> valueClass,
            ClassHelper classHelper) {
        assertNotNull(valueClass, classHelper);
        Class<?> boxedClass = ClassUtil
                .toBoxedPrimitiveTypeIfPossible(valueClass);
        assertTrue(value == null || boxedClass.isInstance(value));

        if (Wrapper.class.isAssignableFrom(boxedClass)) {
            return (Wrapper<?>) value;
        }
        Wrapper<?> result = wrapBasicObject(value, boxedClass);
        if (result == null) {
            result = wrapDomainObject(value, boxedClass, classHelper);
            if (result == null) {
                result = wrapEnumObject(value, boxedClass);
                if (result == null) {
                    throw new WrapperException(Message.DOMA1007,
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
    protected static Wrapper<?> wrapBasicObject(Object value,
            Class<?> valueClass) {
        assertNotNull(valueClass);
        if (valueClass == String.class) {
            return new StringWrapper((String) value);
        }
        if (valueClass == Integer.class) {
            return new IntegerWrapper((Integer) value);
        }
        if (valueClass == Long.class) {
            return new LongWrapper((Long) value);
        }
        if (valueClass == BigDecimal.class) {
            return new BigDecimalWrapper((BigDecimal) value);
        }
        if (valueClass == java.util.Date.class) {
            return new UtilDateWrapper((java.util.Date) value);
        }
        if (valueClass == Date.class) {
            return new DateWrapper((Date) value);
        }
        if (valueClass == Timestamp.class) {
            return new TimestampWrapper((Timestamp) value);
        }
        if (valueClass == Time.class) {
            return new TimeWrapper((Time) value);
        }
        if (valueClass == Boolean.class) {
            return new BooleanWrapper((Boolean) value);
        }
        if (valueClass == Array.class) {
            return new ArrayWrapper((Array) value);
        }
        if (valueClass == BigInteger.class) {
            return new BigIntegerWrapper((BigInteger) value);
        }
        if (valueClass == Blob.class) {
            return new BlobWrapper((Blob) value);
        }
        if (valueClass == byte[].class) {
            return new BytesWrapper((byte[]) value);
        }
        if (valueClass == Byte.class) {
            return new ByteWrapper((Byte) value);
        }
        if (valueClass == Clob.class) {
            return new ClobWrapper((Clob) value);
        }
        if (valueClass == Double.class) {
            return new DoubleWrapper((Double) value);
        }
        if (valueClass == Float.class) {
            return new FloatWrapper((Float) value);
        }
        if (valueClass == NClob.class) {
            return new NClobWrapper((NClob) value);
        }
        if (valueClass == Short.class) {
            return new ShortWrapper((Short) value);
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
    protected static Wrapper<?> wrapEnumObject(Object value, Class<?> valueClass) {
        assertNotNull(valueClass);
        if (valueClass.isEnum() || Enum.class.isAssignableFrom(valueClass)) {
            return new EnumWrapper(valueClass, (Enum) value);
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
    protected static <V, D> Wrapper<?> wrapDomainObject(Object value,
            Class<D> valueClass, ClassHelper classHelper) {
        DomainType<V, D> domainType;
        if (valueClass.isAnnotationPresent(Domain.class)
                || valueClass.isAnnotationPresent(EnumDomain.class)) {
            domainType = DomainTypeFactory.getDomainType(valueClass,
                    classHelper);
        } else {
            domainType = DomainTypeFactory.getExternalDomainType(valueClass,
                    classHelper);
        }
        if (domainType == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        V domainValue = domainType.getWrapper((D) value).get();
        Class<V> domainValueClass = domainType.getValueClass();
        Wrapper<?> result = wrapBasicObject(domainValue, domainValueClass);
        if (result == null) {
            result = wrapEnumObject(domainValue, domainValueClass);
        }
        return result;
    }

}
