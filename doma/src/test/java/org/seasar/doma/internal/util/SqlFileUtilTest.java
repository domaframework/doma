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

import org.seasar.doma.internal.jdbc.util.SqlFileUtil;

/**
 * @author taedium
 * 
 */
public class SqlFileUtilTest extends TestCase {

    public void testBuildPath() throws Exception {
        String path = SqlFileUtil.buildPath("aaa.bbb.Ccc", "ddd");
        assertEquals("META-INF/aaa/bbb/Ccc/ddd.sql", path);
    }

    public void testBuildPath_defaultPackage() throws Exception {
        String path = SqlFileUtil.buildPath("Ccc", "ddd");
        assertEquals("META-INF/Ccc/ddd.sql", path);
    }

}
