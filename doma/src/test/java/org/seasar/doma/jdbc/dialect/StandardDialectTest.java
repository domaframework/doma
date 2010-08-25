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
package org.seasar.doma.jdbc.dialect;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.TestCase;

import org.seasar.doma.expr.ExpressionFunctions;

/**
 * @author taedium
 * 
 */
public class StandardDialectTest extends TestCase {

    public void testApplyQuote() {
        StandardDialect dialect = new StandardDialect();
        assertEquals("\"aaa\"", dialect.applyQuote("aaa"));
    }

    public void testRemoveQuote() {
        StandardDialect dialect = new StandardDialect();
        assertEquals("aaa", dialect.removeQuote("\"aaa\""));
        assertEquals("bbb", dialect.removeQuote("bbb"));
    }

    public void testExpressionFunctions_prefix() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a$$a$%a$_%", functions.prefix("a$a%a_"));
    }

    public void testExpressionFunctions_prefix_escape() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a!!a!%a!_%", functions.prefix("a!a%a_", '!'));
    }

    public void testExpressionFunctions_prefix_escapeWithBackslash()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a\\\\a\\%a\\_%", functions.prefix("a\\a%a_", '\\'));
    }

    public void testExpressionFunctions_roundDonwTimePart_forDate()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
        Date date = new Date(calendar.getTimeInMillis());
        assertEquals(Date.valueOf("2009-01-23"),
                functions.roundDownTimePart(date));
    }

    public void testExpressionFunctions_roundDonwTimePart_forTimestamp()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Timestamp timestamp = Timestamp
                .valueOf("2009-01-23 12:34:56.123456789");
        assertEquals(Timestamp.valueOf("2009-01-23 00:00:00.000000000"),
                functions.roundDownTimePart(timestamp));
    }

    public void testExpressionFunctions_roundUpTimePart_forDate()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
        Date date = new Date(calendar.getTimeInMillis());
        assertEquals(Date.valueOf("2009-01-24").getTime(), functions
                .roundUpTimePart(date).getTime());
    }

    public void testExpressionFunctions_roundUpTimePart_forTimestamp()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Timestamp timestamp = Timestamp
                .valueOf("2009-01-23 12:34:56.123456789");
        assertEquals(Timestamp.valueOf("2009-01-24 00:00:00.000000000"),
                functions.roundUpTimePart(timestamp));
    }

    public void testExpressionFunctions_isEmpty() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertTrue(functions.isEmpty(null));
        assertTrue(functions.isEmpty(""));
        assertFalse(functions.isEmpty(" "));
        assertFalse(functions.isEmpty(" \t\n\r "));
        assertFalse(functions.isEmpty("a"));
        assertFalse(functions.isEmpty(" a "));
    }

    public void testExpressionFunctions_isNotEmpty() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertFalse(functions.isNotEmpty(null));
        assertFalse(functions.isNotEmpty(""));
        assertTrue(functions.isNotEmpty(" "));
        assertTrue(functions.isNotEmpty(" \t\n\r "));
        assertTrue(functions.isNotEmpty("a"));
        assertTrue(functions.isNotEmpty(" a "));
    }

    public void testExpressionFunctions_isBlank() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertTrue(functions.isBlank(null));
        assertTrue(functions.isBlank(""));
        assertTrue(functions.isBlank(" "));
        assertTrue(functions.isBlank(" \t\n\r "));
        assertFalse(functions.isBlank("a"));
        assertFalse(functions.isBlank(" a "));
    }

    public void testExpressionFunctions_isNotBlank() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertFalse(functions.isNotBlank(null));
        assertFalse(functions.isNotBlank(""));
        assertFalse(functions.isNotBlank(" "));
        assertFalse(functions.isNotBlank(" \t\n\r "));
        assertTrue(functions.isNotBlank("a"));
        assertTrue(functions.isNotBlank(" a "));
    }
}
