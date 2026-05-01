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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.ChunkedBatchUpdateConfig;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.jdbc.Config;

/**
 * Re-runs every {@link AutoBatchUpdateTest} scenario against {@link
 * org.seasar.doma.jdbc.query.ChunkedAutoBatchUpdateQuery} by wrapping the resolved {@link Config}
 * with {@link ChunkedBatchUpdateConfig}, which swaps the {@link
 * org.seasar.doma.jdbc.QueryImplementors} factory.
 *
 * <p>This subclass also adds coverage for behavior that only matters when SQL is built lazily per
 * chunk: a single DAO invocation must still cover many entities spanning multiple chunks.
 */
@ExtendWith(IntegrationTestEnvironment.class)
public class ChunkedAutoBatchUpdateTest extends AutoBatchUpdateTest {

  @Override
  protected Config customize(Config config) {
    return new ChunkedBatchUpdateConfig(config);
  }

  /**
   * Updates more entities than {@code batchSize} so the call provably spans multiple chunks. The
   * DAO method is annotated {@code @BatchUpdate(batchSize = 7)} and we update {@code 7 * 3 + 2 =
   * 23} rows, which forces chunk boundaries at 7, 14, 21 with a final partial chunk of 2. The
   * assertions verify row counts, version increments, and read-back of every updated row, so any
   * failure to advance across chunks shows up as either a count mismatch or a stale row.
   */
  @Test
  public void testManyEntities_crossesMultipleChunkBoundaries(Config config) {
    config = customize(config);
    DepartmentDao dao = new DepartmentDaoImpl(config);
    int count = 7 * 3 + 2;
    List<Department> departments = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      Department d = new Department();
      int id = 100 + i;
      d.setDepartmentId(new Identity<>(id));
      d.setDepartmentNo(id);
      d.setDepartmentName("before-" + i);
      dao.insert(d);
      d.setDepartmentName("after-" + i);
      departments.add(d);
    }

    int[] result = dao.updateWithSmallBatchSize(departments);

    assertEquals(count, result.length);
    for (int i = 0; i < count; i++) {
      assertEquals(1, result[i], "row count at index " + i);
      assertEquals(Integer.valueOf(2), departments.get(i).getVersion(), "version at index " + i);
    }
    for (int i = 0; i < count; i++) {
      Department reloaded = dao.selectById(100 + i);
      assertNotNull(reloaded, "row missing for id " + (100 + i));
      assertEquals("after-" + i, reloaded.getDepartmentName());
      assertEquals(Integer.valueOf(2), reloaded.getVersion());
    }
  }

  /**
   * An empty list must not trigger any chunk processing. The chunked path treats {@code
   * isExecutable() == false} the same as the non-chunked path: zero rows updated, no SQL run.
   */
  @Test
  public void testEmptyList_noChunksExecuted(Config config) {
    config = customize(config);
    DepartmentDao dao = new DepartmentDaoImpl(config);

    int[] result = dao.update(List.of());

    assertEquals(0, result.length);
  }
}
