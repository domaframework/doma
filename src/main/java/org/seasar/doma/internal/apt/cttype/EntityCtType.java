package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

/** @author taedium */
public class EntityCtType extends AbstractCtType {

  private final boolean immutable;

  public EntityCtType(TypeMirror type, ProcessingEnvironment env, boolean immutable) {
    super(type, env);
    this.immutable = immutable;
  }

  public static EntityCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
    if (typeElement == null) {
      return null;
    }
    Entity entity = typeElement.getAnnotation(Entity.class);
    if (entity == null) {
      return null;
    }
    return new EntityCtType(type, env, entity.immutable());
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
