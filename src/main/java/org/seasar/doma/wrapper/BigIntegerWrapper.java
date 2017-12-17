package org.seasar.doma.wrapper;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.seasar.doma.DomaNullPointerException;

/**
 * A wrapper for the {@link BigInteger} class.
 */
public class BigIntegerWrapper extends AbstractWrapper<BigInteger>
        implements NumberWrapper<BigInteger> {

    public BigIntegerWrapper() {
        super(BigInteger.class);
    }

    public BigIntegerWrapper(BigInteger value) {
        super(BigInteger.class, value);
    }

    @Override
    public void set(Number v) {
        if (v instanceof BigInteger) {
            super.set((BigInteger) v);
        } else if (v instanceof BigDecimal) {
            super.set(((BigDecimal) v).toBigInteger());
        } else {
            super.set(BigInteger.valueOf(v.longValue()));
        }
    }

    @Override
    public void increment() {
        BigInteger value = doGet();
        if (value != null) {
            doSet(value.add(BigInteger.ONE));
        }
    }

    @Override
    public void decrement() {
        BigInteger value = doGet();
        if (value != null) {
            doSet(value.subtract(BigInteger.ONE));
        }
    }

    @Override
    public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitBigIntegerWrapper(this, p, q);
    }
}
