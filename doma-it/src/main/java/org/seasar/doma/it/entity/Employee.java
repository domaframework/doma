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
import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.ModifiedProperties;
import org.seasar.doma.Version;

@Entity
public class Employee {

    @Id
    Integer employee_id;

    Integer employee_no;

    String employee_name;

    Integer manager_id;

    java.sql.Date hiredate;

    BigDecimal salary;

    Integer department_id;

    Integer address_id;

    @Version
    Integer version;

    @ModifiedProperties
    Set<String> modifiedProperties = new HashSet<String>();

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        modifiedProperties.add("employee_id");
        this.employee_id = employee_id;
    }

    public Integer getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(Integer employeeNo) {
        modifiedProperties.add("employee_no");
        employee_no = employeeNo;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employeeName) {
        modifiedProperties.add("employee_name");
        employee_name = employeeName;
    }

    public Integer getManager_id() {
        return manager_id;
    }

    public void setManager_id(Integer managerId) {
        modifiedProperties.add("manager_id");
        manager_id = managerId;
    }

    public java.sql.Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(java.sql.Date hiredate) {
        modifiedProperties.add("hiredate");
        this.hiredate = hiredate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        modifiedProperties.add("salary");
        this.salary = salary;
    }

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer departmentId) {
        modifiedProperties.add("department_id");
        department_id = departmentId;
    }

    public Integer getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Integer addressId) {
        modifiedProperties.add("address_id");
        address_id = addressId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        modifiedProperties.add("version");
        this.version = version;
    }

}
