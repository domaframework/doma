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
package example.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.ChangedProperties;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;

@Entity
public class Emp {

    @Id
    Integer id;

    String name;

    BigDecimal salary;

    @Version
    Integer version;

    @ChangedProperties
    Set<String> dirtyStates = new HashSet<String>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        dirtyStates.add("id");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        dirtyStates.add("name");
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        dirtyStates.add("salary");
        this.salary = salary;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        dirtyStates.add("version");
        this.version = version;
    }

}
