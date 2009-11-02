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

    public void testExpressionFunctions_starts() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a\\\\a\\%a\\_%", functions.startWith("a\\a%a_"));
    }

    public void testExpressionFunctions_starts_escape() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a$$a$%a$_%", functions.startWith("a$a%a_", '$'));
    }

    public void testExpressionFunctions_starts_escapeWithDefault()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a\\\\a\\%a\\_%", functions.startWith("a\\a%a_", '\\'));
    }

    public void testExpressionFunctions_minimizeTimePart_forDate()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
        Date date = new Date(calendar.getTimeInMillis());
        assertEquals(Date.valueOf("2009-01-23"), functions
                .minimizeTimePart(date));
    }

    public void testExpressionFunctions_minimizeTimePart_forTimestamp()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Timestamp timestamp = Timestamp
                .valueOf("2009-01-23 12:34:56.123456789");
        assertEquals(Timestamp.valueOf("2009-01-23 00:00:00.000000000"),
                functions.minimizeTimePart(timestamp));
    }

    public void testExpressionFunctions_maximizeTimePart_forDate()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
        Date date = new Date(calendar.getTimeInMillis());
        assertEquals(Timestamp.valueOf("2009-01-23 23:59:59.999").getTime(),
                functions.maximizeTimePart(date).getTime());
    }

    public void testExpressionFunctions_maximizeTimePart_forTimestamp()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Timestamp timestamp = Timestamp
                .valueOf("2009-01-23 12:34:56.123456789");
        assertEquals(Timestamp.valueOf("2009-01-23 23:59:59.999999999"),
                functions.maximizeTimePart(timestamp));
    }
}
