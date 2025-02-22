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
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity
@Table(name = "COMP_KEY_DEPARTMENT")
public class CompKeyDepartment {

  @Id Integer departmentId1;

  @Id Integer departmentId2;

  Integer departmentNo;

  String departmentName;

  String location;

  @Version Integer version;

  @OriginalStates CompKeyDepartment originalStates;

  public Integer getDepartmentId1() {
    return departmentId1;
  }

  public void setDepartmentId1(Integer departmentId1) {
    this.departmentId1 = departmentId1;
  }

  public Integer getDepartmentId2() {
    return departmentId2;
  }

  public void setDepartmentId2(Integer departmentId2) {
    this.departmentId2 = departmentId2;
  }

  public Integer getDepartmentNo() {
    return departmentNo;
  }

  public void setDepartmentNo(Integer departmentNo) {
    this.departmentNo = departmentNo;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
