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

import java.sql.Time;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class TimeConverterTest extends TestCase {

    private TimeConverter converter = new TimeConverter();

    public void testNull() throws Exception {
        Time value = converter.convert(null, null);
        assertNull(value);
    }

    public void testUtilDate() throws Exception {
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd")
                .parse("2001-02-03");
        Time value = converter.convert(date, null);
        assertEquals(date, value);
    }

    public void testString() throws Exception {
        Time value = converter.convert("12:34:56", null);
        assertEquals(Time.valueOf("12:34:56"), value);
    }

    public void testStringWithPattern() throws Exception {
        Time value = converter.convert("12-34-56", "HH-mm-ss");
        assertEquals(Time.valueOf("12:34:56"), value);
    }
}
