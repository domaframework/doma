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
package org.seasar.doma.it.entity;

import java.sql.Array;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;

@Entity
@Table(name = "SAL_EMP")
public class SalEmp {

  @Id String name;

  Array payByQuarter;

  Array schedule;

  @OriginalStates SalEmp originalStates;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Array getPayByQuarter() {
    return payByQuarter;
  }

  public void setPayByQuarter(Array payByQuarter) {
    this.payByQuarter = payByQuarter;
  }

  public Array getSchedule() {
    return schedule;
  }

  public void setSchedule(Array schedule) {
    this.schedule = schedule;
  }
}
