package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class EmbeddableCtType extends AbstractCtType {

  EmbeddableCtType(Context ctx, TypeMirror type) {
    super(ctx, type);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEmbeddableCtType(this, p);
  }
}
