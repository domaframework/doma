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

public class RegisterOutParameter {

  protected final int index;

  protected final int sqlType;

  public RegisterOutParameter(int index, int sqlType) {
    this.index = index;
    this.sqlType = sqlType;
  }

  public int getIndex() {
    return index;
  }

  public int getSqlType() {
    return sqlType;
  }
}
