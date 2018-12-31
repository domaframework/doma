package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.function.BiFunction;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class BiFunctionCtType extends AbstractCtType {

  protected final boolean isRawType;

  protected CtType firstArgCtType;

  protected CtType secondArgCtType;

  protected AnyCtType resultCtType;

  public BiFunctionCtType(TypeMirror type, Context ctx, boolean isRawType) {
    super(type, ctx);
    this.isRawType = isRawType;
  }

  public CtType getFirstArgCtType() {
    return firstArgCtType;
  }

  public CtType getSecondArgCtType() {
    return secondArgCtType;
  }

  public AnyCtType getResultCtType() {
    return resultCtType;
  }

  public boolean isRawType() {
    return isRawType;
  }

  public boolean isWildcardType() {
    return resultCtType.getTypeMirror() != null
            && resultCtType.getTypeMirror().getKind() == TypeKind.WILDCARD
        || firstArgCtType.getTypeMirror() != null
            && firstArgCtType.getTypeMirror().getKind() == TypeKind.WILDCARD
        || secondArgCtType.getTypeMirror() != null
            && secondArgCtType.getTypeMirror().getKind() == TypeKind.WILDCARD;
  }

  public static BiFunctionCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    DeclaredType biFunctionDeclaredType = getBiFunctionDeclaredType(type, ctx);
    if (biFunctionDeclaredType == null) {
      return null;
    }

    List<? extends TypeMirror> typeArguments = biFunctionDeclaredType.getTypeArguments();
    boolean isRawType = typeArguments.size() != 3;
    BiFunctionCtType biFunctionCtType = new BiFunctionCtType(type, ctx, isRawType);
    if (!isRawType) {
      TypeMirror firstArgTypeMirror = typeArguments.get(0);
      TypeMirror secondArgTypeMirror = typeArguments.get(1);
      TypeMirror resultTypeMirror = typeArguments.get(2);

      biFunctionCtType.firstArgCtType = ConfigCtType.newInstance(firstArgTypeMirror, ctx);
      if (biFunctionCtType.firstArgCtType == null) {
        biFunctionCtType.firstArgCtType = AnyCtType.newInstance(firstArgTypeMirror, ctx);
      }

      biFunctionCtType.secondArgCtType = PreparedSqlCtType.newInstance(secondArgTypeMirror, ctx);
      if (biFunctionCtType.secondArgCtType == null) {
        biFunctionCtType.secondArgCtType = AnyCtType.newInstance(secondArgTypeMirror, ctx);
      }

      biFunctionCtType.resultCtType = AnyCtType.newInstance(resultTypeMirror, ctx);
    }

    return biFunctionCtType;
  }

  protected static DeclaredType getBiFunctionDeclaredType(TypeMirror type, Context ctx) {
    if (ctx.getTypes().isSameType(type, BiFunction.class)) {
      return ctx.getTypes().toDeclaredType(type);
    }
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(type)) {
      if (ctx.getTypes().isSameType(supertype, BiFunction.class)) {
        return ctx.getTypes().toDeclaredType(supertype);
      }
      DeclaredType result = getBiFunctionDeclaredType(supertype, ctx);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBiFunctionCtType(this, p);
  }
}
