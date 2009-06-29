package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.domain.IntegerDomain;

/**
 * @author taedium
 * 
 */
@Entity
public interface ParentEntity {

    IntegerDomain aaa();

    IntegerDomain bbb();

}
