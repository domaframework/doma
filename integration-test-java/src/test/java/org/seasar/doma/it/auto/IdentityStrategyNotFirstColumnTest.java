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

import java.util.Arrays;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IdentityOverridableConfig;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.IdentityStrategyNotFirstColumnDao;
import org.seasar.doma.it.dao.IdentityStrategyNotFirstColumnDaoImpl;
import org.seasar.doma.it.entity.IdentityStrategyNotFirstColumn;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class IdentityStrategyNotFirstColumnTest {

  @Test
  public void testInsert(Config config) {
    IdentityStrategyNotFirstColumnDao dao = new IdentityStrategyNotFirstColumnDaoImpl(config);
    IdentityStrategyNotFirstColumn entity = new IdentityStrategyNotFirstColumn();
    entity.setToken("token-value");
    entity.setUserId(100L);
    dao.insert(entity);
    assertNotNull(entity.getTokenId());
    assertEquals(entity.getTokenId(), entity.getLog());
  }

  @Test
  @Run(unless = {Dbms.SQLSERVER})
  public void testId_Identity_override(Config config) {
    Config newConfig = new IdentityOverridableConfig(config);
    IdentityStrategyNotFirstColumnDao dao = new IdentityStrategyNotFirstColumnDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      IdentityStrategyNotFirstColumn entity = new IdentityStrategyNotFirstColumn();
      entity.setTokenId(1000L + i);
      entity.setToken("token-value");
      entity.setUserId(100L);
      dao.insert(entity);
      assertNotNull(entity.getTokenId());
    }
    var expected = LongStream.rangeClosed(1000L, 1109L).boxed().toList();
    var actual =
        dao.selectAll().stream().map(IdentityStrategyNotFirstColumn::getTokenId).sorted().toList();
    assertEquals(expected, actual);
  }

  @Test
  public void testId_Identity_dontOverride(Config config) {
    Config newConfig = new IdentityOverridableConfig(config);
    IdentityStrategyNotFirstColumnDao dao = new IdentityStrategyNotFirstColumnDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      IdentityStrategyNotFirstColumn entity = new IdentityStrategyNotFirstColumn();
      entity.setToken("token-value");
      entity.setUserId(100L);
      dao.insert(entity);
      assertNotNull(entity.getTokenId());
    }
    var expected = LongStream.rangeClosed(1L, 110L).boxed().toList();
    var actual =
        dao.selectAll().stream().map(IdentityStrategyNotFirstColumn::getTokenId).sorted().toList();
    assertEquals(expected, actual);
  }

  @Test
  public void testBatchInsert(Config config) {
    IdentityStrategyNotFirstColumnDao dao = new IdentityStrategyNotFirstColumnDaoImpl(config);
    IdentityStrategyNotFirstColumn entity1 = new IdentityStrategyNotFirstColumn();
    entity1.setToken("token-1");
    entity1.setUserId(100L);
    IdentityStrategyNotFirstColumn entity2 = new IdentityStrategyNotFirstColumn();
    entity2.setToken("token-2");
    entity2.setUserId(200L);
    dao.insert(Arrays.asList(entity1, entity2));
    assertNotNull(entity1.getTokenId());
    assertEquals(entity1.getTokenId(), entity1.getLog());
    assertNotNull(entity2.getTokenId());
    assertEquals(entity2.getTokenId(), entity2.getLog());
  }

  @Test
  @Run(unless = {Dbms.ORACLE, Dbms.SQLSERVER})
  public void testBatchId_Identity_override(Config config) {
    IdentityOverridableConfig newConfig = new IdentityOverridableConfig(config);
    IdentityStrategyNotFirstColumnDao dao = new IdentityStrategyNotFirstColumnDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      IdentityStrategyNotFirstColumn entity = new IdentityStrategyNotFirstColumn();
      entity.setTokenId(1000L + i);
      entity.setToken("token-value");
      entity.setUserId(100L);
      IdentityStrategyNotFirstColumn entity2 = new IdentityStrategyNotFirstColumn();
      entity2.setTokenId(2000L + i);
      entity2.setToken("token-value");
      entity2.setUserId(100L);
      int[] result = dao.insert(Arrays.asList(entity, entity2));
      assertEquals(2, result.length);
      assertEquals(1, result[0]);
      assertEquals(1, result[1]);
      assertNotNull(entity.getTokenId());
      assertNotNull(entity2.getTokenId());
    }
    var expected =
        LongStream.concat(
                LongStream.rangeClosed(1000L, 1109L), LongStream.rangeClosed(2000L, 2109L))
            .boxed()
            .toList();
    var actual =
        dao.selectAll().stream().map(IdentityStrategyNotFirstColumn::getTokenId).sorted().toList();
    assertEquals(expected, actual);
  }

  @Test
  @Run(unless = {Dbms.ORACLE})
  public void testBatchId_Identity_dontOverride(Config config) {
    IdentityOverridableConfig newConfig = new IdentityOverridableConfig(config);
    IdentityStrategyNotFirstColumnDao dao = new IdentityStrategyNotFirstColumnDaoImpl(newConfig);
    for (int i = 0; i < 110; i++) {
      IdentityStrategyNotFirstColumn entity = new IdentityStrategyNotFirstColumn();
      entity.setToken("token-value");
      entity.setUserId(100L);
      IdentityStrategyNotFirstColumn entity2 = new IdentityStrategyNotFirstColumn();
      entity2.setToken("token-value");
      entity2.setUserId(100L);
      int[] result = dao.insert(Arrays.asList(entity, entity2));
      assertEquals(2, result.length);
      assertEquals(1, result[0]);
      assertEquals(1, result[1]);
      assertNotNull(entity.getTokenId());
      assertNotNull(entity2.getTokenId());
    }
    var expected = LongStream.rangeClosed(1L, 220L).boxed().toList();
    var actual =
        dao.selectAll().stream().map(IdentityStrategyNotFirstColumn::getTokenId).sorted().toList();
    assertEquals(expected, actual);
  }
}
