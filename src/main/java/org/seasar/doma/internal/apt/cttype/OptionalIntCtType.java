package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class OptionalIntCtType extends ScalarCtType {

    private final CtType elementCtType;

    OptionalIntCtType(Context ctx, TypeMirror typeMirro, CtType elementCtType) {
        super(ctx, typeMirro);
        this.elementCtType = elementCtType;
    }

    public CtType getElementCtType() {
        return elementCtType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitOptionalIntCtType(this, p);
    }

}
