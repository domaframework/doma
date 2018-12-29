package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.OptionalInt;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class OptionalIntCtType extends AbstractCtType {

  private final CtType elementCtType;

  public OptionalIntCtType(TypeMirror typeMirro, ProcessingEnvironment env) {
    super(typeMirro, env);
    PrimitiveType primitiveType = env.getTypeUtils().getPrimitiveType(TypeKind.INT);
    this.elementCtType = BasicCtType.newInstance(primitiveType, env);
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public static OptionalIntCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isSameType(type, OptionalInt.class, env)) {
      return null;
    }
    DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
    if (declaredType == null) {
      return null;
    }
    return new OptionalIntCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalIntCtType(this, p);
  }
}
