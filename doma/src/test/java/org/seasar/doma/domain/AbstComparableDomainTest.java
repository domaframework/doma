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

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public class AbstComparableDomainTest extends TestCase {

    public void testEq_value() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertFalse(d.eq("4"));
        assertTrue(d.eq("5"));
        assertFalse(d.eq("6"));
    }

    public void testEq_domain() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertFalse(d.eq(new AbstComparableDomain("4")));
        assertTrue(d.eq(new AbstComparableDomain("5")));
        assertFalse(d.eq(new AbstComparableDomain("6")));
    }

    public void testGe_value() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertTrue(d.ge("4"));
        assertTrue(d.ge("5"));
        assertFalse(d.ge("6"));
    }

    public void testGe_domain() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertTrue(d.ge(new AbstComparableDomain("4")));
        assertTrue(d.ge(new AbstComparableDomain("5")));
        assertFalse(d.ge(new AbstComparableDomain("6")));
    }

    public void testGt_value() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertTrue(d.gt("4"));
        assertFalse(d.gt("5"));
        assertFalse(d.gt("6"));
    }

    public void testGt_domain() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertTrue(d.gt(new AbstComparableDomain("4")));
        assertFalse(d.gt(new AbstComparableDomain("5")));
        assertFalse(d.gt(new AbstComparableDomain("6")));
    }

    public void testLe_value() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertFalse(d.le("4"));
        assertTrue(d.le("5"));
        assertTrue(d.le("6"));
    }

    public void testLe_domain() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertFalse(d.le(new AbstComparableDomain("4")));
        assertTrue(d.le(new AbstComparableDomain("5")));
        assertTrue(d.le(new AbstComparableDomain("6")));
    }

    public void testLt_value() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertFalse(d.lt("4"));
        assertFalse(d.lt("5"));
        assertTrue(d.lt("6"));
    }

    public void testLt_domain() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertFalse(d.lt(new AbstComparableDomain("4")));
        assertFalse(d.lt(new AbstComparableDomain("5")));
        assertTrue(d.lt(new AbstComparableDomain("6")));
    }

    public void test_nullValue() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        assertFalse(d.eq(String.class.cast(null)));
        try {
            d.lt(String.class.cast(null));
            fail();
        } catch (DomainIncomparableException expected) {
        }
    }

    public void test_nullDomain() throws Exception {
        AbstComparableDomain d = new AbstComparableDomain("5");
        try {
            d.eq(AbstComparableDomain.class.cast(null));
            fail();
        } catch (DomaIllegalArgumentException expected) {
        }
        try {
            d.lt(AbstComparableDomain.class.cast(null));
            fail();
        } catch (DomaIllegalArgumentException expected) {
        }
    }

    private static class AbstComparableDomain extends
            AbstractComparableDomain<String, AbstComparableDomain> {

        public AbstComparableDomain(String value) {
            super(value);
        }

        @Override
        public <R, P, TH extends Throwable> R accept(
                DomainVisitor<R, P, TH> visitor, P p) throws TH {
            return visitor.visitUnknownDomain(this, p);
        }

    }
}
