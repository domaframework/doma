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
package org.seasar.doma.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.entity.Emp;
import example.entity._Emp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.ChunkedAutoBatchInsertQuery;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ChunkedBatchInsertCommandTest {

  private final MockConfig runtimeConfig = new MockConfig();

  @Test
  public void testExecute_chunksBatchExecutions(TestInfo testInfo) {
    // MockConfig.getBatchSize() returns 10, so 25 entities should produce
    // 3 batches: 10, 10, 5. addBatch() is called 25 times total and executeBatch()
    // is called 3 times (the mock resets addBatchCount each executeBatch).
    int total = 25;
    List<Emp> emps = new ArrayList<>(total);
    for (int i = 0; i < total; i++) {
      Emp emp = new Emp();
      emp.setId(i + 1);
      emp.setName("name-" + i);
      emp.setVersion(1);
      emps.add(emp);
    }

    ChunkedAutoBatchInsertQuery<Emp> query =
        new ChunkedAutoBatchInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(testInfo.getTestMethod().get());
    query.setConfig(runtimeConfig);
    query.setEntities(emps);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    int[] rows = new BatchInsertCommand(query).execute();
    query.complete();

    assertEquals(total, rows.length);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);
    // After the last executeBatch the mock resets addBatchCount to 0.
    assertEquals(0, runtimeConfig.dataSource.connection.preparedStatement.addBatchCount);
  }
}
