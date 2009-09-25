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
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.ModifiedProperties;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity
@Table(name = "COMP_KEY_EMPLOYEE")
public class CompKeyEmployee {

    @Id
    Integer employee_id1;

    @Id
    Integer employee_id2;

    Integer employee_no;

    String employee_name;

    Integer manager_id1;

    Integer manager_id2;

    Date hiredate;

    BigDecimal salary;

    Integer department_id1;

    Integer department_id2;

    Integer address_id1;

    Integer address_id2;

    @Version
    Integer version;

    @ModifiedProperties
    Set<String> modifiedProperties = new HashSet<String>();

    public Integer getEmployee_id1() {
        return employee_id1;
    }

    public void setEmployee_id1(Integer employeeId1) {
        modifiedProperties.add("employeeId1");
        employee_id1 = employeeId1;
    }

    public Integer getEmployee_id2() {
        return employee_id2;
    }

    public void setEmployee_id2(Integer employeeId2) {
        modifiedProperties.add("employeeId2");
        employee_id2 = employeeId2;
    }

    public Integer getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(Integer employeeNo) {
        modifiedProperties.add("employeeNo");
        employee_no = employeeNo;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employeeName) {
        modifiedProperties.add("employeeName");
        employee_name = employeeName;
    }

    public Integer getManager_id1() {
        return manager_id1;
    }

    public void setManager_id1(Integer managerId1) {
        modifiedProperties.add("managerId1");
        manager_id1 = managerId1;
    }

    public Integer getManager_id2() {
        return manager_id2;
    }

    public void setManager_id2(Integer managerId2) {
        modifiedProperties.add("managerId2");
        manager_id2 = managerId2;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
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

    public Integer getAddress_id1() {
        return address_id1;
    }

    public void setAddress_id1(Integer addressId1) {
        modifiedProperties.add("addressId1");
        address_id1 = addressId1;
    }

    public Integer getAddress_id2() {
        return address_id2;
    }

    public void setAddress_id2(Integer addressId2) {
        modifiedProperties.add("addressId2");
        address_id2 = addressId2;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        modifiedProperties.add("version");
        this.version = version;
    }

}
