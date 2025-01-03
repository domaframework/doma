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
package org.seasar.doma.internal.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.query.Query;

public class PreparedSqlParameterBinderTest {

  private final MockConfig runtimeConfig = new MockConfig();

  @Test
  public void testBind() throws Exception {
    MockPreparedStatement preparedStatement = new MockPreparedStatement();
    List<BasicInParameter<?>> parameters = new ArrayList<>();
    parameters.add(new BasicInParameter<>(() -> new org.seasar.doma.wrapper.StringWrapper("aaa")));
    parameters.add(
        new BasicInParameter<>(
            () -> new org.seasar.doma.wrapper.BigDecimalWrapper(new BigDecimal(10))));
    PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(new MyQuery());
    binder.bind(preparedStatement, parameters);

    List<BindValue> bindValues = preparedStatement.bindValues;
    assertEquals(2, bindValues.size());
    BindValue bindValue = bindValues.get(0);
    assertEquals(1, bindValue.getIndex());
    assertEquals("aaa", bindValue.getValue());
    bindValue = bindValues.get(1);
    assertEquals(2, bindValue.getIndex());
    assertEquals(new BigDecimal(10), bindValue.getValue());
  }

  protected class MyQuery implements Query {

    @Override
    public Sql<?> getSql() {
      return null;
    }

    @Override
    public Config getConfig() {
      return runtimeConfig;
    }

    @Override
    public String getClassName() {
      return null;
    }

    @Override
    public String getMethodName() {
      return null;
    }

    @Override
    public int getQueryTimeout() {
      return 0;
    }

    @Override
    public void prepare() {}

    @Override
    public void complete() {}

    @Override
    public Method getMethod() {
      return null;
    }

    @Override
    public String comment(String sql) {
      return sql;
    }
  }
}
