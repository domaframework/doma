package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;
import org.seasar.doma.domain.DateDomain;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.NoDomain;
import org.seasar.doma.it.domain.SalaryDomain;
import org.seasar.doma.it.domain.VersionDomain;

@Entity
public interface Employee {

    @Id
    IdDomain employee_id();

    NoDomain employee_no();

    NameDomain employee_name();

    IdDomain manager_id();

    DateDomain hiredate();

    SalaryDomain salary();

    IdDomain department_id();

    IdDomain address_id();

    @Version
    VersionDomain version();
}
