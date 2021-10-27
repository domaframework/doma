package org.seasar.doma.it.criteria

import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Metamodel
import org.seasar.doma.OriginalStates
import org.seasar.doma.Transient
import org.seasar.doma.Version

@Entity(metamodel = Metamodel())
class Department {

    @Id
    var departmentId: Int? = null
    var departmentNo: Int? = null
    var departmentName: String? = null
    var location: String? = null

    @Version
    var version: Int? = null

    @OriginalStates
    var originalStates: Department? = null

    @Transient
    var employeeList: List<Employee> = mutableListOf()
}
