package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.function.BiFunction;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class BiFunctionCtType extends AbstractCtType {

  protected final boolean isRawType;

  protected CtType firstArgCtType;

  protected CtType secondArgCtType;

  protected AnyCtType resultCtType;

  public BiFunctionCtType(TypeMirror type, ProcessingEnvironment env, boolean isRawType) {
    super(type, env);
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

  public static BiFunctionCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    DeclaredType biFunctionDeclaredType = getBiFunctionDeclaredType(type, env);
    if (biFunctionDeclaredType == null) {
      return null;
    }

    List<? extends TypeMirror> typeArguments = biFunctionDeclaredType.getTypeArguments();
    boolean isRawType = typeArguments.size() != 3;
    BiFunctionCtType biFunctionCtType = new BiFunctionCtType(type, env, isRawType);
    if (!isRawType) {
      TypeMirror firstArgTypeMirror = typeArguments.get(0);
      TypeMirror secondArgTypeMirror = typeArguments.get(1);
      TypeMirror resultTypeMirror = typeArguments.get(2);

      biFunctionCtType.firstArgCtType = ConfigCtType.newInstance(firstArgTypeMirror, env);
      if (biFunctionCtType.firstArgCtType == null) {
        biFunctionCtType.firstArgCtType = AnyCtType.newInstance(firstArgTypeMirror, env);
      }

      biFunctionCtType.secondArgCtType = PreparedSqlCtType.newInstance(secondArgTypeMirror, env);
      if (biFunctionCtType.secondArgCtType == null) {
        biFunctionCtType.secondArgCtType = AnyCtType.newInstance(secondArgTypeMirror, env);
      }

      biFunctionCtType.resultCtType = AnyCtType.newInstance(resultTypeMirror, env);
    }

    return biFunctionCtType;
  }

  protected static DeclaredType getBiFunctionDeclaredType(
      TypeMirror type, ProcessingEnvironment env) {
    if (TypeMirrorUtil.isSameType(type, BiFunction.class, env)) {
      return TypeMirrorUtil.toDeclaredType(type, env);
    }
    for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
      if (TypeMirrorUtil.isSameType(supertype, BiFunction.class, env)) {
        return TypeMirrorUtil.toDeclaredType(supertype, env);
      }
      DeclaredType result = getBiFunctionDeclaredType(supertype, env);
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
