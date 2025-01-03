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
package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class AutoFunctionQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  @Test
  public void testPrepare() {
    AutoFunctionQuery<Integer> query = new AutoFunctionQuery<>();
    query.setConfig(runtimeConfig);
    query.setFunctionName("aaa");
    query.setResultParameter(new BasicSingleResultParameter<>(IntegerWrapper::new));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertNotNull(query.getSql());
  }
}
