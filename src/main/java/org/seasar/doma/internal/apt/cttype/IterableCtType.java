package org.seasar.doma.internal.apt.cttype;

import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class IterableCtType extends AbstractCtType {

    private final CtType elementCtType;

    IterableCtType(Context ctx, TypeMirror type, CtType elementCtType) {
        super(ctx, type);
        this.elementCtType = elementCtType;
    }

    public CtType getElementCtType() {
        return elementCtType;
    }

    public boolean isList() {
        return ctx.getTypes().isSameType(type, List.class);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitIterableCtType(this, p);
    }
}
