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
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

import example.domain.PhoneNumber;
import example.entity.Emp;

/**
 * @author taedium
 * 
 */
public class SelectBuilderTest extends TestCase {

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
        Emp emp = builder.getSingleResult(Emp.class);
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
                .getSingleResult(MapKeyNamingType.CAMEL_CASE);
        assertNull(emp);
    }

    public void testSingleResult_Domain() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        PhoneNumber phoneNumber = builder.getSingleResult(PhoneNumber.class);
        assertNull(phoneNumber);
    }

    public void testSingleResult_Basic() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        String result = builder.getSingleResult(String.class);
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
            builder.getSingleResult(Object.class);
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
        List<Emp> list = builder.getResultList(Emp.class);
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
                .getResultList(MapKeyNamingType.CAMEL_CASE);
        assertNotNull(list);
    }

    public void testGetResultList_Domain() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        List<PhoneNumber> list = builder.getResultList(PhoneNumber.class);
        assertNotNull(list);
    }

    public void testGetResultList_Basic() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa = ").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        List<String> list = builder.getResultList(String.class);
        assertNotNull(list);
    }

    public void testIterate_Entity() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select * from Emp");
        builder.sql("where");
        builder.sql("aaa =").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        builder.iterate(Emp.class, new IterationCallback<Void, Emp>() {

            @Override
            public Void iterate(Emp target, IterationContext context) {
                return null;
            }

        });
    }

    public void testIterate_Map() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select * from Emp");
        builder.sql("where");
        builder.sql("aaa =").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        builder.iterate(MapKeyNamingType.CAMEL_CASE,
                new IterationCallback<Void, Map<String, Object>>() {

                    @Override
                    public Void iterate(Map<String, Object> target,
                            IterationContext context) {
                        return null;
                    }

                });
    }

    public void testIterate_Domain() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa =").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        builder.iterate(PhoneNumber.class,
                new IterationCallback<Void, PhoneNumber>() {

                    @Override
                    public Void iterate(PhoneNumber target,
                            IterationContext context) {
                        return null;
                    }

                });
    }

    public void testIterate_Basic() throws Exception {
        SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
        builder.sql("select ccc from Emp");
        builder.sql("where");
        builder.sql("aaa =").param(String.class, "aaa");
        builder.sql("and");
        builder.sql("bbb = ").param(int.class, 100);
        builder.iterate(String.class, new IterationCallback<Void, String>() {

            @Override
            public Void iterate(String target, IterationContext context) {
                return null;
            }

        });
    }
}
