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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.DefaultClassHelper;
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
public class WrappersTest extends TestCase {

    private final ClassHelper classHelper = new DefaultClassHelper();

    public void testWrap() throws Exception {
        assertNotNull(Wrappers.wrap(true, boolean.class, classHelper));
        assertNotNull(Wrappers.wrap(true, Boolean.class, classHelper));
        assertNotNull(Wrappers.wrap((byte) 1, byte.class, classHelper));
        assertNotNull(Wrappers
                .wrap(new Byte((byte) 1), Byte.class, classHelper));
        assertNotNull(Wrappers.wrap((short) 1, short.class, classHelper));
        assertNotNull(Wrappers.wrap(new Short((short) 1), Short.class,
                classHelper));
        assertNotNull(Wrappers.wrap(1, int.class, classHelper));
        assertNotNull(Wrappers.wrap(new Integer(1), Integer.class, classHelper));
        assertNotNull(Wrappers.wrap(1L, long.class, classHelper));
        assertNotNull(Wrappers.wrap(new Long(1), Long.class, classHelper));
        assertNotNull(Wrappers.wrap(1f, float.class, classHelper));
        assertNotNull(Wrappers.wrap(new Float(1), Float.class, classHelper));
        assertNotNull(Wrappers.wrap(1d, double.class, classHelper));
        assertNotNull(Wrappers.wrap(new Double(1), Double.class, classHelper));
        assertNotNull(Wrappers
                .wrap(new byte[] { 1 }, byte[].class, classHelper));
        assertNotNull(Wrappers.wrap("", String.class, classHelper));
        assertNotNull(Wrappers.wrap(new BigDecimal("1"), BigDecimal.class,
                classHelper));
        assertNotNull(Wrappers.wrap(new BigInteger("1"), BigInteger.class,
                classHelper));
        assertNotNull(Wrappers.wrap(Date.valueOf("2009-01-23"), Date.class,
                classHelper));
        assertNotNull(Wrappers.wrap(Time.valueOf("12:34:56"), Time.class,
                classHelper));
        assertNotNull(Wrappers.wrap(Timestamp.valueOf("2009-01-23 12:34:56"),
                Timestamp.class, classHelper));
        assertNotNull(Wrappers.wrap(new java.util.Date(), java.util.Date.class,
                classHelper));
        assertNotNull(Wrappers.wrap(null, Array.class, classHelper));
        assertNotNull(Wrappers.wrap(null, Blob.class, classHelper));
        assertNotNull(Wrappers.wrap(null, Clob.class, classHelper));
        assertNotNull(Wrappers.wrap(null, NClob.class, classHelper));
    }

    public void testWrapBasic_boxedValue_primitiveType() throws Exception {
        Wrapper<?> wrapper = Wrappers.wrap(new Integer(10), int.class,
                classHelper);
        assertNotNull(wrapper);
        assertEquals(IntegerWrapper.class, wrapper.getClass());
        assertEquals(new Integer(10), wrapper.get());
    }

    public void testWrapBasic_null() throws Exception {
        Wrapper<?> wrapper = Wrappers.wrap(null, Integer.class, classHelper);
        assertNotNull(wrapper);
        assertEquals(IntegerWrapper.class, wrapper.getClass());
        assertNull(null, wrapper.get());
    }

    public void testWrapEnum() throws Exception {
        Wrapper<?> wrapper = Wrappers.wrap(MyEnum.AAA, MyEnum.class,
                classHelper);
        assertNotNull(wrapper);
        assertEquals(EnumWrapper.class, wrapper.getClass());
        assertEquals(MyEnum.AAA, wrapper.get());
    }

    public void testWrapEnum_null() throws Exception {
        Wrapper<?> wrapper = Wrappers.wrap(null, MyEnum.class, classHelper);
        assertNotNull(wrapper);
        assertEquals(EnumWrapper.class, wrapper.getClass());
        assertNull(wrapper.get());
    }

    public void testWrapDomain() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber("123-456-789");
        Wrapper<?> wrapper = Wrappers.wrap(phoneNumber, PhoneNumber.class,
                classHelper);
        assertNotNull(wrapper);
        assertEquals(StringWrapper.class, wrapper.getClass());
        assertEquals("123-456-789", wrapper.get());
    }

    public void testWrapDomain_subclass() throws Exception {
        PhoneNumber phoneNumber = new InternationalPhoneNumber("123-456-789");
        Wrapper<?> wrapper = Wrappers.wrap(phoneNumber,
                InternationalPhoneNumber.class, classHelper);
        assertNotNull(wrapper);
        assertEquals(StringWrapper.class, wrapper.getClass());
        assertEquals("123-456-789", wrapper.get());
    }

    public void testWrapDomain_null() throws Exception {
        Wrapper<?> wrapper = Wrappers
                .wrap(null, PhoneNumber.class, classHelper);
        assertNotNull(wrapper);
        assertEquals(StringWrapper.class, wrapper.getClass());
        assertNull(wrapper.get());
    }

    public enum MyEnum {
        AAA, BBB, CCC
    }
}
