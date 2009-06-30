/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
