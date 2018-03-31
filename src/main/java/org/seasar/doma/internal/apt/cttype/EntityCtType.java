package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class EntityCtType extends AbstractCtType {

  private final boolean immutable;

  private final String descClassName;

  EntityCtType(Context ctx, TypeMirror type, boolean immutable) {
    super(ctx, type);
    assertNotNull(typeElement);
    this.immutable = immutable;
    var codeSpec = ctx.getCodeSpecs().newEntityDescCodeSpec(typeElement);
    this.descClassName = codeSpec.getQualifiedName();
  }

  public boolean isImmutable() {
    return immutable;
  }

  public boolean isAbstract() {
    return typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  public String getDescClassName() {
    return descClassName;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEntityCtType(this, p);
  }
}
