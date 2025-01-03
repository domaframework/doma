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
package org.seasar.doma.internal.apt.processor.dao;

import javax.sql.DataSource;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class MyConfig implements Config {

  protected MockDataSource dataSource = new MockDataSource();

  protected StandardDialect dialect = new StandardDialect();

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public StandardDialect getDialect() {
    return dialect;
  }
}
