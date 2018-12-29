package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class AnyCtType extends AbstractCtType {

  public AnyCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  public static AnyCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    return new AnyCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitAnyCtType(this, p);
  }
}
