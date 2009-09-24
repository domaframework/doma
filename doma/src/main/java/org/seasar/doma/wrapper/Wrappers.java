package org.seasar.doma.wrapper;

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
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.message.DomaMessageCode;

public final class Wrappers {

    public static Wrapper<?> wrap(Object value, Class<?> valueClass) {
        assertNotNull(valueClass);
        assertTrue(value == null || valueClass.isInstance(value));

        Wrapper<?> result = wrapBasicObject(value, valueClass);
        if (result == null) {
            result = wrapDomainObject(value, valueClass);
            if (result == null) {
                result = new ObjectWrapper(value);
            }
        }
        return result;
    }

    protected static Wrapper<?> wrapBasicObject(Object value,
            Class<?> valueClass) {
        Class<?> wrapperClass = ClassUtil
                .getWrapperClassIfPrimitive(valueClass);
        if (wrapperClass == Array.class) {
            return new ArrayWrapper(Array.class.cast(value));
        }
        if (wrapperClass == BigDecimal.class) {
            return new BigDecimalWrapper(BigDecimal.class.cast(value));
        }
        if (wrapperClass == BigInteger.class) {
            return new BigIntegerWrapper(BigInteger.class.cast(value));
        }
        if (wrapperClass == Blob.class) {
            return new BlobWrapper(Blob.class.cast(value));
        }
        if (wrapperClass == byte[].class) {
            return new BytesWrapper(byte[].class.cast(value));
        }
        if (wrapperClass == Byte.class) {
            return new ByteWrapper(Byte.class.cast(value));
        }
        if (wrapperClass == Clob.class) {
            return new ClobWrapper(Clob.class.cast(value));
        }
        if (wrapperClass == Date.class) {
            return new DateWrapper(Date.class.cast(value));
        }
        if (wrapperClass == Double.class) {
            return new DoubleWrapper(Double.class.cast(value));
        }
        if (wrapperClass == Float.class) {
            return new FloatWrapper(Float.class.cast(value));
        }
        if (wrapperClass == Integer.class) {
            return new IntegerWrapper(Integer.class.cast(value));
        }
        if (wrapperClass == Long.class) {
            return new LongWrapper(Long.class.cast(value));
        }
        if (wrapperClass == NClob.class) {
            return new NClobWrapper(NClob.class.cast(value));
        }
        if (wrapperClass == Short.class) {
            return new ShortWrapper(Short.class.cast(value));
        }
        if (wrapperClass == String.class) {
            return new StringWrapper(String.class.cast(value));
        }
        if (wrapperClass == Timestamp.class) {
            return new TimestampWrapper(Timestamp.class.cast(value));
        }
        if (wrapperClass == Time.class) {
            return new TimeWrapper(Time.class.cast(value));
        }
        return null;
    }

    protected static Wrapper<?> wrapDomainObject(Object value,
            Class<?> valueClass) {
        Domain domain = valueClass.getAnnotation(Domain.class);
        if (domain == null) {
            return null;
        }
        Object domainValue = getDomainValue(value, valueClass, domain
                .accessorMethod());
        Class<?> domainValueClass = domain.valueType();
        return wrapBasicObject(domainValue, domainValueClass);
    }

    protected static Object getDomainValue(Object domainObject,
            Class<?> domainClass, String accessorMethodName) {
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
