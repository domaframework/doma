package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class BasicCtType extends AbstractCtType {

  protected WrapperCtType wrapperCtType;

  public BasicCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
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

  public static BasicCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    BasicCtType basicCtType = new BasicCtType(type, env);
    WrapperCtType wrapperCtType = WrapperCtType.newInstance(basicCtType, env);
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
