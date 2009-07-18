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
package org.seasar.doma.converter;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BooleanConverterTest extends TestCase {

    private BooleanConverter converter = new BooleanConverter();

    public void testNull() throws Exception {
        Boolean value = converter.convert(null, null);
        assertNull(value);
    }

    public void testInt() throws Exception {
        Boolean value = converter.convert(1, null);
        assertTrue(value);
        value = converter.convert(2, null);
        assertFalse(value);
    }

    public void testString() throws Exception {
        Boolean value = converter.convert("true", null);
        assertTrue(value);
        value = converter.convert("hoge", null);
        assertFalse(value);
    }
}
