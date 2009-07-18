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
package org.seasar.doma.copy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class StandardCopierTest extends TestCase {

    private StandardCopier copier = new StandardCopier();

    public void testFromEntityToEntity() throws Exception {
        Emp src = new Emp_();
        src.id().set(10);
        src.name().set("aaa");
        src.salary().set(new BigDecimal("100"));
        src.version().set(20);
        Emp dest = new Emp_();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.id(), dest.id());
        assertEquals(src.name(), dest.name());
        assertEquals(src.salary(), dest.salary());
        assertEquals(src.version(), dest.version());
    }

    public void testFromEntityToBean() throws Exception {
        Emp src = new Emp_();
        src.id().set(10);
        src.name().set("aaa");
        src.salary().set(new BigDecimal("100"));
        src.version().set(20);
        EmpBean dest = new EmpBean();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.id().get(), dest.id);
        assertEquals(src.name().get(), dest.name);
        assertEquals(src.salary().get(), dest.salary);
        assertEquals(src.version().get(), dest.version);
    }

    public void testFromEntityToMap() throws Exception {
        Emp src = new Emp_();
        src.id().set(10);
        src.name().set("aaa");
        src.salary().set(new BigDecimal("100"));
        src.version().set(20);
        Map<String, Object> dest = new HashMap<String, Object>();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.id().get(), dest.get("id"));
        assertEquals(src.name().get(), dest.get("name"));
        assertEquals(src.salary().get(), dest.get("salary"));
        assertEquals(src.version().get(), dest.get("version"));
    }

    public void testFromBeanToEntity() throws Exception {
        EmpBean src = new EmpBean();
        src.id = 10;
        src.name = "aaa";
        src.salary = new BigDecimal("100");
        src.version = 20;
        Emp dest = new Emp_();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.id, dest.id().get());
        assertEquals(src.name, dest.name().get());
        assertEquals(src.salary, dest.salary().get());
        assertEquals(src.version, dest.version().get());
    }

    public void testFromBeanToBean() throws Exception {
        EmpBean src = new EmpBean();
        src.id = 10;
        src.name = "aaa";
        src.salary = new BigDecimal("100");
        src.version = 20;
        EmpBean dest = new EmpBean();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.id, dest.id);
        assertEquals(src.name, dest.name);
        assertEquals(src.salary, dest.salary);
        assertEquals(src.version, dest.version);
    }

    public void testFromBeanToMap() throws Exception {
        EmpBean src = new EmpBean();
        src.id = 10;
        src.name = "aaa";
        src.salary = new BigDecimal("100");
        src.version = 20;
        Map<String, Object> dest = new HashMap<String, Object>();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.id, dest.get("id"));
        assertEquals(src.name, dest.get("name"));
        assertEquals(src.salary, dest.get("salary"));
        assertEquals(src.version, dest.get("version"));
    }

    public void testFromMapToEntity() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("id", 10);
        src.put("name", "aaa");
        src.put("salary", new BigDecimal("100"));
        src.put("version", 20);
        Emp dest = new Emp_();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.get("id"), dest.id().get());
        assertEquals(src.get("name"), dest.name().get());
        assertEquals(src.get("salary"), dest.salary().get());
        assertEquals(src.get("version"), dest.version().get());
    }

    public void testFromMapToBean() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("id", 10);
        src.put("name", "aaa");
        src.put("salary", new BigDecimal("100"));
        src.put("version", 20);
        EmpBean dest = new EmpBean();
        copier.copy(src, dest, new CopyOptions());

        assertEquals(src.get("id"), dest.id);
        assertEquals(src.get("name"), dest.name);
        assertEquals(src.get("salary"), dest.salary);
        assertEquals(src.get("version"), dest.version);
    }

    public void testPropertyCopyException() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("id", "aaa");
        EmpBean dest = new EmpBean();
        try {
            copier.copy(src, dest, new CopyOptions());
            fail();
        } catch (PropertyCopyException expected) {
        }
    }

    public static class EmpBean {

        public Integer id;

        public String name;

        public BigDecimal salary;

        public Integer version;
    }
}
