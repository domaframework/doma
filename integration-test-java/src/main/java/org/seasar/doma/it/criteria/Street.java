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
package org.seasar.doma.it.criteria;

import java.util.Objects;
import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class Street implements CharSequence {
  private final String value;

  public Street(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int index) {
    return value.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return value.subSequence(start, end);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Street street = (Street) o;
    return Objects.equals(value, street.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
