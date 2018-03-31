package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

/** @author taedium */
public class MapCtType extends AbstractCtType {

  MapCtType(Context ctx, TypeMirror type) {
    super(ctx, type);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitMapCtType(this, p);
  }
}
