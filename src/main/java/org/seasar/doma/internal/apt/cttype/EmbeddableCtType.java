package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.generator.Code;

public class EmbeddableCtType extends AbstractCtType {

  private final ClassName descClassName;

  EmbeddableCtType(Context ctx, TypeMirror type, ClassName descClassName) {
    super(ctx, type);
    assertNotNull(descClassName);
    this.descClassName = descClassName;
  }

  public Code getDescCode() {
    return new Code(p -> p.print("%1$s.getSingletonInternal()", descClassName));
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEmbeddableCtType(this, p);
  }
}
