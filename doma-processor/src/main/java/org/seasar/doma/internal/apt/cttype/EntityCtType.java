package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.generator.Code;

public class EntityCtType extends AbstractCtType {

  private final boolean immutable;

  private final ClassName typeClassName;

  EntityCtType(Context ctx, TypeMirror type, boolean immutable, ClassName typeClassName) {
    super(ctx, type);
    assertNotNull(typeClassName);
    this.immutable = immutable;
    this.typeClassName = typeClassName;
  }

  public boolean isImmutable() {
    return immutable;
  }

  public boolean isAbstract() {
    return typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  public Code getTypeCode() {
    return new Code(p -> p.print("%1$s.getSingletonInternal()", typeClassName));
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitEntityCtType(this, p);
  }
}
