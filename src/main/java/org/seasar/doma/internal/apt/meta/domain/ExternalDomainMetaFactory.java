package org.seasar.doma.internal.apt.meta.domain;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.message.Message;

public class ExternalDomainMetaFactory implements TypeElementMetaFactory<ExternalDomainMeta> {

  private final Context ctx;

  public ExternalDomainMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public ExternalDomainMeta createTypeElementMeta(TypeElement convElement) {
    validateConverter(convElement);
    TypeMirror[] argTypes = getConverterArgTypes(convElement.asType());
    if (argTypes == null) {
      throw new AptIllegalStateException(
          "converter doesn't have type args: " + convElement.getQualifiedName());
    }
    ExternalDomainMeta meta = new ExternalDomainMeta(convElement);
    doDomainType(convElement, argTypes[0], meta);
    doValueType(convElement, argTypes[1], meta);
    return meta;
  }

  protected void validateConverter(TypeElement convElement) {
    if (!ctx.getTypes().isAssignableWithErasure(convElement.asType(), DomainConverter.class)) {
      throw new AptException(Message.DOMA4191, convElement, new Object[] {});
    }
    if (convElement.getNestingKind().isNested()) {
      throw new AptException(Message.DOMA4198, convElement, new Object[] {});
    }
    if (convElement.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new AptException(Message.DOMA4192, convElement, new Object[] {});
    }
    ExecutableElement constructor = ctx.getElements().getNoArgConstructor(convElement);
    if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(Message.DOMA4193, convElement, new Object[] {});
    }
  }

  protected TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(typeMirror)) {
      if (!ctx.getTypes().isAssignableWithErasure(supertype, DomainConverter.class)) {
        continue;
      }
      if (ctx.getTypes().isSameTypeWithErasure(supertype, DomainConverter.class)) {
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
        assertNotNull(declaredType);
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        assertEquals(2, args.size());
        return new TypeMirror[] {args.get(0), args.get(1)};
      }
      TypeMirror[] argTypes = getConverterArgTypes(supertype);
      if (argTypes != null) {
        return argTypes;
      }
    }
    return null;
  }

  protected void doDomainType(
      TypeElement convElement, TypeMirror domainType, ExternalDomainMeta meta) {
    TypeElement domainElement = ctx.getTypes().toTypeElement(domainType);
    if (domainElement == null) {
      throw new AptIllegalStateException(domainType.toString());
    }
    if (domainElement.getNestingKind().isNested()) {
      validateEnclosingElement(domainElement);
    }
    PackageElement pkgElement = ctx.getElements().getPackageOf(domainElement);
    if (pkgElement.isUnnamed()) {
      throw new AptException(
          Message.DOMA4197, convElement, new Object[] {domainElement.getQualifiedName()});
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(domainType);
    if (declaredType == null) {
      throw new AptIllegalStateException(domainType.toString());
    }
    for (TypeMirror typeArg : declaredType.getTypeArguments()) {
      if (typeArg.getKind() != TypeKind.WILDCARD) {
        throw new AptException(
            Message.DOMA4203, convElement, new Object[] {domainElement.getQualifiedName()});
      }
    }
    meta.setDomainElement(domainElement);
  }

  protected void validateEnclosingElement(Element element) {
    TypeElement typeElement = ctx.getElements().toTypeElement(element);
    if (typeElement == null) {
      return;
    }
    String simpleName = typeElement.getSimpleName().toString();
    if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
        || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
      throw new AptException(
          Message.DOMA4280, typeElement, new Object[] {typeElement.getQualifiedName()});
    }
    NestingKind nestingKind = typeElement.getNestingKind();
    if (nestingKind == NestingKind.TOP_LEVEL) {
      return;
    } else if (nestingKind == NestingKind.MEMBER) {
      Set<Modifier> modifiers = typeElement.getModifiers();
      if (modifiers.containsAll(Arrays.asList(Modifier.STATIC, Modifier.PUBLIC))) {
        validateEnclosingElement(typeElement.getEnclosingElement());
      } else {
        throw new AptException(
            Message.DOMA4278, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    } else {
      throw new AptException(
          Message.DOMA4279, typeElement, new Object[] {typeElement.getQualifiedName()});
    }
  }

  protected void doValueType(
      TypeElement convElement, TypeMirror valueType, ExternalDomainMeta meta) {
    String valueTypeName = ctx.getTypes().getTypeName(valueType);
    meta.setValueTypeName(valueTypeName);

    BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(valueType);
    if (basicCtType == null) {
      throw new AptException(Message.DOMA4194, convElement, new Object[] {valueTypeName});
    }
    meta.setBasicCtType(basicCtType);
  }
}
