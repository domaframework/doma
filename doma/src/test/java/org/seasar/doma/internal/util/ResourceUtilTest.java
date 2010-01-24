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

import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ResourceUtilTest extends TestCase {

    public void testGetResourceAsStream() throws Exception {
        String path = getClass().getName().replace(".", "/") + ".txt";
        InputStream inputStream = ResourceUtil.getResourceAsStream(path);
        assertNotNull(inputStream);
    }

    public void testGetResourceAsStream_nonexistentPath() throws Exception {
        InputStream inputStream = ResourceUtil
                .getResourceAsStream("nonexistentPath");
        assertNull(inputStream);
    }

    public void testGetResourceAsString() throws Exception {
        String path = getClass().getName().replace(".", "/") + ".txt";
        String value = ResourceUtil.getResourceAsString(path);
        assertEquals("aaa", value);
    }
}
