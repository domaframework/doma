package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public class BasicScalar<BASIC> implements Scalar<BASIC, BASIC> {

    protected final Wrapper<BASIC> wrapper;

    protected final boolean primitive;

    public BasicScalar(Wrapper<BASIC> wrapper, boolean primitive) {
        AssertionUtil.assertNotNull(wrapper);
        this.wrapper = wrapper;
        this.primitive = primitive;
    }

    public BasicScalar(Supplier<Wrapper<BASIC>> supplier, boolean primitive) {
        AssertionUtil.assertNotNull(supplier);
        this.wrapper = supplier.get();
        AssertionUtil.assertNotNull(this.wrapper);
        this.primitive = primitive;
    }

    @Override
    public Optional<Class<?>> getHolderClass() {
        return Optional.empty();
    }

    @Override
    public BASIC cast(Object value) {
        return wrapper.getBasicClass().cast(value);
    }

    @Override
    public BASIC get() {
        BASIC value = wrapper.get();
        if (value == null) {
            return getDefaultInternal();
        }
        return value;
    }

    @Override
    public BASIC getDefault() {
        return getDefaultInternal();
    }

    protected BASIC getDefaultInternal() {
        if (primitive) {
            return wrapper.getDefault();
        }
        return null;
    }

    @Override
    public void set(BASIC value) {
        if (value == null && primitive) {
            wrapper.set(wrapper.getDefault());
        } else {
            wrapper.set(value);
        }
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
        return wrapper;
    }

}
