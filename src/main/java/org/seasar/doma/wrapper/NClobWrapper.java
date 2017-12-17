package org.seasar.doma.wrapper;

import java.sql.NClob;

import org.seasar.doma.DomaNullPointerException;

/**
 * A wrapper for the {@link NClob} class.
 */
public class NClobWrapper extends AbstractWrapper<NClob> {

    public NClobWrapper() {
        super(NClob.class);
    }

    public NClobWrapper(NClob value) {
        super(NClob.class, value);
    }

    @Override
    protected NClob doGetCopy() {
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
        return visitor.visitNClobWrapper(this, p, q);
    }
}
