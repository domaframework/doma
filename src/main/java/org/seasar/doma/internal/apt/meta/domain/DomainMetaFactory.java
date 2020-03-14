package org.seasar.doma.internal.apt.meta.domain;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.DomainAnnot;
import org.seasar.doma.internal.apt.annot.ValueAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.util.ElementKindUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.message.Message;

public class DomainMetaFactory implements TypeElementMetaFactory<DomainMeta> {

  private final Context ctx;

  public DomainMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public DomainMeta createTypeElementMeta(TypeElement classElement) {
    assertNotNull(classElement);
    DomainAnnot domainAnnot = ctx.getAnnotations().newDomainAnnot(classElement);
    if (domainAnnot == null) {
      throw new AptIllegalStateException("domainAnnot");
    }
    DomainMeta domainMeta = new DomainMeta(classElement, classElement.asType());
    domainMeta.setDomainAnnot(domainAnnot);
    Strategy strategy = createStrategy(classElement, domainMeta);
    strategy.doTypeParameters(classElement, domainMeta);
    strategy.doWrapperCtType(classElement, domainMeta);
    strategy.validateAcceptNull(classElement, domainMeta);
    strategy.validateClass(classElement, domainMeta);
    strategy.validateInitializer(classElement, domainMeta);
    strategy.validateAccessorMethod(classElement, domainMeta);
    return domainMeta;
  }

  private Strategy createStrategy(TypeElement classElement, DomainMeta domainMeta) {
    ValueAnnot valueAnnot = ctx.getAnnotations().newValueAnnot(classElement);
    if (valueAnnot != null) {
      return new ValueStrategy(ctx, valueAnnot);
    }
    return new DefaultStrategy(ctx);
  }

  interface Strategy {

    void doTypeParameters(TypeElement classElement, DomainMeta domainMeta);

    void doWrapperCtType(TypeElement classElement, DomainMeta domainMeta);

    void validateAcceptNull(TypeElement classElement, DomainMeta domainMeta);

    void validateClass(TypeElement classElement, DomainMeta domainMeta);

    void validateInitializer(TypeElement classElement, DomainMeta domainMeta);

    void validateAccessorMethod(TypeElement classElement, DomainMeta domainMeta);
  }

  protected static class DefaultStrategy implements Strategy {

    final Context ctx;

    DefaultStrategy(Context ctx) {
      assertNotNull(ctx);
      this.ctx = ctx;
    }

    @Override
    public void doTypeParameters(TypeElement classElement, DomainMeta domainMeta) {
      TypeParametersDef typeParametersDef =
          ctx.getMoreElements().getTypeParametersDef(classElement);
      domainMeta.setTypeParametersDef(typeParametersDef);
    }

    @Override
    public void doWrapperCtType(TypeElement classElement, DomainMeta domainMeta) {
      BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(domainMeta.getValueType());
      if (basicCtType == null) {
        DomainAnnot domainAnnot = domainMeta.getDomainAnnot();
        throw new AptException(
            Message.DOMA4102,
            classElement,
            domainAnnot.getAnnotationMirror(),
            domainAnnot.getValueType(),
            new Object[] {domainAnnot.getValueTypeValue()});
      }
      domainMeta.setBasicCtType(basicCtType);
    }

    @Override
    public void validateAcceptNull(TypeElement classElement, DomainMeta domainMeta) {
      if (domainMeta.getBasicCtType().isPrimitive() && domainMeta.getAcceptNull()) {
        DomainAnnot domainAnnot = domainMeta.getDomainAnnot();
        throw new AptException(
            Message.DOMA4251,
            classElement,
            domainAnnot.getAnnotationMirror(),
            domainAnnot.getAcceptNull(),
            new Object[] {});
      }
    }

    @Override
    public void validateClass(TypeElement classElement, DomainMeta domainMeta) {
      ElementKind kind = classElement.getKind();
      if (kind == ElementKind.CLASS || ElementKindUtil.isRecord(kind)) {
        if (domainMeta.providesConstructor()
            && classElement.getModifiers().contains(Modifier.ABSTRACT)) {
          throw new AptException(Message.DOMA4132, classElement, new Object[] {});
        }
        if (classElement.getNestingKind().isNested()) {
          validateEnclosingElement(classElement);
        }
      } else if (kind == ElementKind.ENUM) {
        if (domainMeta.providesConstructor()) {
          DomainAnnot domainAnnot = domainMeta.getDomainAnnot();
          throw new AptException(
              Message.DOMA4184,
              classElement,
              domainAnnot.getAnnotationMirror(),
              domainAnnot.getFactoryMethod(),
              new Object[] {});
        }
        if (classElement.getNestingKind().isNested()) {
          validateEnclosingElement(classElement);
        }
      } else if (kind == ElementKind.INTERFACE) {
        if (domainMeta.providesConstructor()) {
          throw new AptException(Message.DOMA4268, classElement, new Object[] {});
        }
        if (classElement.getNestingKind().isNested()) {
          validateEnclosingElement(classElement);
        }
      } else {
        DomainAnnot domainAnnot = domainMeta.getDomainAnnot();
        throw new AptException(
            Message.DOMA4105, classElement, domainAnnot.getAnnotationMirror(), new Object[] {});
      }
    }

    void validateEnclosingElement(Element element) {
      TypeElement typeElement = ctx.getMoreElements().toTypeElement(element);
      if (typeElement == null) {
        return;
      }
      String simpleName = typeElement.getSimpleName().toString();
      if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
          || simpleName.contains(Constants.DESC_NAME_DELIMITER)) {
        throw new AptException(
            Message.DOMA4277, typeElement, new Object[] {typeElement.getQualifiedName()});
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
              Message.DOMA4275, typeElement, new Object[] {typeElement.getQualifiedName()});
        }
      } else {
        throw new AptException(
            Message.DOMA4276, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    }

    @Override
    public void validateInitializer(TypeElement classElement, DomainMeta domainMeta) {
      if (domainMeta.providesConstructor()) {
        validateConstructor(classElement, domainMeta);
      } else {
        validateFactoryMethod(classElement, domainMeta);
      }
    }

    void validateConstructor(TypeElement classElement, DomainMeta domainMeta) {
      for (ExecutableElement constructor :
          ElementFilter.constructorsIn(classElement.getEnclosedElements())) {
        if (constructor.getModifiers().contains(Modifier.PRIVATE)) {
          continue;
        }
        List<? extends VariableElement> parameters = constructor.getParameters();
        if (parameters.size() != 1) {
          continue;
        }
        TypeMirror parameterType = ctx.getMoreTypes().erasure(parameters.get(0).asType());
        if (ctx.getMoreTypes().isSameTypeWithErasure(parameterType, domainMeta.getValueType())) {
          return;
        }
      }
      throw new AptException(
          Message.DOMA4103, classElement, new Object[] {domainMeta.getValueType()});
    }

    void validateFactoryMethod(TypeElement classElement, DomainMeta domainMeta) {
      outer:
      for (ExecutableElement method : ElementFilter.methodsIn(classElement.getEnclosedElements())) {
        if (!method.getSimpleName().contentEquals(domainMeta.getFactoryMethod())) {
          continue;
        }
        if (method.getModifiers().contains(Modifier.PRIVATE)) {
          continue;
        }
        if (!method.getModifiers().contains(Modifier.STATIC)) {
          continue;
        }
        if (method.getParameters().size() != 1) {
          continue;
        }
        TypeMirror parameterType = method.getParameters().get(0).asType();
        if (!ctx.getMoreTypes().isAssignableWithErasure(domainMeta.getValueType(), parameterType)) {
          continue;
        }
        TypeMirror returnType = ctx.getMoreTypes().erasure(method.getReturnType());
        if (!ctx.getMoreTypes().isAssignableWithErasure(returnType, domainMeta.getType())) {
          continue;
        }
        List<? extends TypeParameterElement> classTypeParams = classElement.getTypeParameters();
        List<? extends TypeParameterElement> methodTypeParams = method.getTypeParameters();
        if (classTypeParams.size() != methodTypeParams.size()) {
          continue;
        }
        for (Iterator<? extends TypeParameterElement> cit = classTypeParams.iterator(),
                mit = methodTypeParams.iterator();
            cit.hasNext() && mit.hasNext(); ) {
          TypeParameterElement classTypeParam = cit.next();
          TypeParameterElement methodTypeParam = mit.next();
          if (!ctx.getMoreTypes()
              .isSameTypeWithErasure(classTypeParam.asType(), methodTypeParam.asType())) {
            continue outer;
          }
        }
        return;
      }
      throw new AptException(
          Message.DOMA4106,
          classElement,
          new Object[] {
            domainMeta.getFactoryMethod(), classElement.asType(), domainMeta.getValueType()
          });
    }

    @Override
    public void validateAccessorMethod(TypeElement classElement, DomainMeta domainMeta) {
      TypeElement typeElement = classElement;
      TypeMirror typeMirror = classElement.asType();
      for (; typeElement != null && typeMirror.getKind() != TypeKind.NONE; ) {
        for (ExecutableElement method :
            ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
          if (!method.getSimpleName().contentEquals(domainMeta.getAccessorMethod())) {
            continue;
          }
          if (method.getModifiers().contains(Modifier.PRIVATE)) {
            continue;
          }
          if (!method.getParameters().isEmpty()) {
            continue;
          }
          TypeMirror returnType = method.getReturnType();
          if (ctx.getMoreTypes()
              .isAssignableWithErasure(
                  ctx.getMoreTypes().erasure(returnType), domainMeta.getValueType())) {
            return;
          }
          TypeVariable typeVariable = ctx.getMoreTypes().toTypeVariable(returnType);
          if (typeVariable != null) {
            TypeMirror inferredReturnType = inferType(typeVariable, typeElement, typeMirror);
            if (inferredReturnType != null) {
              if (ctx.getMoreTypes()
                  .isAssignableWithErasure(inferredReturnType, domainMeta.getValueType())) {
                return;
              }
            }
          }
        }
        typeMirror = typeElement.getSuperclass();
        typeElement = ctx.getMoreTypes().toTypeElement(typeMirror);
      }
      throw new AptException(
          Message.DOMA4104,
          classElement,
          new Object[] {domainMeta.getAccessorMethod(), domainMeta.getValueType()});
    }

    TypeMirror inferType(
        TypeVariable typeVariable, TypeElement classElement, TypeMirror classMirror) {
      DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(classMirror);
      if (declaredType == null) {
        return null;
      }
      List<? extends TypeMirror> args = declaredType.getTypeArguments();
      if (args.isEmpty()) {
        return null;
      }
      int argsSize = args.size();
      int index = 0;
      for (TypeParameterElement typeParam : classElement.getTypeParameters()) {
        if (index >= argsSize) {
          break;
        }
        if (ctx.getMoreTypes().isSameTypeWithErasure(typeVariable, typeParam.asType())) {
          return args.get(index);
        }
        index++;
      }
      return null;
    }
  }

  protected static class ValueStrategy extends DefaultStrategy {

    final ValueAnnot valueAnnot;

    ValueStrategy(Context ctx, ValueAnnot valueAnnot) {
      super(ctx);
      assertNotNull(valueAnnot);
      this.valueAnnot = valueAnnot;
    }

    @Override
    public void validateInitializer(TypeElement classElement, DomainMeta domainMeta) {
      if (!valueAnnot.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4428,
            classElement,
            valueAnnot.getAnnotationMirror(),
            valueAnnot.getStaticConstructor(),
            new Object[] {});
      }
    }

    @Override
    public void validateAccessorMethod(TypeElement classElement, DomainMeta domainMeta) {
      VariableElement field = findSingleField(classElement, domainMeta);
      String accessorMethod = inferAccessorMethod(field);
      if (!accessorMethod.equals(domainMeta.getAccessorMethod())) {
        DomainAnnot domainAnnot = domainMeta.getDomainAnnot();
        throw new AptException(
            Message.DOMA4429,
            classElement,
            domainAnnot.getAnnotationMirror(),
            domainAnnot.getAccessorMethod(),
            new Object[] {});
      }
    }

    String inferAccessorMethod(VariableElement field) {
      String name = field.getSimpleName().toString();
      String capitalizedName = StringUtil.capitalize(name);
      if (field.asType().getKind() == TypeKind.BOOLEAN) {
        if (name.startsWith("is") && (name.length() > 2 && Character.isUpperCase(name.charAt(2)))) {
          return name;
        }
        return "is" + capitalizedName;
      }
      return "get" + capitalizedName;
    }

    VariableElement findSingleField(TypeElement classElement, DomainMeta domainMeta) {
      List<VariableElement> fields =
          ElementFilter.fieldsIn(classElement.getEnclosedElements()).stream()
              .filter(field -> !field.getModifiers().contains(Modifier.STATIC))
              .collect(Collectors.toList());
      if (fields.size() == 0) {
        throw new AptException(Message.DOMA4430, classElement, new Object[] {});
      }
      if (fields.size() > 1) {
        throw new AptException(Message.DOMA4431, classElement, new Object[] {});
      }
      VariableElement field = fields.get(0);
      if (!ctx.getMoreTypes().isAssignableWithErasure(field.asType(), domainMeta.getValueType())) {
        throw new AptException(
            Message.DOMA4432, field, new Object[] {field.asType(), domainMeta.getValueType()});
      }
      return field;
    }
  }
}
