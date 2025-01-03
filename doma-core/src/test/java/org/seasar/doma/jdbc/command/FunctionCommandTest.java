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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.wrapper.IntegerWrapper;

public class FunctionCommandTest {

  private final MockConfig runtimeConfig = new MockConfig();

  @Test
  public void testExecute() {
    List<Object> outParameters =
        runtimeConfig.dataSource.connection.callableStatement.outParameters;
    outParameters.add(10);
    outParameters.add(null);
    outParameters.add(20);
    outParameters.add(30);

    IntegerWrapper aaa = new IntegerWrapper(40);
    IntegerWrapper bbb = new IntegerWrapper(50);
    IntegerWrapper ccc = new IntegerWrapper(60);

    AutoFunctionQuery<Integer> query = new AutoFunctionQuery<>();
    query.setConfig(runtimeConfig);
    query.setCatalogName("xxx");
    query.setSchemaName("yyy");
    query.setFunctionName("aaa");
    query.setResultParameter(new BasicSingleResultParameter<>(IntegerWrapper::new));
    query.addParameter(new BasicInParameter<>(() -> aaa));
    query.addParameter(new BasicOutParameter<>(() -> bbb, new Reference<>()));
    query.addParameter(new BasicInOutParameter<>(() -> ccc, new Reference<>()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    Integer result = new FunctionCommand<>(query).execute();
    query.complete();

    assertNotNull(result);
    assertEquals(10, result);
    assertEquals(40, aaa.get());
    assertEquals(20, bbb.get());
    assertEquals(30, ccc.get());

    String sql = runtimeConfig.dataSource.connection.callableStatement.sql;
    assertEquals("{? = call xxx.yyy.aaa(?, ?, ?)}", sql);
  }
}
