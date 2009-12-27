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
import java.util.List;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * 
 * @author taedium
 * 
 */
@Entity(listener = EmpListener.class, naming = NamingType.SNAKE_UPPER_CASE)
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

    @OriginalStates
    Emp originalStates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Timestamp getInsertTimestamp() {
        return insertTimestamp;
    }

    public void setInsertTimestamp(Timestamp insertTimestamp) {
        this.insertTimestamp = insertTimestamp;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public List<String> getTempList() {
        return tempList;
    }

    public void setTempList(List<String> tempList) {
        this.tempList = tempList;
    }

}
