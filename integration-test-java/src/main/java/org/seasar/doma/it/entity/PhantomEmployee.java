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

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Income;

@Entity
@Table(name = "EMPLOYEE")
public class PhantomEmployee {

  @Id private int employeeId;

  private short employeeNo;

  private String employeeName;

  private byte managerId;

  private java.sql.Date hiredate;

  private Income salary;

  private float departmentId;

  private double addressId;

  @Version private long version;

  public int getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(int employeeId) {
    this.employeeId = employeeId;
  }

  public short getEmployeeNo() {
    return employeeNo;
  }

  public void setEmployeeNo(short employeeNo) {
    this.employeeNo = employeeNo;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public byte getManagerId() {
    return managerId;
  }

  public void setManagerId(byte managerId) {
    this.managerId = managerId;
  }

  public java.sql.Date getHiredate() {
    return hiredate;
  }

  public void setHiredate(java.sql.Date hiredate) {
    this.hiredate = hiredate;
  }

  public Income getSalary() {
    return salary;
  }

  public void setSalary(Income salary) {
    this.salary = salary;
  }

  public float getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(float departmentId) {
    this.departmentId = departmentId;
  }

  public double getAddressId() {
    return addressId;
  }

  public void setAddressId(double addressId) {
    this.addressId = addressId;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }
}
