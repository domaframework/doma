package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.message.Message;

/** @author bakenezumi */
public class MapBatchInsertBuilderTest {

  @SuppressWarnings("serial")
  @Test
  public void test() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    builder.callerClassName(getClass().getName());
    builder.callerMethodName("test");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<LinkedHashMap<String, Object>>() {
          {
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salary", 2000);
                  }
                });
          }
        };
    builder.execute(employees);
  }

  @SuppressWarnings("serial")
  @Test
  public void testGetSqls() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<LinkedHashMap<String, Object>>() {
          {
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1001);
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salary", 2001);
                  }
                });
          }
        };
    builder.execute(employees);

    String expectedSql = String.format("insert into Emp" + " (name, salary)%n" + "values (?, ?)");
    List<? extends Sql<?>> sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    assertEquals(expectedSql, sqls.get(0).getRawSql());
    List<? extends SqlParameter> parameters0 = sqls.get(0).getParameters();
    assertEquals(2, parameters0.size());
    assertEquals("SMITH", parameters0.get(0).getValue());
    assertEquals(1001, parameters0.get(1).getValue());

    assertEquals(expectedSql, sqls.get(1).getRawSql());
    List<? extends SqlParameter> parameters1 = sqls.get(1).getParameters();
    assertEquals(2, parameters1.size());
    assertEquals("ALLEN", parameters1.get(0).getValue());
    assertEquals(2001, parameters1.get(1).getValue());
  }

  @SuppressWarnings("serial")
  @Test
  public void testNullValue() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<LinkedHashMap<String, Object>>() {
          {
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", null);
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salary", null);
                  }
                });
          }
        };
    builder.execute(employees);

    String expectedSql = String.format("insert into Emp" + " (name, salary)%n" + "values (?, ?)");
    List<? extends Sql<?>> sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    assertEquals(expectedSql, sqls.get(0).getRawSql());
    List<? extends SqlParameter> parameters0 = sqls.get(0).getParameters();
    assertEquals(2, parameters0.size());
    assertNull(parameters0.get(0).getValue());
    assertEquals(1000, parameters0.get(1).getValue());

    assertEquals(expectedSql, sqls.get(1).getRawSql());
    List<? extends SqlParameter> parameters1 = sqls.get(1).getParameters();
    assertEquals(2, parameters1.size());
    assertEquals("ALLEN", parameters1.get(0).getValue());
    assertNull(parameters1.get(1).getValue());
  }

  @SuppressWarnings("serial")
  @Test
  public void testChangeType() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<LinkedHashMap<String, Object>>() {
          {
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", null);
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salary", null);
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "WORD");
                    put("salary", "3000");
                  }
                });
          }
        };
    try {
      builder.execute(employees);
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2229, e.getMessageResource());
      return;
    }
    fail();
  }

  @SuppressWarnings("serial")
  @Test
  public void testNotEqualMapSize() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<LinkedHashMap<String, Object>>() {
          {
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "SMITH");
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salary", 2000);
                  }
                });
          }
        };
    try {
      builder.execute(employees);
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2231, e.getMessageResource());
      return;
    }
    fail();
  }

  @SuppressWarnings("serial")
  @Test
  public void testHashMap() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<Map<String, Object>> employees =
        new ArrayList<Map<String, Object>>() {
          {
            add(
                new HashMap<String, Object>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1002);
                  }
                });
            add(
                new HashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salary", 2002);
                  }
                });
          }
        };
    builder.execute(employees);

    String expectedSql = String.format("insert into Emp" + " (name, salary)%n" + "values (?, ?)");
    List<? extends Sql<?>> sqls = builder.getSqls();
    assertEquals(2, sqls.size());
    assertEquals(expectedSql, sqls.get(0).getRawSql());
    List<? extends SqlParameter> parameters0 = sqls.get(0).getParameters();
    assertEquals(2, parameters0.size());
    assertEquals("SMITH", parameters0.get(0).getValue());
    assertEquals(1002, parameters0.get(1).getValue());

    assertEquals(expectedSql, sqls.get(1).getRawSql());
    List<? extends SqlParameter> parameters1 = sqls.get(1).getParameters();
    assertEquals(2, parameters1.size());
    assertEquals("ALLEN", parameters1.get(0).getValue());
    assertEquals(2002, parameters1.get(1).getValue());
  }

  @Test
  public void testEmptyList() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees = new ArrayList<>();
    try {
      builder.execute(employees);
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2232, e.getMessageResource());
      return;
    }
    fail();
  }

  @SuppressWarnings("serial")
  @Test
  public void testDifferentKey() throws Exception {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    builder.callerClassName(getClass().getName());
    builder.callerMethodName("test");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<LinkedHashMap<String, Object>>() {
          {
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<String, Object>() {
                  {
                    put("name", "ALLEN");
                    put("salaree", 2000);
                  }
                });
          }
        };
    try {
      builder.execute(employees);
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2233, e.getMessageResource());
      return;
    }
    fail();
  }
}
