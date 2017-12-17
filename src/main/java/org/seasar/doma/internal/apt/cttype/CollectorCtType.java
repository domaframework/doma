package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class CollectorCtType extends AbstractCtType {

    private final CtType targetCtType;

    private final AnyCtType returnCtType;

    CollectorCtType(Context ctx, TypeMirror type, CtType targetCtType, AnyCtType returnCtType) {
        super(ctx, type);
        this.targetCtType = targetCtType;
        this.returnCtType = returnCtType;
    }

    public CtType getTargetCtType() {
        return targetCtType;
    }

    public AnyCtType getReturnCtType() {
        return returnCtType;
    }

    public boolean hasWildcardType() {
        return returnCtType.getType() != null
                && returnCtType.getType().getKind() == TypeKind.WILDCARD
                || targetCtType.getType() != null
                        && targetCtType.getType().getKind() == TypeKind.WILDCARD;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitCollectorCtType(this, p);
    }
}
