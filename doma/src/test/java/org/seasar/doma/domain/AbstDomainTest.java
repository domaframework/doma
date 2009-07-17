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

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class AbstDomainTest extends TestCase {

    public void testIsNull() throws Exception {
        MyDomain domain = new MyDomain();
        assertTrue(domain.isNull());
    }

    public void testIsNotNull() throws Exception {
        MyDomain domain = new MyDomain("");
        assertTrue(domain.isNotNull());
    }

    public void testToString() throws Exception {
        MyDomain domain = new MyDomain("aaa");
        assertEquals("aaa", domain.toString());

        MyDomain domain2 = new MyDomain();
        assertNull(domain2.toString());
    }

    public void testIsChanged() throws Exception {
        MyDomain domain = new MyDomain();
        assertFalse(domain.isChanged());
        domain.set("aaa");
        assertTrue(domain.isChanged());
    }

    private static class MyDomain extends AbstractDomain<String, MyDomain> {

        public MyDomain() {
            this(null);
        }

        public MyDomain(String v) {
            super(String.class, v);
        }

        @Override
        public <R, P, TH extends Throwable> R accept(
                DomainVisitor<R, P, TH> visitor, P p) throws TH {
            return visitor.visitUnknownDomain(this, p);
        }
    }
}
