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
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity
@Table(name = "COMP_KEY_DEPARTMENT")
public class CompKeyDepartment {

    @Id
    Integer department_id1;

    @Id
    Integer department_id2;

    Integer department_no;

    String department_name;

    String location;

    @Version
    Integer version;

    @ModifiedProperties
    Set<String> modifiedProperties = new HashSet<String>();

    public Integer getDepartment_id1() {
        return department_id1;
    }

    public void setDepartment_id1(Integer departmentId1) {
        modifiedProperties.add("departmentId1");
        department_id1 = departmentId1;
    }

    public Integer getDepartment_id2() {
        return department_id2;
    }

    public void setDepartment_id2(Integer departmentId2) {
        modifiedProperties.add("departmentId2");
        department_id2 = departmentId2;
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
