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
package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class, factoryMethod = "of")
public class IllegalSizeParameterizedOfSalary<T, U> {

  private final BigDecimal value;

  private IllegalSizeParameterizedOfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static <T, U, V> IllegalSizeParameterizedOfSalary<T, U> of(BigDecimal value) {
    return new IllegalSizeParameterizedOfSalary<>(value);
  }
}
