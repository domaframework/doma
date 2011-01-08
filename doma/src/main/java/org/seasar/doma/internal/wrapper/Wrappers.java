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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
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
     * @return ラッパー
     * @throws WrapperException
     *             ラップに失敗した場合
     */
    public static Wrapper<?> wrap(Object value, Class<?> valueClass) {
        assertNotNull(valueClass);
        assertTrue(value == null
                || ClassUtil.toBoxedPrimitiveTypeIfPossible(valueClass)
                        .isInstance(value));

        if (Wrapper.class.isAssignableFrom(valueClass)) {
            return (Wrapper<?>) value;
        }
        Wrapper<?> result = wrapBasicObject(value, valueClass);
        if (result == null) {
            result = wrapDomainObject(value, valueClass);
            if (result == null) {
                result = wrapEnumObject(value, valueClass);
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
        Class<?> boxedClass = ClassUtil
                .toBoxedPrimitiveTypeIfPossible(valueClass);
        if (boxedClass == String.class) {
            return new StringWrapper((String) value);
        }
        if (boxedClass == Integer.class) {
            return new IntegerWrapper((Integer) value);
        }
        if (boxedClass == Long.class) {
            return new LongWrapper((Long) value);
        }
        if (boxedClass == BigDecimal.class) {
            return new BigDecimalWrapper((BigDecimal) value);
        }
        if (boxedClass == java.util.Date.class) {
            return new UtilDateWrapper((java.util.Date) value);
        }
        if (boxedClass == Date.class) {
            return new DateWrapper((Date) value);
        }
        if (boxedClass == Timestamp.class) {
            return new TimestampWrapper((Timestamp) value);
        }
        if (boxedClass == Time.class) {
            return new TimeWrapper((Time) value);
        }
        if (boxedClass == Boolean.class) {
            return new BooleanWrapper((Boolean) value);
        }
        if (boxedClass == Array.class) {
            return new ArrayWrapper((Array) value);
        }
        if (boxedClass == BigInteger.class) {
            return new BigIntegerWrapper((BigInteger) value);
        }
        if (boxedClass == Blob.class) {
            return new BlobWrapper((Blob) value);
        }
        if (boxedClass == byte[].class) {
            return new BytesWrapper((byte[]) value);
        }
        if (boxedClass == Byte.class) {
            return new ByteWrapper((Byte) value);
        }
        if (boxedClass == Clob.class) {
            return new ClobWrapper((Clob) value);
        }
        if (boxedClass == Double.class) {
            return new DoubleWrapper((Double) value);
        }
        if (boxedClass == Float.class) {
            return new FloatWrapper((Float) value);
        }
        if (boxedClass == NClob.class) {
            return new NClobWrapper((NClob) value);
        }
        if (boxedClass == Short.class) {
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
        Class<?> boxedClass = ClassUtil
                .toBoxedPrimitiveTypeIfPossible(valueClass);
        if (boxedClass.isEnum() || Enum.class.isAssignableFrom(boxedClass)) {
            return new EnumWrapper(boxedClass, (Enum) value);
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
        DomainDesc domainDesc = getDomainDesc(valueClass);
        if (domainDesc == null) {
            return null;
        }
        Object domainValue = null;
        if (value != null) {
            domainValue = getDomainValue(value, valueClass,
                    domainDesc.accessorMethod);
        }
        Wrapper<?> result = wrapBasicObject(domainValue, domainDesc.valueType);
        if (result == null) {
            result = wrapEnumObject(domainValue, domainDesc.valueType);
        }
        return result;
    }

    /**
     * ドメイン記述を返します。
     * 
     * @param clazz
     *            ドメインクラス
     * @return ドメイン記述
     */
    protected static DomainDesc getDomainDesc(Class<?> clazz) {
        assertNotNull(clazz);
        Domain domain = clazz.getAnnotation(Domain.class);
        if (domain != null) {
            return new DomainDesc(domain.valueType(), domain.accessorMethod());
        }
        EnumDomain enumDomain = clazz.getAnnotation(EnumDomain.class);
        if (enumDomain != null) {
            return new DomainDesc(enumDomain.valueType(),
                    enumDomain.accessorMethod());
        }
        return null;
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
            Method method = ClassUtil.getDeclaredMethod(domainClass,
                    accessorMethodName);
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            method.setAccessible(true);
            return MethodUtil.invoke(method, domainObject);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new WrapperException(Message.DOMA1006, cause, cause);
        }
    }

    /**
     * ドメイン記述。 {@link Domain} と {@link EnumDomain} を抽象化します。
     */
    private static class DomainDesc {

        private final Class<?> valueType;

        private final String accessorMethod;

        private DomainDesc(Class<?> valueType, String accessorMethod) {
            assertNotNull(valueType, accessorMethod);
            this.valueType = valueType;
            this.accessorMethod = accessorMethod;
        }
    }
}
