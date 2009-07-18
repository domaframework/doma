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
package org.seasar.doma.bean;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class FieldAccessBeanTest extends TestCase {

    public void testGetBeanClass() throws Exception {
        Foo foo = new Foo();
        FieldAccessBean bean = new FieldAccessBean(foo);
        assertEquals(Foo.class, bean.getBeanClass());
    }

    public void testGetBeanProperties() throws Exception {
        Foo foo = new Foo();
        FieldAccessBean bean = new FieldAccessBean(foo);
        assertEquals(3, bean.getBeanProperties().size());
    }

    public void testGetBeanProperty() throws Exception {
        Foo foo = new Foo();
        FieldAccessBean bean = new FieldAccessBean(foo);

        BeanProperty property = bean.getBeanProperty("aaa");
        assertNotNull(property);
        assertEquals("aaa", property.getName());
        assertEquals(int.class, property.getPropertyClass());

        property = bean.getBeanProperty("bbb");
        assertNull(property);

        property = bean.getBeanProperty("ccc");
        assertNotNull(property);
        assertEquals("ccc", property.getName());
        assertEquals(String.class, property.getPropertyClass());

        property = bean.getBeanProperty("ddd");
        assertNotNull(property);
        assertEquals("ddd", property.getName());
        assertEquals(String.class, property.getPropertyClass());
    }

    public void testSetValue() throws Exception {
        Foo foo = new Foo();
        FieldAccessBean bean = new FieldAccessBean(foo);

        BeanProperty property = bean.getBeanProperty("aaa");
        property.setValue(100);
        assertEquals(100, foo.aaa);
    }

    public void testGetValue() throws Exception {
        Foo foo = new Foo();
        FieldAccessBean bean = new FieldAccessBean(foo);

        foo.aaa = 100;
        BeanProperty property = bean.getBeanProperty("aaa");
        assertEquals(100, property.getValue());
    }

    public static class Hoge {

        public String aaa;

        @SuppressWarnings("unused")
        private String bbb;

        public String ccc;
    }

    public static class Foo extends Hoge {

        @SuppressWarnings("hiding")
        public int aaa;

        public String ddd;
    }
}
