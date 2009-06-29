package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.domain.DateDomain;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.NoDomain;
import org.seasar.doma.it.domain.SalaryDomain;
import org.seasar.doma.it.domain.VersionDomain;

@Entity
@Table(name = "COMP_KEY_EMPLOYEE")
public interface CompKeyEmployee {

    @Id
    IdDomain employee_id1();

    @Id
    IdDomain employee_id2();

    NoDomain employee_no();

    NameDomain employee_name();

    IdDomain manager_id1();

    IdDomain manager_id2();

    DateDomain hiredate();

    SalaryDomain salary();

    IdDomain department_id1();

    IdDomain department_id2();

    IdDomain address_id1();

    IdDomain address_id2();

    @Version
    VersionDomain version();
}
