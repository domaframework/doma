package org.seasar.doma.internal.apt.cttype;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

/** @author taedium */
public class EnumWrapperCtType extends WrapperCtType {

  public EnumWrapperCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEnumWrapperCtType(this, p);
  }
}
