package org.seasar.doma.it.jep384;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
@Table(name = "EMPLOYEE")
public record Worker(
    @Id Integer employeeId,
    Integer employeeNo,
    String employeeName,
    WorkerInfo workerInfo,
    Salary salary,
    Integer departmentId,
    Integer addressId,
    @Version Integer version) {}
