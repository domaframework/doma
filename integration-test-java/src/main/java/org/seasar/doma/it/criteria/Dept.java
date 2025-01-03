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
package org.seasar.doma.it.criteria;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "DEPARTMENT")
public class Dept {

  @Id private final Integer departmentId;
  private final Integer departmentNo;
  private final String departmentName;
  private final String location;
  @Version private final Integer version;

  public Dept(
      Integer departmentId,
      Integer departmentNo,
      String departmentName,
      String location,
      Integer version) {
    this.departmentId = departmentId;
    this.departmentNo = departmentNo;
    this.departmentName = departmentName;
    this.location = location;
    this.version = version;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public Integer getDepartmentNo() {
    return departmentNo;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public String getLocation() {
    return location;
  }

  public Integer getVersion() {
    return version;
  }
}
