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

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class)
public class Salary {

  private final BigDecimal value;

  public Salary(BigDecimal value) {
    this.value = value;
  }

  public Salary(String value) {
    this(new BigDecimal(value));
  }

  public BigDecimal getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Salary salary = (Salary) o;
    if (value == null && salary.value == null) {
      return true;
    }
    if (value == null || salary.value == null) {
      return false;
    }
    return value.compareTo(salary.value) == 0;
  }

  @Override
  public int hashCode() {
    if (value == null) {
      return 0;
    }
    return value.stripTrailingZeros().hashCode();
  }

  @Override
  public String toString() {
    return value == null ? null : value.toPlainString();
  }
}
