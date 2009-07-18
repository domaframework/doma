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

import java.sql.Date;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DateConverterTest extends TestCase {

    private DateConverter converter = new DateConverter();

    public void testNull() throws Exception {
        Date value = converter.convert(null, null);
        assertNull(value);
    }

    public void testUtilDate() throws Exception {
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd")
                .parse("2001-02-03");
        Date value = converter.convert(date, null);
        assertEquals(date, value);
    }

    public void testString() throws Exception {
        Date value = converter.convert("2001-02-03", null);
        assertEquals(Date.valueOf("2001-02-03"), value);
    }

    public void testStringWithPattern() throws Exception {
        Date value = converter.convert("2001/02/03", "yyyy/MM/dd");
        assertEquals(Date.valueOf("2001-02-03"), value);
    }
}
