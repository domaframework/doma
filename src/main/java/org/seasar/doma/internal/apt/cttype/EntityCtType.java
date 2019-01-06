package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class EntityCtType extends AbstractCtType {

  private final boolean immutable;

  EntityCtType(Context ctx, TypeMirror type, boolean immutable) {
    super(ctx, type);
    this.immutable = immutable;
  }

  public boolean isImmutable() {
    return immutable;
  }

  public boolean isAbstract() {
    return typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEntityCtType(this, p);
  }
}
