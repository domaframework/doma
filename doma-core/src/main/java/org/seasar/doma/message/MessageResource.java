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
package org.seasar.doma.message;

/**
 * A message resource.
 *
 * <p>This interface implementation instance must be thread safe.
 */
public interface MessageResource {

  /**
   * Returns a message code.
   *
   * @return a message code
   */
  String getCode();

  /**
   * Returns the string that contains replacement parameters such as {0} and {1}.
   *
   * @return the string that contains replacement parameters
   */
  String getMessagePattern();

  /**
   * Returns the message that contains a message code.
   *
   * @param args the arguments that corresponds to replacement parameters
   * @return the message
   */
  String getMessage(Object... args);

  /**
   * Returns the message that does not contains a message code.
   *
   * @param args the arguments that corresponds to replacement parameters
   * @return the message
   */
  String getSimpleMessage(Object... args);
}
