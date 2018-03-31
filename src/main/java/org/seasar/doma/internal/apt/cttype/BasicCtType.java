package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class BasicCtType extends ScalarCtType {

  private final String wrapperTypeName;

  BasicCtType(Context ctx, TypeMirror type, TypeMirror wrapperType) {
    super(ctx, type);
    this.wrapperTypeName = ctx.getTypes().getTypeName(wrapperType);
  }

  public String getWrapperTypeName() {
    return wrapperTypeName;
  }

  public boolean isEnum() {
    return typeElement != null && typeElement.getKind() == ElementKind.ENUM;
  }

  public boolean isPrimitive() {
    return type.getKind().isPrimitive();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBasicCtType(this, p);
  }
}
