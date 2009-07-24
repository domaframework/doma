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
public class MethodAccessBeanWrapperTest extends TestCase {

    public void testGetBeanClass() throws Exception {
        Foo foo = new Foo();
        MethodAccessBeanWrapper bean = new MethodAccessBeanWrapper(foo);
        assertEquals(Foo.class, bean.getBeanClass());
    }

    public void testGetBeanPropertyWrappers() throws Exception {
        Foo foo = new Foo();
        MethodAccessBeanWrapper bean = new MethodAccessBeanWrapper(foo);
        assertEquals(4, bean.getBeanPropertyWrappers().size());
    }

    public void testGetBeanPropertyWrapper() throws Exception {
        Foo foo = new Foo();
        MethodAccessBeanWrapper bean = new MethodAccessBeanWrapper(foo);

        BeanPropertyWrapper property = bean.getBeanPropertyWrapper("aaa");
        assertNotNull(property);
        assertEquals("aaa", property.getName());
        assertEquals(String.class, property.getPropertyClass());

        property = bean.getBeanPropertyWrapper("bbb");
        assertNull(property);

        property = bean.getBeanPropertyWrapper("ccc");
        assertNotNull(property);
        assertEquals("ccc", property.getName());
        assertEquals(String.class, property.getPropertyClass());

        property = bean.getBeanPropertyWrapper("ddd");
        assertNotNull(property);
        assertEquals("ddd", property.getName());
        assertEquals(String.class, property.getPropertyClass());
    }

    public void testSetValue() throws Exception {
        Foo foo = new Foo();
        MethodAccessBeanWrapper bean = new MethodAccessBeanWrapper(foo);

        BeanPropertyWrapper property = bean.getBeanPropertyWrapper("aaa");
        property.setValue("hoge");
        assertEquals("hoge", foo.aaa);
    }

    public void testGetValue() throws Exception {
        Foo foo = new Foo();
        MethodAccessBeanWrapper bean = new MethodAccessBeanWrapper(foo);

        foo.setAaa("hoge");
        BeanPropertyWrapper property = bean.getBeanPropertyWrapper("aaa");
        assertEquals("hoge", property.getValue());
    }

    public static class Hoge {

        private String aaa;

        private String bbb;

        private String ccc;

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        @SuppressWarnings("unused")
        private String getBbb() {
            return bbb;
        }

        @SuppressWarnings("unused")
        private void setBbb(String bbb) {
            this.bbb = bbb;
        }

        public String getCcc() {
            return ccc;
        }

        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

    }

    public static class Foo extends Hoge {

        private String aaa;

        private String ddd;

        @Override
        public String getAaa() {
            return aaa;
        }

        @Override
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        public String getDdd() {
            return ddd;
        }

        public void setDdd(String ddd) {
            this.ddd = ddd;
        }

    }
}
