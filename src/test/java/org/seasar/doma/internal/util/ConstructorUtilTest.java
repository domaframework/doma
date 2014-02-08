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

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ConstructorUtilTest extends TestCase {

    public void testToSignature() throws Exception {
        Constructor<String> constructor = String.class.getConstructor(
                char[].class, int.class, int.class);
        assertEquals("java.lang.String(char[], int, int)",
                ConstructorUtil.createSignature(constructor));
    }

    public void testGetConstructor() throws Exception {
        Constructor<String> constructor = String.class.getConstructor(
                char[].class, int.class, int.class);
        String result = ConstructorUtil.newInstance(constructor, new char[] {
                'a', 'b', 'c', 'd', 'e' }, 1, 3);
        assertEquals("bcd", result);
    }

    public class Hoge {
        public Hoge(String name) {
        }
    }
}
