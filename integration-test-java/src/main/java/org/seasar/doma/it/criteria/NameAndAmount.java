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

import java.math.BigDecimal;
import java.util.Objects;
import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel)
public class NameAndAmount {
  private String name;
  private Integer amount;

  public NameAndAmount() {}

  public NameAndAmount(String accounting, BigDecimal bigDecimal) {
    this.name = accounting;
    this.amount = bigDecimal.intValue();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NameAndAmount that = (NameAndAmount) o;
    return Objects.equals(name, that.name) && Objects.equals(amount, that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, amount);
  }
}
