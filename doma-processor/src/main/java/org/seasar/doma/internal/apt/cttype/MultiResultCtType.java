package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class MultiResultCtType extends AbstractCtType {
  private final CtType elementCtType;

  MultiResultCtType(Context ctx, TypeMirror type, CtType elementCtType) {
    super(ctx, type);
    this.elementCtType = elementCtType;
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public boolean isRaw() {
    return elementCtType.isNone();
  }

  public boolean hasWildcard() {
    return elementCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitMultiResultCtType(this, p);
  }
}
