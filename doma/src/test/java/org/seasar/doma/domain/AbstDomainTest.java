package org.seasar.doma.domain;

import org.seasar.doma.domain.AbstractDomain;
import org.seasar.doma.domain.DomainVisitor;

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
            super(v);
        }

        @Override
        public <R, P, TH extends Throwable> R accept(
                DomainVisitor<R, P, TH> visitor, P p) throws TH {
            return visitor.visitUnknownDomain(this, p);
        }
    }
}
