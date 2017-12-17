package org.seasar.doma.wrapper;

import java.sql.SQLXML;

import org.seasar.doma.DomaNullPointerException;

/**
 * A wrapper for the {@link SQLXML} class.
 */
public class SQLXMLWrapper extends AbstractWrapper<SQLXML> {

    public SQLXMLWrapper() {
        super(SQLXML.class);
    }

    public SQLXMLWrapper(SQLXML value) {
        super(SQLXML.class, value);
    }

    @Override
    protected SQLXML doGetCopy() {
        return null;
    }

    @Override
    protected boolean doHasEqualValue(Object otherValue) {
        return false;
    }

    @Override
    public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitSQLXMLWrapper(this, p, q);
    }

}
