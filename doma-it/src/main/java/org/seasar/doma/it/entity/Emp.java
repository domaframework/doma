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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.doma.ChangedProperties;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.it.ItNamingConvention;

/**
 * 
 * @author taedium
 * 
 */
@Entity(listener = EmpListener.class, namingConvention = ItNamingConvention.class)
public class Emp {

    @Id
    Integer id;

    String name;

    BigDecimal salary;

    @Version
    Integer version;

    Timestamp insertTimestamp;

    Timestamp updateTimestamp;

    @Transient
    String temp;

    @Transient
    List<String> tempList;

    @ChangedProperties
    Set<String> changedProperties = new HashSet<String>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        changedProperties.add("id");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        changedProperties.add("name");
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        changedProperties.add("salary");
        this.salary = salary;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        changedProperties.add("version");
        this.version = version;
    }

    public Timestamp getInsertTimestamp() {
        return insertTimestamp;
    }

    public void setInsertTimestamp(Timestamp insertTimestamp) {
        changedProperties.add("insertTimestamp");
        this.insertTimestamp = insertTimestamp;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        changedProperties.add("updateTimestamp");
        this.updateTimestamp = updateTimestamp;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        changedProperties.add("temp");
        this.temp = temp;
    }

    public List<String> getTempList() {
        return tempList;
    }

    public void setTempList(List<String> tempList) {
        changedProperties.add("tempList");
        this.tempList = tempList;
    }

}
