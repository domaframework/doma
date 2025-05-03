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

import org.seasar.doma.message.MessageResource;

/**
 * The root exception in the Doma framework.
 *
 * <p>All exceptions thrown by the Doma framework extend this class.
 */
public class DomaException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /** the message resource */
  protected final MessageResource messageResource;

  /** the message arguments */
  protected final Object args;

  /**
   * Constructs an instance.
   *
   * @param messageResource the message resource
   * @param args the message arguments
   */
  public DomaException(MessageResource messageResource, Object... args) {
    this(messageResource, null, args);
  }

  /**
   * Constructs an instance with a cause.
   *
   * @param messageResource the message resource
   * @param cause the cause error or exception
   * @param args the message arguments
   */
  public DomaException(MessageResource messageResource, Throwable cause, Object... args) {
    super(messageResource.getMessage(args), cause);
    this.messageResource = messageResource;
    this.args = args;
  }

  /**
   * Returns the message resource.
   *
   * @return the message resource
   */
  public MessageResource getMessageResource() {
    return messageResource;
  }

  /**
   * Returns the message arguments.
   *
   * @return the message arguments
   */
  public Object getArgs() {
    return args;
  }
}
