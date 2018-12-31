package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class BasicCtType extends AbstractCtType {

  protected WrapperCtType wrapperCtType;

  public BasicCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public WrapperCtType getWrapperCtType() {
    return wrapperCtType;
  }

  public String getDefaultValue() {
    switch (typeMirror.getKind()) {
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

  public static BasicCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    BasicCtType basicCtType = new BasicCtType(type, ctx);
    WrapperCtType wrapperCtType = WrapperCtType.newInstance(basicCtType, ctx);
    if (wrapperCtType == null) {
      return null;
    }
    basicCtType.wrapperCtType = wrapperCtType;
    return basicCtType;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBasicCtType(this, p);
  }
}
