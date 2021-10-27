package org.seasar.doma.it.criteria

import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Metamodel
import org.seasar.doma.OriginalStates
import org.seasar.doma.Transient
import org.seasar.doma.Version
import java.time.LocalDate

@Entity(metamodel = Metamodel())
class Employee {
    @Id
    var employeeId: Int? = null
    var employeeNo: Int? = null
    var employeeName: String? = null
    var managerId: Int? = null
    var hiredate: LocalDate? = null
    var salary: Salary? = null
    var departmentId: Int? = null
    var addressId: Int? = null

    @Version
    var version: Int? = null

    @OriginalStates
    private val states: Employee? = null

    @Transient
    var department: Department? = null

    @Transient
    var manager: Employee? = null

    @Transient
    var address: Address? = null
}
