package org.seasar.doma.it.criteria

import org.seasar.doma.Embeddable
import java.time.LocalDate

@Embeddable
class EmployeeInfo(val hiredate: LocalDate)
