package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Context;

public class EntityCtType extends AbstractCtType {

  private final boolean immutable;

  private final ClassName entityDescClassName;

  EntityCtType(Context ctx, TypeMirror type, TypeElement typeElement, boolean immutable) {
    super(ctx, type);
    this.immutable = immutable;
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    this.entityDescClassName = ClassNames.newEntityDescClassName(binaryName);
  }

  public boolean isImmutable() {
    return immutable;
  }

  public boolean isAbstract() {
    return typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  public String entityDescSingletonCode() {
    return entityDescClassName + ".getSingletonInternal()";
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEntityCtType(this, p);
  }
}
