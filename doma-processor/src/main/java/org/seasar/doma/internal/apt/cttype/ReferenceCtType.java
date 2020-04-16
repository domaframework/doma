package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class ReferenceCtType extends AbstractCtType {

  private final CtType referentCtType;

  ReferenceCtType(Context ctx, TypeMirror type, CtType referentCtType) {
    super(ctx, type);
    this.referentCtType = referentCtType;
  }

  public CtType getReferentCtType() {
    return referentCtType;
  }

  public TypeMirror getReferentTypeMirror() {
    return referentCtType.getType();
  }

  public boolean isRaw() {
    return referentCtType.isNone();
  }

  public boolean hasWildcard() {
    return referentCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitReferenceCtType(this, p);
  }
}
