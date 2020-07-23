package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class BiFunctionCtType extends AbstractCtType {

  private final CtType firstArgCtType;

  private final CtType secondArgCtType;

  private final CtType resultCtType;

  BiFunctionCtType(
      Context ctx,
      TypeMirror type,
      CtType firstArgCtType,
      CtType secondArgCtType,
      CtType resultCtType) {
    super(ctx, type);
    assertNotNull(firstArgCtType, secondArgCtType, resultCtType);
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

  public CtType getResultCtType() {
    return resultCtType;
  }

  public boolean isRaw() {
    return firstArgCtType.isNone() || secondArgCtType.isNone() || resultCtType.isNone();
  }

  public boolean hasWildcard() {
    return firstArgCtType.isWildcard() || secondArgCtType.isWildcard() || resultCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBiFunctionCtType(this, p);
  }
}
