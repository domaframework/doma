package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.OptionalDouble;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class OptionalDoubleCtType extends AbstractCtType {

  private final CtType elementCtType;

  public OptionalDoubleCtType(TypeMirror typeMirro, Context ctx) {
    super(typeMirro, ctx);
    PrimitiveType primitiveType = ctx.getTypes().getPrimitiveType(TypeKind.DOUBLE);
    this.elementCtType = BasicCtType.newInstance(primitiveType, ctx);
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public static OptionalDoubleCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isSameType(type, OptionalDouble.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    return new OptionalDoubleCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalDoubleCtType(this, p);
  }
}
