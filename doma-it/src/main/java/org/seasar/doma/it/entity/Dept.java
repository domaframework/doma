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

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_UPPER_CASE, immutable = true, listener = DeptListener.class)
@Table(name = "DEPARTMENT")
public class Dept {

    @Id
    final Identity<Dept> departmentId;

    final Integer departmentNo;

    final String departmentName;

    final Location<Dept> location;

    @Version
    final Integer version;

    public Dept(Identity<Dept> departmentId, Integer departmentNo,
            String departmentName, Location<Dept> location, Integer version) {
        super();
        this.departmentId = departmentId;
        this.departmentNo = departmentNo;
        this.departmentName = departmentName;
        this.location = location;
        this.version = version;
    }

    /**
     * @return the departmentId
     */
    public Identity<Dept> getDepartmentId() {
        return departmentId;
    }

    /**
     * @return the departmentNo
     */
    public Integer getDepartmentNo() {
        return departmentNo;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @return the location
     */
    public Location<Dept> getLocation() {
        return location;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

}
