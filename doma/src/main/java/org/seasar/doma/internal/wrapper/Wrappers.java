/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.lang.reflect.Method;
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
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.message.DomaMessageCode;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.wrapper.ArrayWrapper;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.BigIntegerWrapper;
import org.seasar.doma.wrapper.BlobWrapper;
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
import org.seasar.doma.wrapper.ObjectWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 値を適切なラッパーでラップするためのクラスです。
 * 
 * @author taedium
 * 
 */
public final class Wrappers {

    /**
     * 値をラップします。
     * 
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @return ラッパー
     * @throws WrapperException
     *             ラップに失敗した場合
     */
    public static Wrapper<?> wrap(Object value, Class<?> valueClass) {
        assertNotNull(valueClass);
        assertTrue(value == null
                || ClassUtil.boxIfPrimitive(valueClass).isInstance(value));

        Wrapper<?> result = wrapBasicObject(value, valueClass);
        if (result == null) {
            result = wrapDomainObject(value, valueClass);
            if (result == null) {
                result = new ObjectWrapper(value);
            }
        }
        return result;
    }

    /**
     * 基本型の値をラップします。
     * 
     * @param value
     *            値
     * @param valueClass
     *            値クラス
     * @return ラッパー、値が基本型でない場合 {@code null}
     */
    @SuppressWarnings("unchecked")
    protected static Wrapper<?> wrapBasicObject(Object value,
            Class<?> valueClass) {
        assertNotNull(valueClass);
        Class<?> boxedClass = ClassUtil.boxIfPrimitive(valueClass);
        if (boxedClass == Array.class) {
            return new ArrayWrapper(Array.class.cast(value));
        }
        if (boxedClass == BigDecimal.class) {
            return new BigDecimalWrapper(BigDecimal.class.cast(value));
        }
        if (boxedClass == BigInteger.class) {
            return new BigIntegerWrapper(BigInteger.class.cast(value));
        }
        if (boxedClass == Blob.class) {
            return new BlobWrapper(Blob.class.cast(value));
        }
        if (boxedClass == byte[].class) {
            return new BytesWrapper(byte[].class.cast(value));
        }
        if (boxedClass == Byte.class) {
            return new ByteWrapper(Byte.class.cast(value));
        }
        if (boxedClass == Clob.class) {
            return new ClobWrapper(Clob.class.cast(value));
        }
        if (boxedClass == Date.class) {
            return new DateWrapper(Date.class.cast(value));
        }
        if (boxedClass == Double.class) {
            return new DoubleWrapper(Double.class.cast(value));
        }
        if (boxedClass == Float.class) {
            return new FloatWrapper(Float.class.cast(value));
        }
        if (boxedClass == Integer.class) {
            return new IntegerWrapper(Integer.class.cast(value));
        }
        if (boxedClass == Long.class) {
            return new LongWrapper(Long.class.cast(value));
        }
        if (boxedClass == NClob.class) {
            return new NClobWrapper(NClob.class.cast(value));
        }
        if (boxedClass == Short.class) {
            return new ShortWrapper(Short.class.cast(value));
        }
        if (boxedClass == String.class) {
            return new StringWrapper(String.class.cast(value));
        }
        if (boxedClass == Timestamp.class) {
            return new TimestampWrapper(Timestamp.class.cast(value));
        }
        if (boxedClass == Time.class) {
            return new TimeWrapper(Time.class.cast(value));
        }
        if (boxedClass.isEnum() || Enum.class.isAssignableFrom(boxedClass)) {
            return new EnumWrapper(boxedClass, Enum.class.cast(value));
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
     * @return ラッパー、値がドメインクラスのオブジェクトでない場合 {@code null}
     * @throws WrapperException
     *             ラップに失敗した場合
     */
    protected static Wrapper<?> wrapDomainObject(Object value,
            Class<?> valueClass) {
        assertNotNull(valueClass);
        Domain domain = valueClass.getAnnotation(Domain.class);
        if (domain == null) {
            return null;
        }
        Class<?> domainValueClass = domain.valueType();
        if (domainValueClass == null) {
            return null;
        }
        Object domainValue = null;
        if (value != null) {
            domainValue = getDomainValue(value, valueClass, domain
                    .accessorMethod());
        }
        return wrapBasicObject(domainValue, domainValueClass);
    }

    /**
     * ドメインクラスに管理された値を返します。
     * 
     * @param domainObject
     *            ドメインクラスのオブジェクト
     * @param domainClass
     *            ドメインクラス
     * @param accessorMethodName
     *            ドメインクラスのアクセッサーメソッドの名前
     * @return ドメインクラスに管理された値
     * @throws WrapperException
     *             ラップに失敗した場合
     */
    protected static Object getDomainValue(Object domainObject,
            Class<?> domainClass, String accessorMethodName) {
        assertNotNull(domainObject, domainClass, accessorMethodName);
        try {
            Method method = MethodUtil.getMethod(domainClass,
                    accessorMethodName);
            return MethodUtil.invoke(method, domainObject);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new WrapperException(DomaMessageCode.DOMA1006, cause, cause);
        }
    }
}
