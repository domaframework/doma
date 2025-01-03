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

/** Thrown to indicate that a JDBC related unsupported method is invoked. */
public class JdbcUnsupportedOperationException extends JdbcException {

  private static final long serialVersionUID = 1L;

  protected final String className;

  protected final String methodName;

  public JdbcUnsupportedOperationException(String className, String methodName) {
    super(Message.DOMA2034, className, methodName);
    this.className = className;
    this.methodName = methodName;
  }

  /**
   * Returns the class name.
   *
   * @return the class name
   */
  public String getClassName() {
    return className;
  }

  /**
   * Return the unsupported method name.
   *
   * @return the method name
   */
  public String getMethodName() {
    return methodName;
  }
}
