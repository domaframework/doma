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
package org.seasar.doma.jdbc.builder;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

import example.domain.PhoneNumber;
import example.entity.Emp;

/**
 * @author taedium
 * 
 */
public class SelectBuilderTest extends TestCase {

    public void testGetSql() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select");
        builder.sql("id").sql(",");
        builder.sql("name").sql(",");
        builder.sql("salary");
        builder.sql("from Emp");
        builder.sql("where");
        builder.sql("name like ").param(String.class, "S%");
        builder.sql("and");
        builder.sql("age > ").param(int.class, 20);

        String sql = String.format("select%n" + "id,%n" + "name,%n"
                + "salary%n" + "from Emp%n" + "where%n" + "name like ?%n"
                + "and%n" + "age > ?");
        assertEquals(sql, builder.getSql().getRawSql());

        Emp emp = builder.getEntitySingleResult(Emp.class);
        assertNull(emp);
    }

    public void testRmoveLast() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("aaa").sql("bbb");
        builder.removeLast();
        assertEquals("aaa", builder.getSql().getRawSql());
    }

    public void testSingleResult_Entity() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select");
        builder.sql("id").sql(",");
        builder.sql("name").sql(",");
        builder.sql("salary");
        builder.sql("from Emp");
        builder.sql("where");
        builder.sql("name like ").param(String.class, "S%");
        builder.sql("and");
        builder.sql("age > ").param(int.class, 20);
        Emp emp = builder.getEntitySingleResult(Emp.class);
        assertNull(emp);
    }

    public void testSingleResult_Map() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select");
        builder.sql("id").sql(",");
        builder.sql("name").sql(",");
        builder.sql("salary");
        builder.sql("from Emp");
        builder.sql("where");
        builder.sql("name like ").param(String.class, "S%");
        builder.sql("and");
        builder.sql("age > ").param(int.class, 20);
        Map<String, Object> emp = builder
                .getMapSingleResult(MapKeyNamingType.CAMEL_CASE);
        assertNull(emp);
    }

    public void testSingleResult_Domain() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        PhoneNumber phoneNumber = builder
                .getScalarSingleResult(PhoneNumber.class);
        assertNull(phoneNumber);
    }

    public void testSingleResult_Basic() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        String result = builder.getScalarSingleResult(String.class);
        assertNull(result);
    }

    public void testSingleResult_DomaIllegalArgumentException()
            throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select");
        builder.sql("aaa").sql(",");
        builder.sql("bbb");
        builder.sql("from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        try {
            builder.getScalarSingleResult(Class.class);
            fail();
        } catch (DomaIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testGetResultList_Entity() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select * from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        List<Emp> list = builder.getEntityResultList(Emp.class);
        assertNotNull(list);
    }

    public void testGetResultList_Map() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select * from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        List<Map<String, Object>> list = builder
                .getMapResultList(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(list);
    }

    public void testGetResultList_Domain() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        List<PhoneNumber> list = builder.getScalarResultList(PhoneNumber.class);
        assertNotNull(list);
    }

    public void testGetResultList_Basic() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        List<String> list = builder.getScalarResultList(String.class);
        assertNotNull(list);
    }

}
