/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.entity;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.Association;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Salary;

@Entity(metamodel = @Metamodel)
public class Employee {

  @Id private Integer employeeId;

  private Integer employeeNo;

  private String employeeName;

  private Integer managerId;

  private java.sql.Date hiredate;

  private Salary salary;

  private Identity<Department> departmentId;

  private Integer addressId;

  @Version private Integer version;

  @OriginalStates private Employee originalStates;

  @Association Department department;

  @Association Address address;

  @Association Employee manager;

  @Association List<Employee> assistants = new ArrayList<>();

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

  public Identity<Department> getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Identity<Department> departmentId) {
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

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Employee getManager() {
    return manager;
  }

  public void setManager(Employee manager) {
    this.manager = manager;
  }

  public List<Employee> getAssistants() {
    return assistants;
  }

  public void setAssistant(List<Employee> assistants) {
    this.assistants = assistants;
  }
}
