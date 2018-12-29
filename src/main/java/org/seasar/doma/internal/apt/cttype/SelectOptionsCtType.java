package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.SelectOptions;

public class SelectOptionsCtType extends AbstractCtType {

  public SelectOptionsCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  public static SelectOptionsCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isAssignable(type, SelectOptions.class, env)) {
      return null;
    }
    return new SelectOptionsCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitSelectOptionsCtType(this, p);
  }
}
