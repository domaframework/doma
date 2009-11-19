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
import java.sql.Time;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.sql.ConvertToLogFormatFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;

/**
 * @author taedium
 * 
 */
public class OracleDialectTest extends TestCase {

    public void testExpressionFunctions_starts() throws Exception {
        OracleDialect dialect = new OracleDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a\\\\a\\%a\\_a\\％a\\＿%", functions
                .startWith("a\\a%a_a％a＿"));
    }

    public void testExpressionFunctions_starts_escape() throws Exception {
        OracleDialect dialect = new OracleDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a$$a$%a$_a$％a$＿%", functions.startWith("a$a%a_a％a＿", '$'));
    }

    public void testExpressionFunctions_starts_escapeWithDefault()
            throws Exception {
        OracleDialect dialect = new OracleDialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a\\\\a\\%a\\_a\\％a\\＿%", functions.startWith(
                "a\\a%a_a％a＿", '\\'));
    }

    public void testDateFormat() throws Exception {
        OracleDialect dialect = new OracleDialect();
        SqlLogFormattingVisitor visitor = dialect.getSqlLogFormattingVisitor();
        DateWrapper wrapper = new DateWrapper(Date.valueOf("2009-01-23"));
        assertEquals("date'2009-01-23'", wrapper.accept(visitor,
                new ConvertToLogFormatFunction()));
    }

    public void testTimeFormat() throws Exception {
        OracleDialect dialect = new OracleDialect();
        SqlLogFormattingVisitor visitor = dialect.getSqlLogFormattingVisitor();
        TimeWrapper wrapper = new TimeWrapper(Time.valueOf("01:23:45"));
        assertEquals("time'01:23:45'", wrapper.accept(visitor,
                new ConvertToLogFormatFunction()));
    }

    public void testTimestampFormat() throws Exception {
        OracleDialect dialect = new OracleDialect();
        SqlLogFormattingVisitor visitor = dialect.getSqlLogFormattingVisitor();
        TimestampWrapper wrapper = new TimestampWrapper(Timestamp
                .valueOf("2009-01-23 01:23:45.123456789"));
        assertEquals("timestamp'2009-01-23 01:23:45.123456789'", wrapper
                .accept(visitor, new ConvertToLogFormatFunction()));
    }
}
