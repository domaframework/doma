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
package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ChunkedAutoBatchInsertQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare_onlyFirstEntitySqlBuilt() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");

    Emp emp3 = new Emp();
    emp3.setId(30);
    emp3.setName("ccc");

    ChunkedAutoBatchInsertQuery<Emp> query =
        new ChunkedAutoBatchInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setEntities(Arrays.asList(emp1, emp2, emp3));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertTrue(query.isExecutable());
    assertTrue(query.isBatchSupported());
    assertEquals(3, query.getEntityCount());
    // Only the SQL for the first entity is built up front; the rest are built on demand.
    assertEquals(1, query.getSqls().size());
    assertSame(query.getSqls().get(0), query.getSql());
  }

  @Test
  public void testBuildSql_reusesFirstEntityAndDropsBuffer() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");

    Emp emp3 = new Emp();
    emp3.setId(30);
    emp3.setName("ccc");

    ChunkedAutoBatchInsertQuery<Emp> query =
        new ChunkedAutoBatchInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setEntities(Arrays.asList(emp1, emp2, emp3));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql firstEntitySql = query.getSql();

    PreparedSql sql0 = query.buildSql(0);
    // Entity 0's SQL was reused from prepare().
    assertSame(firstEntitySql, sql0);
    assertEquals(10, sql0.getParameters().get(0).getWrapper().get());
    // The internal buffer is cleared so the just-built SQL can be GC'd once the caller is done.
    assertEquals(0, query.getSqls().size());

    PreparedSql sql1 = query.buildSql(1);
    assertEquals(20, sql1.getParameters().get(0).getWrapper().get());
    assertEquals(0, query.getSqls().size());

    PreparedSql sql2 = query.buildSql(2);
    assertEquals(30, sql2.getParameters().get(0).getWrapper().get());
    assertEquals(0, query.getSqls().size());
  }
}
