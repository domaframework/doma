package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import java.util.Map;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class MapCtType extends AbstractCtType {

  public MapCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public static MapCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isSameType(type, Map.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() != 2) {
      return null;
    }
    if (!ctx.getTypes().isSameType(typeArgs.get(0), String.class)) {
      return null;
    }
    if (!ctx.getTypes().isSameType(typeArgs.get(1), Object.class)) {
      return null;
    }
    return new MapCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitMapCtType(this, p);
  }
}
