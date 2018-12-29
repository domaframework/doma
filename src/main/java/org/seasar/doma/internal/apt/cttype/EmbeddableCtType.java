package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class EmbeddableCtType extends AbstractCtType {

  public EmbeddableCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
  }

  public static EmbeddableCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
    if (typeElement == null) {
      return null;
    }
    Embeddable embeddable = typeElement.getAnnotation(Embeddable.class);
    if (embeddable == null) {
      return null;
    }
    return new EmbeddableCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEmbeddableCtType(this, p);
  }
}
