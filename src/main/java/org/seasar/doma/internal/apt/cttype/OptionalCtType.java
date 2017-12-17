package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class OptionalCtType extends AbstractCtType {

    private final CtType elementCtType;

    OptionalCtType(Context ctx, TypeMirror optionalType, CtType elementCtType) {
        super(ctx, optionalType);
        this.elementCtType = elementCtType;
    }

    public CtType getElementCtType() {
        return elementCtType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitOptionalCtType(this, p);
    }

}
