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

import java.io.Serializable;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Version;
import org.seasar.doma.it.ItNamingConvention;
import org.seasar.doma.it.domain.Salary;

@Entity(namingConvention = ItNamingConvention.class)
public class Employee {

    @Id
    Integer employeeId;

    Integer employeeNo;

    String employeeName;

    Integer managerId;

    java.sql.Date hiredate;

    Salary salary;

    Integer departmentId;

    Integer addressId;

    @Version
    Integer version;

    @OriginalStates
    Serializable originalStates;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employee_id) {
        this.employeeId = employee_id;
    }

    public Integer getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(Integer employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public java.sql.Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(java.sql.Date hiredate) {
        this.hiredate = hiredate;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
