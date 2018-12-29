package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Optional;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class OptionalCtType extends AbstractCtType {

  private final CtType elementCtType;

  private boolean isRawType;

  private boolean isWildcardType;

  public OptionalCtType(TypeMirror optionalType, ProcessingEnvironment env) {
    super(optionalType, env);
    DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(getTypeMirror(), env);
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
        EntityCtType entityCtType = EntityCtType.newInstance(typeArg, env);
        if (entityCtType != null) {
          elementCtType = entityCtType;
        } else {
          DomainCtType domainCtType = DomainCtType.newInstance(typeArg, env);
          if (domainCtType != null) {
            elementCtType = domainCtType;
          } else {
            BasicCtType basicCtType = BasicCtType.newInstance(typeArg, env);
            if (basicCtType != null) {
              elementCtType = basicCtType;
            } else {
              MapCtType mapCtType = MapCtType.newInstance(typeArg, env);
              if (mapCtType != null) {
                elementCtType = mapCtType;
              } else {
                elementCtType = AnyCtType.newInstance(typeArg, env);
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

  public static OptionalCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isSameType(type, Optional.class, env)) {
      return null;
    }
    DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
    if (declaredType == null) {
      return null;
    }
    return new OptionalCtType(type, env);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitOptionalCtType(this, p);
  }
}
