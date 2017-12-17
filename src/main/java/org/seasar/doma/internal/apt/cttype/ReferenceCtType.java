package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class ReferenceCtType extends AbstractCtType {

    private final CtType referentCtType;

    ReferenceCtType(Context ctx, TypeMirror type, CtType referentType) {
        super(ctx, type);
        this.referentCtType = referentType;
    }

    public CtType getReferentCtType() {
        return referentCtType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitReferenceCtType(this, p);
    }
}
