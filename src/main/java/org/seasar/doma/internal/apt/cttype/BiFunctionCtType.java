package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class BiFunctionCtType extends AbstractCtType {

    private final CtType firstArgCtType;

    private final CtType secondArgCtType;

    private final AnyCtType resultCtType;

    BiFunctionCtType(Context ctx, TypeMirror type, CtType firstArgCtType, CtType secondArgCtType,
            AnyCtType resultCtType) {
        super(ctx, type);
        this.firstArgCtType = firstArgCtType;
        this.secondArgCtType = secondArgCtType;
        this.resultCtType = resultCtType;
    }

    public CtType getFirstArgCtType() {
        return firstArgCtType;
    }

    public CtType getSecondArgCtType() {
        return secondArgCtType;
    }

    public AnyCtType getResultCtType() {
        return resultCtType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitBiFunctionCtType(this, p);
    }
}
