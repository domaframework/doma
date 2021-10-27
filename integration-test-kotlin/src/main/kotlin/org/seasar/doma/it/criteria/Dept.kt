package org.seasar.doma.it.criteria

import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Metamodel
import org.seasar.doma.Table
import org.seasar.doma.Version

@Entity(immutable = true, metamodel = Metamodel())
@Table(name = "DEPARTMENT")
data class Dept(
    @Id val departmentId: Int,
    val departmentNo: Int,
    val departmentName: String,
    val location: String,
    @Version val version: Int
)
