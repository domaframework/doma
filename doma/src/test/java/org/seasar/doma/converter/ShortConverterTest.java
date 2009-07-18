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
public class ShortConverterTest extends TestCase {

    private ShortConverter converter = new ShortConverter();

    public void testNull() throws Exception {
        Short value = converter.convert(null, null);
        assertNull(value);
    }

    public void testLong() throws Exception {
        Short value = converter.convert(100L, null);
        assertEquals(new Short("100"), value);
    }

    public void testString() throws Exception {
        Short value = converter.convert("100", null);
        assertEquals(new Short("100"), value);
    }

    public void testStringWithPattern() throws Exception {
        Short value = converter.convert("(100)", "(0)");
        assertEquals(new Short("100"), value);
    }
}
