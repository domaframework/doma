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
package org.seasar.doma.it.embeddable;

import java.sql.Date;
import org.seasar.doma.Embeddable;
import org.seasar.doma.it.domain.Salary;

@Embeddable
public class StaffInfo {

  public final int managerId;

  public final Date hiredate;

  public final Salary salary;

  public StaffInfo(int managerId, Date hiredate, Salary salary) {
    this.managerId = managerId;
    this.hiredate = hiredate;
    this.salary = salary;
  }
}
