package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class OptionalLongCtType extends AbstractCtType {

  private final CtType elementCtType;

  OptionalLongCtType(Context ctx, TypeMirror typeMirror) {
    super(ctx, typeMirror);
    PrimitiveType primitiveType = ctx.getTypes().getPrimitiveType(TypeKind.LONG);
    this.elementCtType = ctx.getCtTypes().newBasicCtType(primitiveType);
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalLongCtType(this, p);
  }
}
