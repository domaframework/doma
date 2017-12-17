package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.wrapper.Wrapper;

public class ScalarInParameter<BASIC, CONTAINER> implements InParameter<BASIC> {

    protected final Scalar<BASIC, CONTAINER> scalar;

    public ScalarInParameter(Scalar<BASIC, CONTAINER> scalar) {
        assertNotNull(scalar);
        this.scalar = scalar;
    }

    public ScalarInParameter(Supplier<Scalar<BASIC, CONTAINER>> supplier) {
        assertNotNull(supplier);
        scalar = supplier.get();
    }

    public ScalarInParameter(Supplier<Scalar<BASIC, CONTAINER>> supplier, CONTAINER value) {
        assertNotNull(supplier);
        scalar = supplier.get();
        scalar.set(value);
    }

    @Override
    public CONTAINER getValue() {
        return scalar.get();
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
        return scalar.getWrapper();
    }

    @Override
    public Optional<Class<?>> getHolderClass() {
        return scalar.getHolderClass();
    }

    @Override
    public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
            throws TH {
        return visitor.visitInParameter(this, p);
    }
}
