package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class OptionalLongCtType extends AbstractCtType {

  private final BasicCtType elementCtType;

  OptionalLongCtType(Context ctx, TypeMirror typeMirror, BasicCtType elementCtType) {
    super(ctx, typeMirror);
    assertNotNull(elementCtType);
    this.elementCtType = elementCtType;
  }

  public BasicCtType getElementCtType() {
    return elementCtType;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalLongCtType(this, p);
  }
}
