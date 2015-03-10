/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.jdbc.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.ImmutableEmp;
import example.entity._Emp;
import example.entity._ImmutableEmp;

/**
 * @author nakamura-to
 * 
 */
public class EntityTypeTest extends TestCase {

    public void test() throws Exception {
        EntityType<Emp> entityType = _Emp.getSingletonInternal();
        entityType.getQualifiedTableName();
    }

    public void testImmutable_newEntity() throws Exception {
        ImmutableEmp emp = new ImmutableEmp(99, "hoge", BigDecimal.ONE, 1);
        EntityType<ImmutableEmp> entityType = _ImmutableEmp
                .getSingletonInternal();
        Map<String, Property<ImmutableEmp, ?>> args = new HashMap<>();

        EntityPropertyType<ImmutableEmp, ?> idType = entityType
                .getEntityPropertyType("id");
        Property<ImmutableEmp, ?> id = idType.createProperty();
        id.load(emp);
        args.put(idType.getName(), id);

        EntityPropertyType<ImmutableEmp, ?> salaryType = entityType
                .getEntityPropertyType("salary");
        Property<ImmutableEmp, ?> salary = salaryType.createProperty();
        salary.load(emp);
        args.put(salaryType.getName(), salary);

        ImmutableEmp newEmp = entityType.newEntity(args);

        assertEquals(Integer.valueOf(99), newEmp.getId());
        assertNull(newEmp.getName());
        assertEquals(BigDecimal.ONE, newEmp.getSalary());
        assertNull(newEmp.getVersion());
    }
}
