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
package org.seasar.doma.internal;

import junit.framework.TestCase;

/**
 * @author nakamura-to
 *
 */
public class ConventionsTest extends TestCase {

    public void testNormalizeBinaryName() throws Exception {
        assertEquals("Ccc", Conventions.normalizeBinaryName("Ccc"));
        assertEquals("aaa.bbb.Ccc",
                Conventions.normalizeBinaryName("aaa.bbb.Ccc"));
        assertEquals("aaa.bbb.Ccc__Ddd__Eee",
                Conventions.normalizeBinaryName("aaa.bbb.Ccc$Ddd$Eee"));
    }

    public void testToFullMetaName() throws Exception {
        assertEquals("_Ccc", Conventions.toFullDescName("Ccc"));
        assertEquals("aaa.bbb._Ccc", Conventions.toFullDescName("aaa.bbb.Ccc"));
        assertEquals("aaa.bbb._Ccc__Ddd__Eee",
                Conventions.toFullDescName("aaa.bbb.Ccc$Ddd$Eee"));
    }

    public void testToSimpleMetaName() throws Exception {
        assertEquals("_Ccc", Conventions.toSimpleDescName("Ccc"));
        assertEquals("_Ccc", Conventions.toSimpleDescName("aaa.bbb.Ccc"));
        assertEquals("_Ccc__Ddd__Eee",
                Conventions.toSimpleDescName("aaa.bbb.Ccc$Ddd$Eee"));
    }

}
