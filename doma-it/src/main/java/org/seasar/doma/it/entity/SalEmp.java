package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.domain.BuiltinArrayDomain;
import org.seasar.doma.it.domain.NameDomain;

@Entity
@Table(name = "SAL_EMP")
public interface SalEmp {

    @Id
    NameDomain name();

    BuiltinArrayDomain<Integer> pay_by_quarter();

    BuiltinArrayDomain<String[]> schedule();
}
