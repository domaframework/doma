package org.seasar.doma.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import org.junit.jupiter.api.Test;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlNode;

public class StandardDialectTest {

  @Test
  public void testApplyQuote() {
    StandardDialect dialect = new StandardDialect();
    assertEquals("\"aaa\"", dialect.applyQuote("aaa"));
  }

  @Test
  public void testRemoveQuote() {
    StandardDialect dialect = new StandardDialect();
    assertEquals("aaa", dialect.removeQuote("\"aaa\""));
    assertEquals("bbb", dialect.removeQuote("bbb"));
  }

  @Test
  public void testExpressionFunctions_escape() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_", functions.escape("a$a%a_"));
  }

  @Test
  public void testExpressionFunctions_escape_withExclamation() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_", functions.escape("a!a%a_", '!'));
  }

  @Test
  public void testExpressionFunctions_escape_withBackslash() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a\\\\a\\%a\\_", functions.escape("a\\a%a_", '\\'));
  }

  @Test
  public void testExpressionFunctions_prefix() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_%", functions.prefix("a$a%a_"));
  }

  @Test
  public void testExpressionFunctions_prefix_escape() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_%", functions.prefix("a!a%a_", '!'));
  }

  @Test
  public void testExpressionFunctions_prefix_escapeWithBackslash() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a\\\\a\\%a\\_%", functions.prefix("a\\a%a_", '\\'));
  }

  @Test
  public void testExpressionFunctions_suffix() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("%a$$a$%a$_", functions.suffix("a$a%a_"));
  }

  @Test
  public void testExpressionFunctions_suffix_escape() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("%a!!a!%a!_", functions.suffix("a!a%a_", '!'));
  }

  @Test
  public void testExpressionFunctions_suffix_escapeWithBackslash() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("%a\\\\a\\%a\\_", functions.suffix("a\\a%a_", '\\'));
  }

  @Test
  public void testExpressionFunctions_infix() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("%a$$a$%a$_%", functions.infix("a$a%a_"));
  }

  @Test
  public void testExpressionFunctions_infix_escape() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("%a!!a!%a!_%", functions.infix("a!a%a_", '!'));
  }

  @Test
  public void testExpressionFunctions_infix_escapeWithBackslash() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("%a\\\\a\\%a\\_%", functions.infix("a\\a%a_", '\\'));
  }

  @Test
  public void testExpressionFunctions_roundDownTimePart_forUtilDate() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
    java.util.Date date = new java.util.Date(calendar.getTimeInMillis());
    assertEquals(Date.valueOf("2009-01-23"), functions.roundDownTimePart(date));
  }

  @Test
  public void testExpressionFunctions_roundDownTimePart_forDate() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
    Date date = new Date(calendar.getTimeInMillis());
    assertEquals(Date.valueOf("2009-01-23"), functions.roundDownTimePart(date));
  }

  @Test
  public void testExpressionFunctions_roundDownTimePart_forTimestamp() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Timestamp timestamp = Timestamp.valueOf("2009-01-23 12:34:56.123456789");
    assertEquals(
        Timestamp.valueOf("2009-01-23 00:00:00.000000000"), functions.roundDownTimePart(timestamp));
  }

  @Test
  public void testExpressionFunctions_roundDownTimePart_forLocalDateTime() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    LocalDateTime localDateTime = LocalDateTime.of(2009, 1, 23, 12, 34, 56, 123456789);
    assertEquals(
        LocalDateTime.of(2009, 1, 23, 0, 0, 0, 0), functions.roundDownTimePart(localDateTime));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forUtilDate() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
    java.util.Date date = new java.util.Date(calendar.getTimeInMillis());
    assertEquals(Date.valueOf("2009-01-24").getTime(), functions.roundUpTimePart(date).getTime());
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forDate() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2009, Calendar.JANUARY, 23, 12, 34, 56);
    Date date = new Date(calendar.getTimeInMillis());
    assertEquals(Date.valueOf("2009-01-24").getTime(), functions.roundUpTimePart(date).getTime());
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forDate_endOfMonth() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2009, Calendar.MARCH, 31, 0, 0, 0);
    Date date = new Date(calendar.getTimeInMillis());
    assertEquals(Date.valueOf("2009-04-01").getTime(), functions.roundUpTimePart(date).getTime());
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forDate_endOfYear() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Calendar calendar = Calendar.getInstance();
    calendar.set(2009, Calendar.DECEMBER, 31, 0, 0, 0);
    Date date = new Date(calendar.getTimeInMillis());
    assertEquals(Date.valueOf("2010-01-01").getTime(), functions.roundUpTimePart(date).getTime());
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forTimestamp() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Timestamp timestamp = Timestamp.valueOf("2009-01-23 12:34:56.123456789");
    assertEquals(
        Timestamp.valueOf("2009-01-24 00:00:00.000000000"), functions.roundUpTimePart(timestamp));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forTimestamp_endOfMonth() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Timestamp timestamp = Timestamp.valueOf("2009-03-31 00:00:00.000000000");
    assertEquals(
        Timestamp.valueOf("2009-04-01 00:00:00.000000000"), functions.roundUpTimePart(timestamp));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forTimestamp_endOfYear() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    Timestamp timestamp = Timestamp.valueOf("2009-12-31 00:00:00.000000000");
    assertEquals(
        Timestamp.valueOf("2010-01-01 00:00:00.000000000"), functions.roundUpTimePart(timestamp));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forLocalDate() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    LocalDate localDate = LocalDate.of(2009, 1, 23);
    assertEquals(LocalDate.of(2009, 1, 24), functions.roundUpTimePart(localDate));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forLocalDate_endOfMonth() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    LocalDate localDate = LocalDate.of(2009, 3, 31);
    assertEquals(LocalDate.of(2009, 4, 1), functions.roundUpTimePart(localDate));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forLocalDate_endOfYear() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    LocalDate localDate = LocalDate.of(2009, 12, 31);
    assertEquals(LocalDate.of(2010, 1, 1), functions.roundUpTimePart(localDate));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forLocalDateTime() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    LocalDateTime localDateTime = LocalDateTime.of(2009, 1, 23, 12, 34, 56, 123456789);
    assertEquals(
        LocalDateTime.of(2009, 1, 24, 0, 0, 0, 0), functions.roundUpTimePart(localDateTime));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forLocalDateTime_endOfMonth() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    LocalDateTime localDateTime = LocalDateTime.of(2009, 3, 31, 12, 34, 56, 123456789);
    assertEquals(
        LocalDateTime.of(2009, 4, 1, 0, 0, 0, 0), functions.roundUpTimePart(localDateTime));
  }

  @Test
  public void testExpressionFunctions_roundUpTimePart_forLocalDateTime_endOfYear() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    LocalDateTime localDateTime = LocalDateTime.of(2009, 12, 31, 12, 34, 56, 123456789);
    assertEquals(
        LocalDateTime.of(2010, 1, 1, 0, 0, 0, 0), functions.roundUpTimePart(localDateTime));
  }

  @Test
  public void testExpressionFunctions_isEmpty() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertTrue(functions.isEmpty(null));
    assertTrue(functions.isEmpty(""));
    assertFalse(functions.isEmpty(" "));
    assertFalse(functions.isEmpty(" \t\n\r "));
    assertFalse(functions.isEmpty("a"));
    assertFalse(functions.isEmpty(" a "));
  }

  @Test
  public void testExpressionFunctions_isNotEmpty() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertFalse(functions.isNotEmpty(null));
    assertFalse(functions.isNotEmpty(""));
    assertTrue(functions.isNotEmpty(" "));
    assertTrue(functions.isNotEmpty(" \t\n\r "));
    assertTrue(functions.isNotEmpty("a"));
    assertTrue(functions.isNotEmpty(" a "));
  }

  @Test
  public void testExpressionFunctions_isBlank() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertTrue(functions.isBlank(null));
    assertTrue(functions.isBlank(""));
    assertTrue(functions.isBlank(" "));
    assertTrue(functions.isBlank(" \t\n\r "));
    assertFalse(functions.isBlank("a"));
    assertFalse(functions.isBlank(" a "));
  }

  @Test
  public void testExpressionFunctions_isNotBlank() {
    StandardDialect dialect = new StandardDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertFalse(functions.isNotBlank(null));
    assertFalse(functions.isNotBlank(""));
    assertFalse(functions.isNotBlank(" "));
    assertFalse(functions.isNotBlank(" \t\n\r "));
    assertTrue(functions.isNotBlank("a"));
    assertTrue(functions.isNotBlank(" a "));
  }

  @Test
  public void testTransformSelectSqlNode_forUpdate() {
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

  @Test
  public void testTransformSelectSqlNode_forUpdateWait() {
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

  @Test
  public void testTransformSelectSqlNode_forUpdateNowait() {
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

  @Test
  public void testTransformSelectSqlNode_forUpdate_alias() {
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

  @Test
  public void testTransformSelectSqlNode_forUpdateWait_alias() {
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

  @Test
  public void testTransformSelectSqlNode_forUpdateNowait_alias() {
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
    public boolean supportsSelectForUpdate(SelectForUpdateType type, boolean withTargets) {
      return !withTargets;
    }
  }
}
