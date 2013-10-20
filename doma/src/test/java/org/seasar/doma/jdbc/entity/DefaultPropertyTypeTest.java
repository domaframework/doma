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

/**
 * @author nakamura-to
 * 
 */
public class DefaultPropertyTypeTest extends TestCase {

    @SuppressWarnings("unused")
    private String hoge;

    public void testIsQuoteRequired_true() throws Exception {
        boolean isQuoteRequired = true;
        DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType = new DefaultPropertyType<>(
                DefaultPropertyTypeTest.class, String.class, String.class,
                org.seasar.doma.wrapper.StringWrapper::new, null, null, "hoge",
                "hoge", true, true, isQuoteRequired);
        assertEquals("hoge", propertyType.getColumnName());
        assertEquals("[hoge]", propertyType.getColumnName(s -> "[" + s + "]"));
    }

    public void testIsQuoteRequired_false() throws Exception {
        boolean isQuoteRequired = false;
        DefaultPropertyType<Object, DefaultPropertyTypeTest, String, Object> propertyType = new DefaultPropertyType<>(
                DefaultPropertyTypeTest.class, String.class, String.class,
                org.seasar.doma.wrapper.StringWrapper::new, null, null, "hoge",
                "hoge", true, true, isQuoteRequired);
        assertEquals("hoge", propertyType.getColumnName());
        assertEquals("hoge", propertyType.getColumnName(s -> "[" + s + "]"));
    }

}
