package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class FunctionCtType extends AbstractCtType {

  private final CtType targetCtType;

  private final CtType returnCtType;

  FunctionCtType(Context ctx, TypeMirror type, CtType targetCtType, CtType returnCtType) {
    super(ctx, type);
    assertNotNull(targetCtType, returnCtType);
    this.targetCtType = targetCtType;
    this.returnCtType = returnCtType;
  }

  public CtType getTargetCtType() {
    return targetCtType;
  }

  public CtType getReturnCtType() {
    return returnCtType;
  }

  public boolean isRaw() {
    return returnCtType.isNone() || targetCtType.isNone();
  }

  public boolean hasWildcard() {
    return returnCtType.isWildcard() || targetCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitFunctionCtType(this, p);
  }
}
