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
package org.seasar.doma.domain;

import org.seasar.doma.domain.DomainException;
import org.seasar.doma.domain.BuiltinStringDomain;

import junit.framework.TestCase;

public class BuiltinStringDomainTest extends TestCase {

    public void testGet() throws Exception {
        BuiltinStringDomain domain = new BuiltinStringDomain("aaa");
        assertEquals("aaa", domain.get());
    }

    public void testSet_value() throws Exception {
        BuiltinStringDomain domain = new BuiltinStringDomain("aaa");
        domain.set("bbb");
        assertEquals("bbb", domain.get());
    }

    public void testSet_domain() throws Exception {
        BuiltinStringDomain domain = new BuiltinStringDomain("aaa");
        domain.setDomain(new BuiltinStringDomain("bbb"));
        assertEquals("bbb", domain.get());
    }

    public void testIsEmpty() throws Exception {
        assertTrue(new BuiltinStringDomain("").isEmpty());
        try {
            new BuiltinStringDomain().isEmpty();
        } catch (DomainException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    public void testEquals() throws Exception {
        BuiltinStringDomain domain = new BuiltinStringDomain("aaa");
        BuiltinStringDomain domain2 = new BuiltinStringDomain("aaa");
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set("aaa");
        assertFalse(domain.equals(domain2));
    }

}
