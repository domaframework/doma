package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class OptionalDoubleCtType extends AbstractCtType {

  private final CtType elementCtType;

  OptionalDoubleCtType(Context ctx, TypeMirror typeMirror) {
    super(ctx, typeMirror);
    PrimitiveType primitiveType = ctx.getTypes().getPrimitiveType(TypeKind.DOUBLE);
    this.elementCtType = ctx.getCtTypes().newBasicCtType(primitiveType);
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalDoubleCtType(this, p);
  }
}
