package org.seasar.doma.wrapper;

import java.sql.Blob;

import org.seasar.doma.DomaNullPointerException;

/**
 * A wrapper for the {@link Blob} class.
 */
public class BlobWrapper extends AbstractWrapper<Blob> {

    public BlobWrapper() {
        super(Blob.class);
    }

    public BlobWrapper(Blob value) {
        super(Blob.class, value);
    }

    @Override
    protected Blob doGetCopy() {
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
        return visitor.visitBlobWrapper(this, p, q);
    }
}
