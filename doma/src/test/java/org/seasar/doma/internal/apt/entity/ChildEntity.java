package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;

/**
 * @author taedium
 * 
 */
@Entity
public interface ChildEntity extends ParentEntity {

    IntegerDomain bbb();

    StringDomain ccc();
}
