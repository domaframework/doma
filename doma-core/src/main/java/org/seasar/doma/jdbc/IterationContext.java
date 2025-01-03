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

/** An execution context of {@link IterationCallback}. */
public class IterationContext {

  /** whether {@link #exit()} is invoked */
  protected boolean exited;

  /**
   * Whether {@link #exit()} is invoked.
   *
   * @return {@code true} if {@link #exit()} is invoked
   */
  public boolean isExited() {
    return exited;
  }

  /** Exits from an execution of {@link IterationCallback}. */
  public void exit() {
    this.exited = true;
  }
}
