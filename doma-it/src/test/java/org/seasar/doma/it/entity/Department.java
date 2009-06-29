package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.LocationDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.NoDomain;
import org.seasar.doma.it.domain.VersionDomain;

@Entity
public interface Department {

    @Id
    IdDomain department_id();

    NoDomain department_no();

    NameDomain department_name();

    LocationDomain location();

    @Version
    VersionDomain version();
}
