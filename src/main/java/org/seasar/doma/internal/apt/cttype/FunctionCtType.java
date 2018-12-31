package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.function.Function;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class FunctionCtType extends AbstractCtType {

  protected CtType targetCtType;

  protected AnyCtType returnCtType;

  public FunctionCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public CtType getTargetCtType() {
    return targetCtType;
  }

  public AnyCtType getReturnCtType() {
    return returnCtType;
  }

  public boolean isRawType() {
    return returnCtType.getTypeMirror() == null || targetCtType.getTypeMirror() == null;
  }

  public boolean isWildcardType() {
    return returnCtType.getTypeMirror() != null
            && returnCtType.getTypeMirror().getKind() == TypeKind.WILDCARD
        || targetCtType.getTypeMirror() != null
            && targetCtType.getTypeMirror().getKind() == TypeKind.WILDCARD;
  }

  public static FunctionCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    DeclaredType functionDeclaredType = getFunctionDeclaredType(type, ctx);
    if (functionDeclaredType == null) {
      return null;
    }

    FunctionCtType functionCtType = new FunctionCtType(type, ctx);
    List<? extends TypeMirror> typeArguments = functionDeclaredType.getTypeArguments();
    if (typeArguments.size() == 2) {
      TypeMirror targetTypeMirror = typeArguments.get(0);
      TypeMirror returnTypeMirror = typeArguments.get(1);

      functionCtType.targetCtType = StreamCtType.newInstance(targetTypeMirror, ctx);
      if (functionCtType.targetCtType == null) {
        functionCtType.targetCtType = PreparedSqlCtType.newInstance(targetTypeMirror, ctx);
        if (functionCtType.targetCtType == null) {
          functionCtType.targetCtType = AnyCtType.newInstance(targetTypeMirror, ctx);
        }
      }

      functionCtType.returnCtType = AnyCtType.newInstance(returnTypeMirror, ctx);
    }

    return functionCtType;
  }

  protected static DeclaredType getFunctionDeclaredType(TypeMirror type, Context ctx) {
    if (ctx.getTypes().isSameType(type, Function.class)) {
      return ctx.getTypes().toDeclaredType(type);
    }
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(type)) {
      if (ctx.getTypes().isSameType(supertype, Function.class)) {
        return ctx.getTypes().toDeclaredType(supertype);
      }
      DeclaredType result = getFunctionDeclaredType(supertype, ctx);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitFunctionCtType(this, p);
  }
}
