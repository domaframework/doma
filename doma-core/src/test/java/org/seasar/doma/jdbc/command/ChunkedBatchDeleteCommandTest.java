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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.ChunkedAutoBatchDeleteQuery;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ChunkedBatchDeleteCommandTest {

  /** Records the size of every executeBatch() call so the test can verify chunk boundaries. */
  private static class RecordingPreparedStatement extends MockPreparedStatement {
    final List<Integer> batchSizes = new ArrayList<>();

    @Override
    public int[] executeBatch() throws SQLException {
      batchSizes.add(addBatchCount);
      return super.executeBatch();
    }
  }

  @Test
  public void testExecute_chunksAlignWithBatchSize(TestInfo testInfo) {
    RecordingPreparedStatement preparedStatement = new RecordingPreparedStatement();
    MockConfig runtimeConfig = new MockConfig();
    runtimeConfig.dataSource = new MockDataSource(new MockConnection(preparedStatement));

    int total = 25;
    List<Emp> emps = new ArrayList<>(total);
    for (int i = 0; i < total; i++) {
      Emp emp = new Emp();
      emp.setId(i + 1);
      emp.setName("name-" + i);
      emp.setVersion(1);
      emps.add(emp);
    }

    ChunkedAutoBatchDeleteQuery<Emp> query =
        new ChunkedAutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(testInfo.getTestMethod().get());
    query.setConfig(runtimeConfig);
    query.setEntities(emps);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    int[] rows = new BatchDeleteCommand(query).execute();
    query.complete();

    assertEquals(total, rows.length);
    assertEquals("delete from EMP where ID = ? and VERSION = ?", preparedStatement.sql);
    assertEquals(List.of(10, 10, 5), preparedStatement.batchSizes);
    assertEquals(0, preparedStatement.addBatchCount);
  }
}
