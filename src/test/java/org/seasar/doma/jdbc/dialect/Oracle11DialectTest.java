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
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.sql.ConvertToLogFormatFunction;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;

public class Oracle11DialectTest extends TestCase {

  public void testExpressionFunctions_prefix() throws Exception {
    var dialect = new Oracle11Dialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a$％a$＿%", functions.prefix("a$a%a_a％a＿"));
  }

  public void testExpressionFunctions_prefix_escape() throws Exception {
    var dialect = new Oracle11Dialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a!％a!＿%", functions.prefix("a!a%a_a％a＿", '!'));
  }

  public void testExpressionFunctions_prefix_escapeWithBackslash() throws Exception {
    var dialect = new Oracle11Dialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a\\\\a\\%a\\_a\\％a\\＿%", functions.prefix("a\\a%a_a％a＿", '\\'));
  }

  public void testDateFormat() throws Exception {
    var dialect = new Oracle11Dialect();
    var visitor = dialect.getSqlLogFormattingVisitor();
    var wrapper = new DateWrapper(Date.valueOf("2009-01-23"));
    assertEquals(
        "date'2009-01-23'", wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }

  public void testTimeFormat() throws Exception {
    var dialect = new Oracle11Dialect();
    var visitor = dialect.getSqlLogFormattingVisitor();
    var wrapper = new TimeWrapper(Time.valueOf("01:23:45"));
    assertEquals("time'01:23:45'", wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }

  public void testTimestampFormat() throws Exception {
    var dialect = new Oracle11Dialect();
    var visitor = dialect.getSqlLogFormattingVisitor();
    var wrapper = new TimestampWrapper(Timestamp.valueOf("2009-01-23 01:23:45.123456789"));
    assertEquals(
        "timestamp'2009-01-23 01:23:45.123456789'",
        wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }

  public void testUtilDateFormat() throws Exception {
    var dialect = new Oracle11Dialect();
    var visitor = dialect.getSqlLogFormattingVisitor();
    var wrapper =
        new UtilDateWrapper(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:sss.SSS").parse("2009-01-23 12:34:56.789"));
    assertEquals(
        "timestamp'2009-01-23 12:34:56.789'",
        wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }
}
