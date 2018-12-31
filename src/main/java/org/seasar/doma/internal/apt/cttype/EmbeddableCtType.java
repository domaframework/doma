package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.Context;

public class EmbeddableCtType extends AbstractCtType {

  public EmbeddableCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public static EmbeddableCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    Embeddable embeddable = typeElement.getAnnotation(Embeddable.class);
    if (embeddable == null) {
      return null;
    }
    return new EmbeddableCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEmbeddableCtType(this, p);
  }
}
