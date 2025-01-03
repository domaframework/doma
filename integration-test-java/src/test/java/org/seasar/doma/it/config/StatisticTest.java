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
package org.seasar.doma.it.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.criteria.Employee_;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.statistic.StatisticManager;

@ExtendWith(IntegrationTestEnvironment.class)
public class StatisticTest {

  private final StatisticManager statisticManager;
  private final QueryDsl dsl;

  public StatisticTest(Config config) {
    this.statisticManager = config.getStatisticManager();
    this.dsl = new QueryDsl(config);
  }

  @BeforeEach
  void beforeEach() {
    statisticManager.setEnabled(true);
  }

  @AfterEach
  void afterEach() {
    statisticManager.setEnabled(false);
    statisticManager.clear();
  }

  @Test
  void test() {
    statisticManager.clear();

    assertEquals(0, statisticManager.getStatistics().spliterator().estimateSize());

    Employee_ e = new Employee_();
    dsl.from(e).fetch();
    dsl.from(e).where(c -> c.eq(e.employeeId, 1)).fetch();

    assertEquals(2, statisticManager.getStatistics().spliterator().estimateSize());
  }

  @Test
  void concurrentExecution() throws InterruptedException {
    statisticManager.clear();

    int threadCount = 5;
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      new Thread(
              () -> {
                try {
                  Employee_ e = new Employee_();
                  dsl.from(e).fetch();
                } finally {
                  latch.countDown();
                }
              })
          .start();
    }
    latch.await();

    assertEquals(1, statisticManager.getStatistics().spliterator().estimateSize());
    assertEquals(5, statisticManager.getStatistics().iterator().next().execCount());
  }
}
