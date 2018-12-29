package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.OptionalLong;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class OptionalLongCtType extends AbstractCtType {

  private final CtType elementCtType;

  public OptionalLongCtType(TypeMirror typeMirro, ProcessingEnvironment env) {
    super(typeMirro, env);
    PrimitiveType primitiveType = env.getTypeUtils().getPrimitiveType(TypeKind.LONG);
    this.elementCtType = BasicCtType.newInstance(primitiveType, env);
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public static OptionalLongCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isSameType(type, OptionalLong.class, env)) {
      return null;
    }
    DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
    if (declaredType == null) {
      return null;
    }
    return new OptionalLongCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalLongCtType(this, p);
  }
}
