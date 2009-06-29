package org.seasar.doma.it.entity;

import doma.Entity;
import doma.Table;
import doma.domain.IntegerDomain;

@Entity
@Table(name = "NO_ID")
public interface NoId {

    IntegerDomain value1();

    IntegerDomain value2();

}
