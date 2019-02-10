package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Context;

public class EmbeddableCtType extends AbstractCtType {

  private final ClassName embeddableDescClassName;

  EmbeddableCtType(Context ctx, TypeMirror type, TypeElement typeElement) {
    super(ctx, type);
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    this.embeddableDescClassName = ClassNames.newEmbeddableDescClassName(binaryName);
  }

  public String embeddableDescSingletonCode() {
    return embeddableDescClassName + ".getSingletonInternal()";
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEmbeddableCtType(this, p);
  }
}
