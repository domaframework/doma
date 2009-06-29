package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.domain.IntegerDomain;

/**
 * @author taedium
 * 
 */
@Entity
public interface ReturnTypeNotDomainEntity {

    @Id
    IntegerDomain id();

    String name();
}
