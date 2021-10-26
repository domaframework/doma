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
    @Transient val manager: Emp?
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
        version: Int
    ) : this(employeeId, employeeNo, employeeName, managerId, hiredate, salary, departmentId, addressId, version, null, null)
}
