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
package org.seasar.doma.it.domain;

import java.util.Objects;
import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class Identity<T> {

  private final Integer value;

  public Identity(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Identity<?> identity = (Identity<?>) o;
    return Objects.equals(value, identity.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
