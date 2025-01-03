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
package org.seasar.doma;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that an argument is {@code null} and it does not allow in the method.
 *
 * <p>This class makes it easy to distinguish Doma's specifications from Doma's bugs.
 */
public class DomaNullPointerException extends DomaException {

  private static final long serialVersionUID = 1L;

  /** the parameter name that corresponds to the argument */
  protected final String parameterName;

  /**
   * Constructs an instance.
   *
   * @param parameterName the parameter name that corresponds to the argument
   */
  public DomaNullPointerException(String parameterName) {
    super(Message.DOMA0001, parameterName);
    this.parameterName = parameterName;
  }

  /**
   * Returns the parameter name that corresponds to the argument.
   *
   * @return the parameter name
   */
  public String getParameterName() {
    return parameterName;
  }
}
