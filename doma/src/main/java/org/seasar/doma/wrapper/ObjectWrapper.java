package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

public class ObjectWrapper extends AbstractWrapper<Object> {

    public ObjectWrapper() {
    }

    public ObjectWrapper(Object value) {
        set(value);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (ObjectWrapperVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            ObjectWrapperVisitor<R, P, TH> v = ObjectWrapperVisitor.class
                    .cast(visitor);
            return v.visitObjectWrapper(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
