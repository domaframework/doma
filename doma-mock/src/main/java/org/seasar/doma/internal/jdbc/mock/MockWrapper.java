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
package org.seasar.doma.internal.jdbc.mock;

import java.sql.SQLException;
import java.sql.Wrapper;

@SuppressWarnings("RedundantThrows")
public class MockWrapper implements Wrapper {

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface != null && iface.isAssignableFrom(getClass());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface == null) {
      throw new SQLException("iface must not be null");
    }
    if (iface.isAssignableFrom(getClass())) {
      return (T) this;
    }
    throw new SQLException("cannot unwrap to " + iface.getName());
  }
}
