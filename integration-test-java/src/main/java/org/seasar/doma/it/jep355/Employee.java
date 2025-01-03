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
package org.seasar.doma.it.jep355;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
@Table(name = "EMPLOYEE")
public class Employee {
  @Id public Integer employeeId;
  public Integer employeeNo;
  public String employeeName;
  public Integer managerId;
  public LocalDate hiredate;
  public BigDecimal salary;
  public Integer departmentId;
  public Integer addressId;
  @Version private Integer version;
}
