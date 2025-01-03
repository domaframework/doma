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
package org.seasar.doma.jdbc.id;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.entity._IdGeneratedEmp;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

public class BuiltinSequenceIdGeneratorTest {

  @Test
  public void test() {
    MockConfig config = new MockConfig();
    config.setDialect(new PostgresDialect());
    MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
    resultSet.rows.add(new RowData(11L));

    BuiltinSequenceIdGenerator idGenerator = new BuiltinSequenceIdGenerator();
    idGenerator.setQualifiedSequenceName("aaa");
    idGenerator.setInitialValue(1);
    idGenerator.setAllocationSize(1);
    IdGenerationConfig idGenerationConfig =
        new IdGenerationConfig(config, _IdGeneratedEmp.getSingletonInternal());
    Long value = idGenerator.generatePreInsert(idGenerationConfig);
    assertEquals(11L, value);
    assertEquals("select nextval('aaa')", config.dataSource.connection.preparedStatement.sql);
  }
}
