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
public class LongConverterTest extends TestCase {

    private LongConverter converter = new LongConverter();

    public void testNull() throws Exception {
        Long value = converter.convert(null, null);
        assertNull(value);
    }

    public void testInt() throws Exception {
        Long value = converter.convert(100, null);
        assertEquals(new Long(100), value);
    }

    public void testString() throws Exception {
        Long value = converter.convert("100", null);
        assertEquals(new Long(100), value);
    }

    public void testStringWithPattern() throws Exception {
        Long value = converter.convert("(100)", "(0)");
        assertEquals(new Long(100), value);
    }
}
