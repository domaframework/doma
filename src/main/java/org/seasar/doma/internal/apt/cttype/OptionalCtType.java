package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Optional;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;

public class OptionalCtType extends AbstractCtType {

  private final CtType elementCtType;

  private boolean isRawType;

  private boolean isWildcardType;

  public OptionalCtType(TypeMirror optionalType, Context ctx) {
    super(optionalType, ctx);
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(getTypeMirror());
    if (declaredType == null) {
      throw new AptIllegalStateException(getTypeName());
    }
    if (declaredType.getTypeArguments().isEmpty()) {
      isRawType = true;
      elementCtType = null;
    } else {
      TypeMirror typeArg = declaredType.getTypeArguments().get(0);
      if (typeArg.getKind() == TypeKind.WILDCARD || typeArg.getKind() == TypeKind.TYPEVAR) {
        isWildcardType = true;
        elementCtType = null;
      } else {
        EntityCtType entityCtType = EntityCtType.newInstance(typeArg, ctx);
        if (entityCtType != null) {
          elementCtType = entityCtType;
        } else {
          DomainCtType domainCtType = DomainCtType.newInstance(typeArg, ctx);
          if (domainCtType != null) {
            elementCtType = domainCtType;
          } else {
            BasicCtType basicCtType = BasicCtType.newInstance(typeArg, ctx);
            if (basicCtType != null) {
              elementCtType = basicCtType;
            } else {
              MapCtType mapCtType = MapCtType.newInstance(typeArg, ctx);
              if (mapCtType != null) {
                elementCtType = mapCtType;
              } else {
                elementCtType = AnyCtType.newInstance(typeArg, ctx);
              }
            }
          }
        }
      }
    }
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public boolean isRawType() {
    return isRawType;
  }

  public boolean isWildcardType() {
    return isWildcardType;
  }

  public static OptionalCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isSameType(type, Optional.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    return new OptionalCtType(type, ctx);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalCtType(this, p);
  }
}
