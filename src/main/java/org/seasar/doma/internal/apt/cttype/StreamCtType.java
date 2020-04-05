package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class StreamCtType extends AbstractCtType {

  private final CtType elementCtType;

  StreamCtType(Context ctx, TypeMirror type, CtType elementCtType) {
    super(ctx, type);
    assertNotNull(elementCtType);
    this.elementCtType = elementCtType;
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitStreamCtType(this, p);
  }
}
