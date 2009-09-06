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
package org.seasar.doma.it.entity;

import java.util.List;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.domain.BuiltinStringDomain;
import org.seasar.doma.domain.BuiltinTimestampDomain;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.SalaryDomain;
import org.seasar.doma.it.domain.VersionDomain;

/**
 * 
 * @author taedium
 * 
 */
@Entity(listener = EmpListener.class)
public interface Emp {

    @Id
    IdDomain id();

    NameDomain name();

    SalaryDomain salary();

    @Version
    VersionDomain version();

    BuiltinTimestampDomain insertTimestamp();

    BuiltinTimestampDomain updateTimestamp();

    @Transient
    BuiltinStringDomain temp();

    @Transient
    List<BuiltinStringDomain> tempList();

}
