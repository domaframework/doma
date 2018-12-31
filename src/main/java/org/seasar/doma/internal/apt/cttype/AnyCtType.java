package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class AnyCtType extends AbstractCtType {

  public AnyCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public static AnyCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    return new AnyCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitAnyCtType(this, p);
  }
}
