package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class BasicCtType extends AbstractCtType {

  private final TypeMirror boxedType;

  private final WrapperCtType wrapperCtType;

  BasicCtType(Context ctx, TypeMirror type, WrapperCtType wrapperCtType) {
    super(ctx, type);
    this.boxedType = ctx.getTypes().boxIfPrimitive(type);
    this.wrapperCtType = wrapperCtType;
  }

  public TypeMirror getBoxedType() {
    return boxedType;
  }

  public WrapperCtType getWrapperCtType() {
    return wrapperCtType;
  }

  public String getDefaultValue() {
    switch (type.getKind()) {
      case BOOLEAN:
        return String.valueOf(false);
      case BYTE:
      case SHORT:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case CHAR:
        return "0";
      default:
        return "null";
    }
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBasicCtType(this, p);
  }
}
