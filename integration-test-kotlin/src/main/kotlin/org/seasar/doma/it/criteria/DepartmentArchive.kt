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
package org.seasar.doma.it.criteria

import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Metamodel
import org.seasar.doma.OriginalStates
import org.seasar.doma.Transient
import org.seasar.doma.Version

@Entity(metamodel = Metamodel())
class DepartmentArchive {
    @Id
    var departmentId: Int? = null
    var departmentNo: Int? = null
    var departmentName: String? = null
    var location: String? = null

    @Version
    var version: Int? = null

    @OriginalStates
    var originalStates: DepartmentArchive? = null

    @Transient
    var employeeList: List<Employee> = mutableListOf()
}
