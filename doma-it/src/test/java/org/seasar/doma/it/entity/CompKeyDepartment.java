package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.LocationDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.NoDomain;
import org.seasar.doma.it.domain.VersionDomain;

@Entity
@Table(name = "COMP_KEY_DEPARTMENT")
public interface CompKeyDepartment {

    @Id
    IdDomain department_id1();

    @Id
    IdDomain department_id2();

    NoDomain department_no();

    NameDomain department_name();

    LocationDomain location();

    @Version
    VersionDomain version();
}
