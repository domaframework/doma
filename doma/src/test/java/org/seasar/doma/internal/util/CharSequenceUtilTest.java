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
package org.seasar.doma.internal.util;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class CharSequenceUtilTest extends TestCase {

    public void testIsEmpty() throws Exception {
        assertTrue(CharSequenceUtil.isEmpty(null));
        assertTrue(CharSequenceUtil.isEmpty(""));
        assertFalse(CharSequenceUtil.isEmpty(" "));
        assertFalse(CharSequenceUtil.isEmpty(" \t\n\r "));
        assertFalse(CharSequenceUtil.isEmpty("a"));
        assertFalse(CharSequenceUtil.isEmpty(" a "));
    }

    public void testIsNotEmpty() throws Exception {
        assertFalse(CharSequenceUtil.isNotEmpty(null));
        assertFalse(CharSequenceUtil.isNotEmpty(""));
        assertTrue(CharSequenceUtil.isNotEmpty(" "));
        assertTrue(CharSequenceUtil.isNotEmpty(" \t\n\r "));
        assertTrue(CharSequenceUtil.isNotEmpty("a"));
        assertTrue(CharSequenceUtil.isNotEmpty(" a "));
    }

    public void testIsBlank() throws Exception {
        assertTrue(CharSequenceUtil.isBlank(null));
        assertTrue(CharSequenceUtil.isBlank(""));
        assertTrue(CharSequenceUtil.isBlank(" "));
        assertTrue(CharSequenceUtil.isBlank(" \t\n\r "));
        assertFalse(CharSequenceUtil.isBlank("a"));
        assertFalse(CharSequenceUtil.isBlank(" a "));
    }

    public void testIsNotBlank() throws Exception {
        assertFalse(CharSequenceUtil.isNotBlank(null));
        assertFalse(CharSequenceUtil.isNotBlank(""));
        assertFalse(CharSequenceUtil.isNotBlank(" "));
        assertFalse(CharSequenceUtil.isNotBlank(" \t\n\r "));
        assertTrue(CharSequenceUtil.isNotBlank("a"));
        assertTrue(CharSequenceUtil.isNotBlank(" a "));
    }

}
