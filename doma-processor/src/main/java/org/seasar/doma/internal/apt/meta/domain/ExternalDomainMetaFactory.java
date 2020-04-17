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
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
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
  public ExternalDomainMeta createTypeElementMeta(TypeElement converterElement) {
    validateConverter(converterElement);
    TypeMirror[] argTypes = getConverterArgTypes(converterElement.asType());
    if (argTypes == null) {
      throw new AptIllegalStateException(
          "converter doesn't have type args: " + converterElement.getQualifiedName());
    }
    ExternalDomainMeta meta = new ExternalDomainMeta(converterElement);
    doDomainType(converterElement, argTypes[0], meta);
    doValueType(converterElement, argTypes[1], meta);
    return meta;
  }

  private void validateConverter(TypeElement converterElement) {
    if (!ctx.getMoreTypes()
        .isAssignableWithErasure(converterElement.asType(), DomainConverter.class)) {
      throw new AptException(Message.DOMA4191, converterElement, new Object[] {});
    }
    if (converterElement.getNestingKind().isNested()) {
      throw new AptException(Message.DOMA4198, converterElement, new Object[] {});
    }
    if (converterElement.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new AptException(Message.DOMA4192, converterElement, new Object[] {});
    }
    ExecutableElement constructor = ctx.getMoreElements().getNoArgConstructor(converterElement);
    if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
      throw new AptException(Message.DOMA4193, converterElement, new Object[] {});
    }
  }

  private TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
    for (TypeMirror supertype : ctx.getMoreTypes().directSupertypes(typeMirror)) {
      if (!ctx.getMoreTypes().isAssignableWithErasure(supertype, DomainConverter.class)) {
        continue;
      }
      if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, DomainConverter.class)) {
        DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(supertype);
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

  private void doDomainType(
      TypeElement converterElement, TypeMirror domainType, ExternalDomainMeta meta) {
    meta.setType(domainType);

    ArrayType arrayType = ctx.getMoreTypes().toArrayType(domainType);
    if (arrayType != null) {
      TypeMirror componentType = arrayType.getComponentType();
      if (componentType.getKind() == TypeKind.ARRAY) {
        throw new AptException(Message.DOMA4447, converterElement, new Object[] {});
      }
      TypeElement componentElement = ctx.getMoreTypes().toTypeElement(componentType);
      if (componentElement == null) {
        throw new AptIllegalStateException(componentType.toString());
      }
      if (!componentElement.getTypeParameters().isEmpty()) {
        throw new AptException(Message.DOMA4448, converterElement, new Object[] {});
      }
      return;
    }

    TypeElement domainElement = ctx.getMoreTypes().toTypeElement(domainType);
    if (domainElement == null) {
      throw new AptIllegalStateException(domainType.toString());
    }
    if (domainElement.getNestingKind().isNested()) {
      validateEnclosingElement(domainElement);
    }
    PackageElement pkgElement = ctx.getMoreElements().getPackageOf(domainElement);
    if (pkgElement.isUnnamed()) {
      throw new AptException(
          Message.DOMA4197, converterElement, new Object[] {domainElement.getQualifiedName()});
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(domainType);
    if (declaredType == null) {
      throw new AptIllegalStateException(domainType.toString());
    }
    for (TypeMirror typeArg : declaredType.getTypeArguments()) {
      if (typeArg.getKind() != TypeKind.WILDCARD) {
        throw new AptException(
            Message.DOMA4203, converterElement, new Object[] {domainElement.getQualifiedName()});
      }
    }
    meta.setTypeElement(domainElement);
    TypeParametersDef typeParametersDef = ctx.getMoreElements().getTypeParametersDef(domainElement);
    meta.setTypeParametersDef(typeParametersDef);
  }

  private void validateEnclosingElement(Element element) {
    TypeElement typeElement = ctx.getMoreElements().toTypeElement(element);
    if (typeElement == null) {
      return;
    }
    String simpleName = typeElement.getSimpleName().toString();
    if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
        || simpleName.contains(Constants.DESC_NAME_DELIMITER)) {
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

  private void doValueType(
      TypeElement converterElement, TypeMirror valueType, ExternalDomainMeta meta) {
    meta.setValueType(valueType);

    BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(valueType);
    if (basicCtType == null) {
      throw new AptException(Message.DOMA4194, converterElement, new Object[] {valueType});
    }
    meta.setBasicCtType(basicCtType);
  }
}
