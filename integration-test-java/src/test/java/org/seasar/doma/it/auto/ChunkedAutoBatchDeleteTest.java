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
package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.ChunkedBatchDeleteConfig;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;

/**
 * Re-runs every {@link AutoBatchDeleteTest} scenario against {@link
 * org.seasar.doma.jdbc.query.ChunkedAutoBatchDeleteQuery} by wrapping the resolved {@link Config}
 * with {@link ChunkedBatchDeleteConfig}, which swaps the {@link
 * org.seasar.doma.jdbc.QueryImplementors} factory.
 *
 * <p>This subclass also adds coverage for behavior that only matters when SQL is built lazily per
 * chunk: a single DAO invocation must still cover many entities spanning multiple chunks.
 */
@ExtendWith(IntegrationTestEnvironment.class)
public class ChunkedAutoBatchDeleteTest extends AutoBatchDeleteTest {

  @Override
  protected Config customize(Config config) {
    return new ChunkedBatchDeleteConfig(config);
  }

  /**
   * Deletes more entities than {@code batchSize} so the call provably spans multiple chunks. The
   * DAO method is annotated {@code @BatchDelete(batchSize = 7)} and we delete {@code 7 * 3 + 2 =
   * 23} rows, which forces chunk boundaries at 7, 14, 21 with a final partial chunk of 2. The
   * assertions verify row counts and read-back of every deleted row, so any failure to advance
   * across chunks shows up as either a count mismatch or a remaining row.
   */
  @Test
  public void testManyEntities_crossesMultipleChunkBoundaries(Config config) {
    config = customize(config);
    EmployeeDao dao = new EmployeeDaoImpl(config);
    int count = 7 * 3 + 2;
    List<Employee> employees = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      Employee e = new Employee();
      int id = 100 + i;
      e.setEmployeeId(id);
      e.setEmployeeNo(id);
      e.setEmployeeName("name-" + i);
      dao.insert(e);
      employees.add(e);
    }

    int[] result = dao.deleteWithSmallBatchSize(employees);

    assertEquals(count, result.length);
    for (int i = 0; i < count; i++) {
      assertEquals(1, result[i], "row count at index " + i);
      assertNull(dao.selectById(100 + i), "row still exists for id " + (100 + i));
    }
  }
}
