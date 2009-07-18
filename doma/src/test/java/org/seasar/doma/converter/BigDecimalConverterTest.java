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
package org.seasar.doma.converter;

import java.math.BigDecimal;
import java.sql.Date;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BigDecimalConverterTest extends TestCase {

    private BigDecimalConverter converter = new BigDecimalConverter();

    public void testNull() throws Exception {
        BigDecimal value = converter.convert(null, null);
        assertNull(value);
    }

    public void testInt() throws Exception {
        BigDecimal value = converter.convert(100, null);
        assertEquals(new BigDecimal(100), value);
    }

    public void testString() throws Exception {
        BigDecimal value = converter.convert("123.456", null);
        assertEquals(new BigDecimal("123.456"), value);
    }

    public void testStringWithPattern() throws Exception {
        BigDecimal value = converter.convert("(123.456)", "(0)");
        assertEquals(new BigDecimal("123.456"), value);
    }

    public void testUnsupportedConversionException() throws Exception {
        try {
            converter.convert(Date.valueOf("2001-02-03"), null);
            fail();
        } catch (UnsupportedConversionException expected) {
        }
    }
}
