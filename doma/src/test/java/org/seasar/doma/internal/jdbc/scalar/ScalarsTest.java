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

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.scalar.Scalars;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

import example.domain.InternationalPhoneNumber;
import example.domain.PhoneNumber;

/**
 * @author taedium
 * 
 */
public class ScalarsTest extends TestCase {

    private final ClassHelper classHelper = new ClassHelper() {
    };

    public void testWrap() throws Exception {
        assertNotNull(Scalars.wrap(true, boolean.class, classHelper));
        assertNotNull(Scalars.wrap(true, Boolean.class, classHelper));
        assertNotNull(Scalars.wrap((byte) 1, byte.class, classHelper));
        assertNotNull(Scalars
                .wrap(new Byte((byte) 1), Byte.class, classHelper));
        assertNotNull(Scalars.wrap((short) 1, short.class, classHelper));
        assertNotNull(Scalars.wrap(new Short((short) 1), Short.class,
                classHelper));
        assertNotNull(Scalars.wrap(1, int.class, classHelper));
        assertNotNull(Scalars.wrap(new Integer(1), Integer.class, classHelper));
        assertNotNull(Scalars.wrap(1L, long.class, classHelper));
        assertNotNull(Scalars.wrap(new Long(1), Long.class, classHelper));
        assertNotNull(Scalars.wrap(1f, float.class, classHelper));
        assertNotNull(Scalars.wrap(new Float(1), Float.class, classHelper));
        assertNotNull(Scalars.wrap(1d, double.class, classHelper));
        assertNotNull(Scalars.wrap(new Double(1), Double.class, classHelper));
        assertNotNull(Scalars
                .wrap(new byte[] { 1 }, byte[].class, classHelper));
        assertNotNull(Scalars.wrap("", String.class, classHelper));
        assertNotNull(Scalars.wrap(new BigDecimal("1"), BigDecimal.class,
                classHelper));
        assertNotNull(Scalars.wrap(new BigInteger("1"), BigInteger.class,
                classHelper));
        assertNotNull(Scalars.wrap(Date.valueOf("2009-01-23"), Date.class,
                classHelper));
        assertNotNull(Scalars.wrap(Time.valueOf("12:34:56"), Time.class,
                classHelper));
        assertNotNull(Scalars.wrap(Timestamp.valueOf("2009-01-23 12:34:56"),
                Timestamp.class, classHelper));
        assertNotNull(Scalars.wrap(new java.util.Date(), java.util.Date.class,
                classHelper));
        assertNotNull(Scalars.wrap(null, Array.class, classHelper));
        assertNotNull(Scalars.wrap(null, Blob.class, classHelper));
        assertNotNull(Scalars.wrap(null, Clob.class, classHelper));
        assertNotNull(Scalars.wrap(null, NClob.class, classHelper));
    }

    public void testWrapBasic_boxedValue_primitiveType() throws Exception {
        Supplier<Scalar<?, ?>> supplier = Scalars.wrap(new Integer(10),
                int.class, classHelper);
        assertNotNull(supplier);
        Wrapper<?> wrapper = supplier.get().getWrapper();
        assertEquals(IntegerWrapper.class, wrapper.getClass());
        assertEquals(new Integer(10), wrapper.get());
    }

    public void testWrapBasic_null() throws Exception {
        Supplier<Scalar<?, ?>> supplier = Scalars.wrap(null, Integer.class,
                classHelper);
        assertNotNull(supplier);
        Wrapper<?> wrapper = supplier.get().getWrapper();
        assertEquals(IntegerWrapper.class, wrapper.getClass());
        assertNull(null, wrapper.get());
    }

    public void testWrapEnum() throws Exception {
        Supplier<Scalar<?, ?>> supplier = Scalars.wrap(MyEnum.AAA,
                MyEnum.class, classHelper);
        assertNotNull(supplier);
        Wrapper<?> wrapper = supplier.get().getWrapper();
        assertEquals(EnumWrapper.class, wrapper.getClass());
        assertEquals(MyEnum.AAA, wrapper.get());
    }

    public void testWrapEnum_null() throws Exception {
        Supplier<Scalar<?, ?>> supplier = Scalars.wrap(null, MyEnum.class,
                classHelper);
        assertNotNull(supplier);
        Wrapper<?> wrapper = supplier.get().getWrapper();
        assertEquals(EnumWrapper.class, wrapper.getClass());
        assertNull(wrapper.get());
    }

    public void testWrapDomain() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber("123-456-789");
        Supplier<Scalar<?, ?>> supplier = Scalars.wrap(phoneNumber,
                PhoneNumber.class, classHelper);
        assertNotNull(supplier);
        Wrapper<?> wrapper = supplier.get().getWrapper();
        assertEquals(StringWrapper.class, wrapper.getClass());
        assertEquals("123-456-789", wrapper.get());
    }

    public void testWrapDomain_subclass() throws Exception {
        PhoneNumber phoneNumber = new InternationalPhoneNumber("123-456-789");
        Supplier<Scalar<?, ?>> supplier = Scalars.wrap(phoneNumber,
                InternationalPhoneNumber.class, classHelper);
        assertNotNull(supplier);
        Wrapper<?> wrapper = supplier.get().getWrapper();
        assertEquals(StringWrapper.class, wrapper.getClass());
        assertEquals("123-456-789", wrapper.get());
    }

    public void testWrapDomain_null() throws Exception {
        Supplier<Scalar<?, ?>> supplier = Scalars.wrap(null,
                PhoneNumber.class, classHelper);
        assertNotNull(supplier);
        Wrapper<?> wrapper = supplier.get().getWrapper();
        assertEquals(StringWrapper.class, wrapper.getClass());
        assertNull(wrapper.get());
    }

    public enum MyEnum {
        AAA, BBB, CCC
    }
}
