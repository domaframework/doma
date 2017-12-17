package org.seasar.doma.wrapper;

import java.time.LocalDateTime;

import org.seasar.doma.DomaNullPointerException;

/**
 * A wrapper for the {@link LocalDateTime} class.
 */
public class LocalDateTimeWrapper extends AbstractWrapper<LocalDateTime> {

    public LocalDateTimeWrapper() {
        super(LocalDateTime.class);
    }

    public LocalDateTimeWrapper(LocalDateTime value) {
        super(LocalDateTime.class, value);
    }

    @Override
    public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitLocalDateTimeWrapper(this, p, q);
    }

}
