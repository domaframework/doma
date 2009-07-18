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

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class CopyOptionsTest extends TestCase {

    private CopyOptions options = new CopyOptions();

    public void testIncludeNull() throws Exception {
        assertFalse(options.isTargetValue(null));
        options.includeNull();
        assertTrue(options.isTargetValue(null));
    }

    public void testIncludeEmptyString() throws Exception {
        assertFalse(options.isTargetValue(""));
        options.includeEmptyString();
        assertTrue(options.isTargetValue(""));
    }

    public void testExcludeWhitespace() throws Exception {
        assertTrue(options.isTargetValue(" "));
        options.excludeWhitespace();
        assertFalse(options.isTargetValue(" "));
    }

    public void testInclude() throws Exception {
        options.include("aaa", "bbb");
        assertTrue(options.isTargetProperty("aaa"));
        assertTrue(options.isTargetProperty("bbb"));
        assertFalse(options.isTargetProperty("ccc"));
    }

    public void testExclude() throws Exception {
        options.include("aaa", "bbb");
        options.exclude("aaa", "ccc");
        assertFalse(options.isTargetProperty("aaa"));
        assertTrue(options.isTargetProperty("bbb"));
        assertFalse(options.isTargetProperty("ccc"));
        assertFalse(options.isTargetProperty("ddd"));
    }
}
