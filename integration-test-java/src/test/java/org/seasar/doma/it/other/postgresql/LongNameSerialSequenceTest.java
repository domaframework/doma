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
package org.seasar.doma.it.other.postgresql;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.VeryLongCharactersNamedTableDao;
import org.seasar.doma.it.dao.VeryLongCharactersNamedTableDaoImpl;
import org.seasar.doma.it.entity.VeryLongCharactersNamedTable;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(onlyIf = {Dbms.POSTGRESQL})
public class LongNameSerialSequenceTest {

  @Test
  public void testInsert(Config config) {
    VeryLongCharactersNamedTableDao dao = new VeryLongCharactersNamedTableDaoImpl(config);
    VeryLongCharactersNamedTable entity = new VeryLongCharactersNamedTable();
    entity.setValue("foo");
    dao.insert(entity);
  }

  @Test
  public void testBatchInsert(Config config) {
    VeryLongCharactersNamedTableDao dao = new VeryLongCharactersNamedTableDaoImpl(config);
    VeryLongCharactersNamedTable entity1 = new VeryLongCharactersNamedTable();
    entity1.setValue("foo");
    VeryLongCharactersNamedTable entity2 = new VeryLongCharactersNamedTable();
    entity2.setValue("bar");
    dao.insert(Arrays.asList(entity1, entity2));
  }
}
