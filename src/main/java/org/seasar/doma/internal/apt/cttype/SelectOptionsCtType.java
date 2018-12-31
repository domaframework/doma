package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.jdbc.SelectOptions;

public class SelectOptionsCtType extends AbstractCtType {

  public SelectOptionsCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public static SelectOptionsCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isAssignable(type, SelectOptions.class)) {
      return null;
    }
    return new SelectOptionsCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitSelectOptionsCtType(this, p);
  }
}
