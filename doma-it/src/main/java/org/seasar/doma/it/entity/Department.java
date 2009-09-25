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

import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.ModifiedProperties;
import org.seasar.doma.Version;

@Entity
public class Department {

    @Id
    Integer department_id;

    Integer department_no;

    String department_name;

    String location;

    @Version
    Integer version;

    @ModifiedProperties
    Set<String> modifiedProperties = new HashSet<String>();

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer departmentId) {
        modifiedProperties.add("departmentId");
        department_id = departmentId;
    }

    public Integer getDepartment_no() {
        return department_no;
    }

    public void setDepartment_no(Integer departmentNo) {
        modifiedProperties.add("departmentNo");
        department_no = departmentNo;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String departmentName) {
        modifiedProperties.add("departmentName");
        department_name = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        modifiedProperties.add("location");
        this.location = location;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        modifiedProperties.add("version");
        this.version = version;
    }

}
