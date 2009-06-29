package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Table;
import org.seasar.doma.domain.IntegerDomain;

@Entity
@Table(name = "NO_ID")
public interface NoId {

    IntegerDomain value1();

    IntegerDomain value2();

}
