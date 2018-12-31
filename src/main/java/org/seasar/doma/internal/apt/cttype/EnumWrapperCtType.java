package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class EnumWrapperCtType extends WrapperCtType {

  public EnumWrapperCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEnumWrapperCtType(this, p);
  }
}
