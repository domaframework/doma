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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ClassUtilTest extends TestCase {

    public void testGetConstructor() throws Exception {
        Constructor<String> constructor = ClassUtil.getConstructor(
                String.class, char[].class, int.class, int.class);
        assertNotNull(constructor);
    }

    public void testGetDeclaredField() throws Exception {
        Field field = ClassUtil.getDeclaredField(TestCase.class, "fName");
        assertNotNull(field);
    }
}
