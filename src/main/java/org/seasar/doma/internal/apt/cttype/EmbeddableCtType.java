package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.TypeName;

public class EmbeddableCtType extends AbstractCtType {

  private final TypeName embeddableDescTypeName;

  EmbeddableCtType(Context ctx, TypeMirror type, TypeElement typeElement) {
    super(ctx, type);
    this.embeddableDescTypeName = ctx.getTypeNames().newEmbeddableDescTypeName(typeElement, type);
  }

  public String embeddableTypeSingletonCode() {
    ClassName className = embeddableDescTypeName.getClassName();
    return className + ".getSingletonInternal()";
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEmbeddableCtType(this, p);
  }
}
