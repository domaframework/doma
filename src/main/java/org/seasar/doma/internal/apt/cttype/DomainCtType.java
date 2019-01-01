package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.DomainConvertersAnnot;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.message.Message;

public class DomainCtType extends AbstractCtType {

  protected final BasicCtType basicCtType;

  protected final boolean external;

  protected final String metaClassName;

  private final String typeArgDecl;

  private boolean isRawType;

  private boolean isWildcardType;

  public DomainCtType(
      TypeMirror domainType, Context ctx, BasicCtType basicCtType, boolean external) {
    super(domainType, ctx);
    assertNotNull(basicCtType);
    this.basicCtType = basicCtType;
    this.external = external;
    int pos = metaTypeName.indexOf('<');
    if (pos > -1) {
      this.metaClassName = metaTypeName.substring(0, pos);
      this.typeArgDecl = metaTypeName.substring(pos);
    } else {
      this.metaClassName = metaTypeName;
      this.typeArgDecl = "";
    }
    if (!typeElement.getTypeParameters().isEmpty()) {
      DeclaredType declaredType = ctx.getTypes().toDeclaredType(getTypeMirror());
      if (declaredType == null) {
        throw new AptIllegalStateException(getTypeName());
      }
      if (declaredType.getTypeArguments().isEmpty()) {
        isRawType = true;
      }
      for (TypeMirror typeArg : declaredType.getTypeArguments()) {
        if (typeArg.getKind() == TypeKind.WILDCARD || typeArg.getKind() == TypeKind.TYPEVAR) {
          isWildcardType = true;
        }
      }
    }
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public boolean isRawType() {
    return isRawType;
  }

  public boolean isWildcardType() {
    return isWildcardType;
  }

  public String getInstantiationCommand() {
    return normalize(metaClassName) + "." + typeArgDecl + "getSingletonInternal()";
  }

  @Override
  public String getMetaTypeName() {
    return normalize(metaTypeName);
  }

  protected String normalize(String name) {
    if (external) {
      return Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE + "." + name;
    }
    return name;
  }

  public static DomainCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    DomainInfo info = getDomainInfo(typeElement, ctx);
    if (info == null) {
      return null;
    }
    BasicCtType basicCtType = BasicCtType.newInstance(info.valueType, ctx);
    if (basicCtType == null) {
      return null;
    }
    return new DomainCtType(type, ctx, basicCtType, info.external);
  }

  protected static DomainInfo getDomainInfo(TypeElement typeElement, Context ctx) {
    Domain domain = typeElement.getAnnotation(Domain.class);
    if (domain != null) {
      return getDomainInfo(typeElement, domain);
    }
    return getExternalDomainInfo(typeElement, ctx);
  }

  protected static DomainInfo getDomainInfo(TypeElement typeElement, Domain domain) {
    try {
      domain.valueType();
    } catch (MirroredTypeException e) {
      return new DomainInfo(e.getTypeMirror(), false);
    }
    throw new AptIllegalStateException("unreachable.");
  }

  protected static DomainInfo getExternalDomainInfo(TypeElement typeElement, Context ctx) {
    String csv = ctx.getOptions().getDomainConverters();
    if (csv != null) {
      TypeMirror domainType = typeElement.asType();
      for (String value : csv.split(",")) {
        String className = value.trim();
        if (className.isEmpty()) {
          continue;
        }
        TypeElement convertersProviderElement = ctx.getElements().getTypeElement(className);
        if (convertersProviderElement == null) {
          throw new AptIllegalOptionException(Message.DOMA4200.getMessage(className));
        }
        DomainConvertersAnnot convertersMirror =
            ctx.getAnnotations().newDomainConvertersAnnot(convertersProviderElement);
        if (convertersMirror == null) {
          throw new AptIllegalOptionException(Message.DOMA4201.getMessage(className));
        }
        for (TypeMirror converterType : convertersMirror.getValueValue()) {
          // converterType does not contain adequate information in
          // eclipse incremental compile, so reload typeMirror
          converterType = reloadTypeMirror(converterType, ctx);
          if (converterType == null) {
            continue;
          }
          TypeMirror[] argTypes = getConverterArgTypes(converterType, ctx);
          if (argTypes == null || !ctx.getTypes().isSameType(domainType, argTypes[0])) {
            continue;
          }
          TypeMirror valueType = argTypes[1];
          return new DomainInfo(valueType, true);
        }
      }
    }
    return null;
  }

  protected static TypeMirror reloadTypeMirror(TypeMirror typeMirror, Context ctx) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(typeMirror);
    if (typeElement == null) {
      return null;
    }
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    typeElement = ctx.getElements().getTypeElement(binaryName);
    if (typeElement == null) {
      return null;
    }
    return typeElement.asType();
  }

  protected static TypeMirror[] getConverterArgTypes(TypeMirror typeMirror, Context ctx) {
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(typeMirror)) {
      if (!ctx.getTypes().isAssignable(supertype, DomainConverter.class)) {
        continue;
      }
      if (ctx.getTypes().isSameType(supertype, DomainConverter.class)) {
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
        assertNotNull(declaredType);
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        assertEquals(2, args.size());
        return new TypeMirror[] {args.get(0), args.get(1)};
      }
      TypeMirror[] argTypes = getConverterArgTypes(supertype, ctx);
      if (argTypes != null) {
        return argTypes;
      }
    }
    return null;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitDomainCtType(this, p);
  }

  private static class DomainInfo {
    private final TypeMirror valueType;

    private final boolean external;

    public DomainInfo(TypeMirror valueType, boolean external) {
      assertNotNull(valueType);
      this.valueType = valueType;
      this.external = external;
    }
  }
}
