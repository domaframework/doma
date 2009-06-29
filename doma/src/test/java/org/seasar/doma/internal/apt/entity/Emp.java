package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;

@Entity(listener = EmpListener.class)
@Table(schema = "AAA")
public interface Emp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "EMP_ID")
    IntegerDomain id();

    StringDomain name();

    @Column(name = "SALARY", insertable = false, updatable = false)
    BigDecimalDomain salary();

    @Version
    IntegerDomain version();

    @Transient
    StringDomain temp();

}
