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
package org.seasar.doma.it.criteria

import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Metamodel
import org.seasar.doma.Table
import org.seasar.doma.Transient
import org.seasar.doma.Version
import java.time.LocalDate

@Entity(immutable = true, metamodel = Metamodel())
@Table(name = "EMPLOYEE")
data class Emp(
    @Id val employeeId: Int,
    val employeeNo: Int,
    val employeeName: String,
    val managerId: Int,
    val hiredate: LocalDate,
    val salary: Salary,
    val departmentId: Int,
    val addressId: Int,
    @Version val version: Int,
    @Transient val department: Dept?,
    @Transient val manager: Emp?,
) {

    constructor(
        employeeId: Int,
        employeeNo: Int,
        employeeName: String,
        managerId: Int,
        hiredate: LocalDate,
        salary: Salary,
        departmentId: Int,
        addressId: Int,
        version: Int,
    ) : this(employeeId, employeeNo, employeeName, managerId, hiredate, salary, departmentId, addressId, version, null, null)
}
