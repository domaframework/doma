/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * @author bakenezumi
 */
public class MapBatchInsertBuilderTest {

  @SuppressWarnings("serial")
  @Test
  public void test() {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    builder.callerClassName(getClass().getName());
    builder.callerMethodName("test");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<>() {
          {
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<>() {
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
  public void testGetSqls() {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<>() {
          {
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1001);
                  }
                });
            add(
                new LinkedHashMap<>() {
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
  public void testNullValue() {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<>() {
          {
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", null);
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<>() {
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
  public void testChangeType() {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<>() {
          {
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", null);
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", "ALLEN");
                    put("salary", null);
                  }
                });
            add(
                new LinkedHashMap<>() {
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
  public void testNotEqualMapSize() {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<>() {
          {
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", "SMITH");
                  }
                });
            add(
                new LinkedHashMap<>() {
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
  public void testHashMap() {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    List<Map<String, Object>> employees =
        new ArrayList<>() {
          {
            add(
                new HashMap<>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1002);
                  }
                });
            add(
                new HashMap<>() {
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
  public void testEmptyList() {
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
  public void testDifferentKey() {
    MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(new MockConfig(), "Emp");
    builder.callerClassName(getClass().getName());
    builder.callerMethodName("test");
    List<LinkedHashMap<String, Object>> employees =
        new ArrayList<>() {
          {
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", "SMITH");
                    put("salary", 1000);
                  }
                });
            add(
                new LinkedHashMap<>() {
                  {
                    put("name", "ALLEN");
                    put("age", 20);
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
