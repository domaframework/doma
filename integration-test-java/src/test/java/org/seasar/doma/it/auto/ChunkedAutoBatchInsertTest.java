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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.ChunkedBatchInsertConfig;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.IdentityStrategyDao;
import org.seasar.doma.it.dao.IdentityStrategyDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.IdentityStrategy;
import org.seasar.doma.jdbc.Config;

/**
 * Re-runs every {@link AutoBatchInsertTest} scenario against {@link
 * org.seasar.doma.jdbc.query.ChunkedAutoBatchInsertQuery} by wrapping the resolved {@link Config}
 * with {@link ChunkedBatchInsertConfig}, which swaps the {@link
 * org.seasar.doma.jdbc.QueryImplementors} factory.
 *
 * <p>This subclass also adds coverage for behaviors that only matter when SQL is built lazily per
 * chunk: a single DAO invocation must still cover many entities spanning multiple chunks, and
 * generated IDs must flow back to the original entity instances across those chunk boundaries.
 */
@ExtendWith(IntegrationTestEnvironment.class)
public class ChunkedAutoBatchInsertTest extends AutoBatchInsertTest {

  @Override
  protected Config customize(Config config) {
    return new ChunkedBatchInsertConfig(config);
  }

  /**
   * A single {@code @BatchInsert} call with many entities must succeed end-to-end. With the chunked
   * implementation this exercises multiple chunks within one DAO invocation; the assertions verify
   * row counts, version initialization, and read-back of every inserted row.
   */
  @Test
  public void testManyEntities_singleInvocation(Config config) {
    config = customize(config);
    DepartmentDao dao = new DepartmentDaoImpl(config);
    int count = 50;
    List<Department> departments = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      Department d = new Department();
      int id = 100 + i;
      d.setDepartmentId(new Identity<>(id));
      d.setDepartmentNo(id);
      d.setDepartmentName("name-" + i);
      departments.add(d);
    }

    int[] result = dao.insert(departments);

    assertEquals(count, result.length);
    for (int i = 0; i < count; i++) {
      assertEquals(1, result[i], "row count at index " + i);
      assertEquals(Integer.valueOf(1), departments.get(i).getVersion(), "version at index " + i);
    }
    for (int i = 0; i < count; i++) {
      Department reloaded = dao.selectById(100 + i);
      assertNotNull(reloaded, "row missing for id " + (100 + i));
      assertEquals("name-" + i, reloaded.getDepartmentName());
      assertEquals(Integer.valueOf(1), reloaded.getVersion());
    }
  }

  /**
   * Generated identity values must be written back to every original entity instance, including
   * those processed in chunks after the first. This guards against regressions where chunked
   * execution might lose track of entity positions across chunk boundaries.
   */
  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testManyEntities_identityIdsAssignedAcrossChunks(Config config) {
    config = customize(config);
    IdentityStrategyDao dao = new IdentityStrategyDaoImpl(config);
    int count = 50;
    List<IdentityStrategy> entities = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      entities.add(new IdentityStrategy());
    }

    int[] result = dao.insert(entities);

    assertEquals(count, result.length);
    long distinctIds = entities.stream().map(IdentityStrategy::getId).distinct().count();
    assertEquals(count, distinctIds, "every entity must receive a unique generated id");
    for (int i = 0; i < count; i++) {
      assertNotNull(entities.get(i).getId(), "id missing at index " + i);
    }
    Integer first = entities.get(0).getId();
    for (int i = 1; i < count; i++) {
      assertTrue(
          entities.get(i).getId() > first,
          "ids should be strictly increasing across chunks; index " + i);
    }
    List<Integer> persisted =
        dao.selectAll().stream().map(IdentityStrategy::getId).sorted().toList();
    List<Integer> expected = entities.stream().map(IdentityStrategy::getId).sorted().toList();
    assertEquals(expected, persisted);
  }

  /**
   * An empty list must not trigger any chunk processing. The chunked path treats {@code
   * isExecutable() == false} the same as the non-chunked path: zero rows updated, no SQL run.
   */
  @Test
  public void testEmptyList_noChunksExecuted(Config config) {
    config = customize(config);
    DepartmentDao dao = new DepartmentDaoImpl(config);

    int[] result = dao.insert(List.of());

    assertEquals(0, result.length);
  }
}
