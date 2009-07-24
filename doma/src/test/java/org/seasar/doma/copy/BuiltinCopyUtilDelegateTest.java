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
public class BuiltinCopyUtilDelegateTest extends TestCase {

    private BuiltinCopyUtilDelegate delegate = new BuiltinCopyUtilDelegate();

    public void testFromEntityToEntity() throws Exception {
        Emp src = new Emp_();
        src.id().set(10);
        src.name().set("aaa");
        src.salary().set(new BigDecimal("100"));
        src.version().set(20);
        Emp dest = new Emp_();
        delegate.copy(src, dest, new CopyOptions());

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
        delegate.copy(src, dest, new CopyOptions());

        assertEquals(src.id().get(), dest.getId());
        assertEquals(src.name().get(), dest.getName());
        assertEquals(src.salary().get(), dest.getSalary());
        assertEquals(src.version().get(), dest.getVersion());
    }

    public void testFromEntityToMap() throws Exception {
        Emp src = new Emp_();
        src.id().set(10);
        src.name().set("aaa");
        src.salary().set(new BigDecimal("100"));
        src.version().set(20);
        Map<String, Object> dest = new HashMap<String, Object>();
        delegate.copy(src, dest, new CopyOptions());

        assertEquals(src.id().get(), dest.get("id"));
        assertEquals(src.name().get(), dest.get("name"));
        assertEquals(src.salary().get(), dest.get("salary"));
        assertEquals(src.version().get(), dest.get("version"));
    }

    public void testFromBeanToEntity() throws Exception {
        EmpBean src = new EmpBean();
        src.setId(10);
        src.setName("aaa");
        src.setSalary(new BigDecimal("100"));
        src.setVersion(20);
        Emp dest = new Emp_();
        delegate.copy(src, dest, new CopyOptions());

        assertEquals(src.getId(), dest.id().get());
        assertEquals(src.getName(), dest.name().get());
        assertEquals(src.getSalary(), dest.salary().get());
        assertEquals(src.getVersion(), dest.version().get());
    }

    public void testFromBeanToBean() throws Exception {
        EmpBean src = new EmpBean();
        src.setId(10);
        src.setName("aaa");
        src.setSalary(new BigDecimal("100"));
        src.setVersion(20);
        EmpBean dest = new EmpBean();
        delegate.copy(src, dest, new CopyOptions());

        assertEquals(src.getId(), dest.getId());
        assertEquals(src.getName(), dest.getName());
        assertEquals(src.getSalary(), dest.getSalary());
        assertEquals(src.getVersion(), dest.getVersion());
    }

    public void testFromBeanToMap() throws Exception {
        EmpBean src = new EmpBean();
        src.setId(10);
        src.setName("aaa");
        src.setSalary(new BigDecimal("100"));
        src.setVersion(20);
        Map<String, Object> dest = new HashMap<String, Object>();
        delegate.copy(src, dest, new CopyOptions());

        assertEquals(src.getId(), dest.get("id"));
        assertEquals(src.getName(), dest.get("name"));
        assertEquals(src.getSalary(), dest.get("salary"));
        assertEquals(src.getVersion(), dest.get("version"));
    }

    public void testFromMapToEntity() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("id", 10);
        src.put("name", "aaa");
        src.put("salary", new BigDecimal("100"));
        src.put("version", 20);
        Emp dest = new Emp_();
        delegate.copy(src, dest, new CopyOptions());

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
        delegate.copy(src, dest, new CopyOptions());

        assertEquals(src.get("id"), dest.getId());
        assertEquals(src.get("name"), dest.getName());
        assertEquals(src.get("salary"), dest.getSalary());
        assertEquals(src.get("version"), dest.getVersion());
    }

    public void testPropertyCopyException() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("id", "aaa");
        EmpBean dest = new EmpBean();
        try {
            delegate.copy(src, dest, new CopyOptions());
            fail();
        } catch (PropertyCopyException expected) {
        }
    }

    public static class EmpBean {

        private Integer id;

        private String name;

        private BigDecimal salary;

        private Integer version;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Integer getVersion() {
            return version;
        }
    }
}
