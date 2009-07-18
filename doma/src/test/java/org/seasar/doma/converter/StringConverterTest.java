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
public class StringConverterTest extends TestCase {

    private StringConverter converter = new StringConverter();

    public void testNull() throws Exception {
        String value = converter.convert(null, null);
        assertNull(value);
    }

    public void testBigDecimal() throws Exception {
        BigDecimal decimal = new BigDecimal(100).setScale(-1);
        String value = converter.convert(decimal, null);
        assertEquals("1.0E+2", decimal.toString());
        assertEquals("100", value);
    }

    public void testInt() throws Exception {
        String value = converter.convert(1000, null);
        assertEquals("1000", value);
    }

    public void testIntWithPattern() throws Exception {
        String value = converter.convert(1000, "#,##0");
        assertEquals("1,000", value);
    }

    public void testDate() throws Exception {
        String value = converter.convert(Date.valueOf("2001-02-03"), null);
        assertEquals("2001-02-03", value);
    }

    public void testDateWithPattern() throws Exception {
        String value = converter
                .convert(Date.valueOf("2001-02-03"), "yyyy/MM/dd");
        assertEquals("2001/02/03", value);
    }
}
