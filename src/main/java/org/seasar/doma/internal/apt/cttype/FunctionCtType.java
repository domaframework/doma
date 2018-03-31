package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class FunctionCtType extends AbstractCtType {

  private final CtType targetCtType;

  private final AnyCtType returnCtType;

  FunctionCtType(Context ctx, TypeMirror type, CtType targetCtType, AnyCtType returnCtType) {
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

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitFunctionCtType(this, p);
  }
}
