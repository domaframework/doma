/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Optional;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Salary;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author nakamura-to
 *
 */
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
@Table(name = "EMPLOYEE")
public class Worker {

    @Id
    public Optional<Integer> employeeId;

    public Optional<Integer> employeeNo;

    public Optional<String> employeeName;

    public Optional<Integer> managerId;

    public Optional<java.sql.Date> hiredate;

    public Optional<Salary> salary;

    public Optional<Identity<Department>> departmentId;

    public Optional<Integer> addressId;

    @Version
    public Optional<Integer> version;
}
