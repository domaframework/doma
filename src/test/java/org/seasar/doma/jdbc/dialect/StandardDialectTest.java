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
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlNode;

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

    public void testExpressionFunctions_suffix() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("%a$$a$%a$_", functions.suffix("a$a%a_"));
    }

    public void testExpressionFunctions_suffix_escape() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("%a!!a!%a!_", functions.suffix("a!a%a_", '!'));
    }

    public void testExpressionFunctions_suffix_escapeWithBackslash()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("%a\\\\a\\%a\\_", functions.suffix("a\\a%a_", '\\'));
    }

    public void testExpressionFunctions_infix() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("%a$$a$%a$_%", functions.infix("a$a%a_"));
    }

    public void testExpressionFunctions_infix_escape() throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("%a!!a!%a!_%", functions.infix("a!a%a_", '!'));
    }

    public void testExpressionFunctions_infix_escapeWithBackslash()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("%a\\\\a\\%a\\_%", functions.infix("a\\a%a_", '\\'));
    }

    public void testExpressionFunctions_roundDonwTimePart_forUtilDate()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
        java.util.Date date = new java.util.Date(calendar.getTimeInMillis());
        assertEquals(Date.valueOf("2009-01-23"),
                functions.roundDownTimePart(date));
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

    public void testExpressionFunctions_roundUpTimePart_forUtilDate()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
        java.util.Date date = new java.util.Date(calendar.getTimeInMillis());
        assertEquals(Date.valueOf("2009-01-24").getTime(), functions
                .roundUpTimePart(date).getTime());
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

    public void testExpressionFunctions_roundUpTimePart_forDate_endOfMonth()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.MARCH, 31, 0, 0, 0);
        Date date = new Date(calendar.getTimeInMillis());
        assertEquals(Date.valueOf("2009-04-01").getTime(), functions
                .roundUpTimePart(date).getTime());
    }

    public void testExpressionFunctions_roundUpTimePart_forDate_endOfYear()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.DECEMBER, 31, 0, 0, 0);
        Date date = new Date(calendar.getTimeInMillis());
        assertEquals(Date.valueOf("2010-01-01").getTime(), functions
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

    public void testExpressionFunctions_roundUpTimePart_forTimestamp_endOfMonth()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Timestamp timestamp = Timestamp
                .valueOf("2009-03-31 00:00:00.000000000");
        assertEquals(Timestamp.valueOf("2009-04-01 00:00:00.000000000"),
                functions.roundUpTimePart(timestamp));
    }

    public void testExpressionFunctions_roundUpTimePart_forTimestamp_endOfYear()
            throws Exception {
        StandardDialect dialect = new StandardDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        Timestamp timestamp = Timestamp
                .valueOf("2009-12-31 00:00:00.000000000");
        assertEquals(Timestamp.valueOf("2010-01-01 00:00:00.000000000"),
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

    public void testTransformSelectSqlNode_forUpdate() throws Exception {
        StandardDialect dialect = new StandardDialect();
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = parser.parse();
        SelectOptions options = SelectOptions.get().forUpdate();
        try {
            dialect.transformSelectSqlNode(sqlNode, options);
            fail();
        } catch (JdbcException ex) {
            System.out.println(ex.getMessage());
            assertEquals("DOMA2023", ex.getMessageResource().getCode());
        }
    }

    public void testTransformSelectSqlNode_forUpdateWait() throws Exception {
        StandardDialect dialect = new StandardDialect();
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = parser.parse();
        SelectOptions options = SelectOptions.get().forUpdateWait(1);
        try {
            dialect.transformSelectSqlNode(sqlNode, options);
            fail();
        } catch (JdbcException ex) {
            System.out.println(ex.getMessage());
            assertEquals("DOMA2079", ex.getMessageResource().getCode());
        }
    }

    public void testTransformSelectSqlNode_forUpdateNowait() throws Exception {
        StandardDialect dialect = new StandardDialect();
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = parser.parse();
        SelectOptions options = SelectOptions.get().forUpdateNowait();
        try {
            dialect.transformSelectSqlNode(sqlNode, options);
            fail();
        } catch (JdbcException ex) {
            System.out.println(ex.getMessage());
            assertEquals("DOMA2080", ex.getMessageResource().getCode());
        }
    }

    public void testTransformSelectSqlNode_forUpdate_alias() throws Exception {
        StandardDialect dialect = new StandardDialectStab();
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = parser.parse();
        SelectOptions options = SelectOptions.get().forUpdate("emp");
        try {
            dialect.transformSelectSqlNode(sqlNode, options);
            fail();
        } catch (JdbcException ex) {
            System.out.println(ex.getMessage());
            assertEquals("DOMA2024", ex.getMessageResource().getCode());
        }
    }

    public void testTransformSelectSqlNode_forUpdateWait_alias()
            throws Exception {
        StandardDialect dialect = new StandardDialectStab();
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = parser.parse();
        SelectOptions options = SelectOptions.get().forUpdateWait(1, "emp");
        try {
            dialect.transformSelectSqlNode(sqlNode, options);
            fail();
        } catch (JdbcException ex) {
            System.out.println(ex.getMessage());
            assertEquals("DOMA2081", ex.getMessageResource().getCode());
        }
    }

    public void testTransformSelectSqlNode_forUpdateNowait_alias()
            throws Exception {
        StandardDialect dialect = new StandardDialectStab();
        SqlParser parser = new SqlParser("select * from emp order by emp.id");
        SqlNode sqlNode = parser.parse();
        SelectOptions options = SelectOptions.get().forUpdateNowait("emp");
        try {
            dialect.transformSelectSqlNode(sqlNode, options);
            fail();
        } catch (JdbcException ex) {
            System.out.println(ex.getMessage());
            assertEquals("DOMA2082", ex.getMessageResource().getCode());
        }
    }

    public static class StandardDialectStab extends StandardDialect {

        @Override
        public boolean supportsSelectForUpdate(SelectForUpdateType type,
                boolean withTargets) {
            return !withTargets;
        }

    }
}
