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

import junit.framework.TestCase;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.DefaultClassHelper;

import example.entity.Emp;

/**
 * @author taedium
 * 
 */
public class EntityTypeFactoryTest extends TestCase {

    public void testGetEntityType() throws Exception {
        EntityType<Emp> type = EntityTypeFactory.getEntityType(Emp.class,
                new DefaultClassHelper());
        assertNotNull(type);
    }

    public void testGetEntityType_DomaIllegalArgumentException()
            throws Exception {
        try {
            EntityTypeFactory.getEntityType(Object.class,
                    new DefaultClassHelper());
            fail();
        } catch (DomaIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testGetEntityType_EntityTypeNotFoundException()
            throws Exception {
        try {
            EntityTypeFactory.getEntityType(Dept.class,
                    new DefaultClassHelper());
            fail();
        } catch (EntityTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
