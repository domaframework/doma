package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.jdbc.domain.ArrayDomain;

@Entity
@Table(name = "SAL_EMP")
public interface SalEmp {

    @Id
    StringDomain name();

    ArrayDomain<Integer> pay_by_quarter();
}
