package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.OptionalInt;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class OptionalIntCtType extends AbstractCtType {

  private final CtType elementCtType;

  public OptionalIntCtType(TypeMirror typeMirro, Context ctx) {
    super(typeMirro, ctx);
    PrimitiveType primitiveType = ctx.getTypes().getPrimitiveType(TypeKind.INT);
    this.elementCtType = BasicCtType.newInstance(primitiveType, ctx);
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public static OptionalIntCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isSameType(type, OptionalInt.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    return new OptionalIntCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalIntCtType(this, p);
  }
}
