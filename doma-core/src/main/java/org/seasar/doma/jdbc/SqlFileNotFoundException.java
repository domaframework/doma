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
package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/** Thrown to indicate that the SQL file is not found. */
public class SqlFileNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** the SQL file path */
  protected final String path;

  /**
   * Creates an instance.
   *
   * @param path the SQL file path
   */
  public SqlFileNotFoundException(String path) {
    super(Message.DOMA2011, path);
    this.path = path;
  }

  /**
   * Returns the SQL file path.
   *
   * @return the SQL file path
   */
  public String getPath() {
    return path;
  }
}
