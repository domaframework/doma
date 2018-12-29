package org.seasar.doma.jdbc.dialect;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import junit.framework.TestCase;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.sql.ConvertToLogFormatFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;

public class Oracle11DialectTest extends TestCase {

  public void testExpressionFunctions_prefix() throws Exception {
    Oracle11Dialect dialect = new Oracle11Dialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a$％a$＿%", functions.prefix("a$a%a_a％a＿"));
  }

  public void testExpressionFunctions_prefix_escape() throws Exception {
    Oracle11Dialect dialect = new Oracle11Dialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a!％a!＿%", functions.prefix("a!a%a_a％a＿", '!'));
  }

  public void testExpressionFunctions_prefix_escapeWithBackslash() throws Exception {
    Oracle11Dialect dialect = new Oracle11Dialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a\\\\a\\%a\\_a\\％a\\＿%", functions.prefix("a\\a%a_a％a＿", '\\'));
  }

  public void testDateFormat() throws Exception {
    Oracle11Dialect dialect = new Oracle11Dialect();
    SqlLogFormattingVisitor visitor = dialect.getSqlLogFormattingVisitor();
    DateWrapper wrapper = new DateWrapper(Date.valueOf("2009-01-23"));
    assertEquals(
        "date'2009-01-23'", wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }

  public void testTimeFormat() throws Exception {
    Oracle11Dialect dialect = new Oracle11Dialect();
    SqlLogFormattingVisitor visitor = dialect.getSqlLogFormattingVisitor();
    TimeWrapper wrapper = new TimeWrapper(Time.valueOf("01:23:45"));
    assertEquals("time'01:23:45'", wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }

  public void testTimestampFormat() throws Exception {
    Oracle11Dialect dialect = new Oracle11Dialect();
    SqlLogFormattingVisitor visitor = dialect.getSqlLogFormattingVisitor();
    TimestampWrapper wrapper =
        new TimestampWrapper(Timestamp.valueOf("2009-01-23 01:23:45.123456789"));
    assertEquals(
        "timestamp'2009-01-23 01:23:45.123456789'",
        wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }

  public void testUtilDateFormat() throws Exception {
    Oracle11Dialect dialect = new Oracle11Dialect();
    SqlLogFormattingVisitor visitor = dialect.getSqlLogFormattingVisitor();
    UtilDateWrapper wrapper =
        new UtilDateWrapper(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:sss.SSS").parse("2009-01-23 12:34:56.789"));
    assertEquals(
        "timestamp'2009-01-23 12:34:56.789'",
        wrapper.accept(visitor, new ConvertToLogFormatFunction(), null));
  }
}
