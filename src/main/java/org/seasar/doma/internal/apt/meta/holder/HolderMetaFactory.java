package org.seasar.doma.internal.apt.meta.holder;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.HolderAnnot;
import org.seasar.doma.internal.apt.annot.ValueAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.message.Message;

public class HolderMetaFactory implements TypeElementMetaFactory<HolderMeta> {

  private final Context ctx;

  private final TypeElement holderElement;

  private final HolderAnnot holderAnnot;

  public HolderMetaFactory(Context ctx, TypeElement classElement) {
    assertNotNull(ctx);
    this.ctx = ctx;
    this.holderElement = classElement;
    holderAnnot = ctx.getAnnots().newHolderAnnot(classElement);
    if (holderAnnot == null) {
      throw new AptIllegalStateException("holderAnnot");
    }
  }

  @Override
  public HolderMeta createTypeElementMeta() {
    assertNotNull(holderElement);
    var basicCtType = createBasicCtType();
    var holderMeta =
        new HolderMeta(holderElement, holderElement.asType(), holderAnnot, basicCtType);
    var strategy = createStrategy();
    strategy.validateAcceptNull(holderMeta);
    strategy.validateClass(holderMeta);
    strategy.validateInitializer(holderMeta);
    strategy.validateAccessorMethod(holderMeta);
    return holderMeta;
  }

  private BasicCtType createBasicCtType() {
    var valueType = holderAnnot.getValueTypeValue();
    var basicCtType = ctx.getCtTypes().newBasicCtType(valueType);
    if (basicCtType == null) {
      throw new AptException(
          Message.DOMA4102,
          holderElement,
          holderAnnot.getAnnotationMirror(),
          holderAnnot.getValueType(),
          new Object[] {valueType});
    }
    return basicCtType;
  }

  private Strategy createStrategy() {
    var valueAnnot = ctx.getAnnots().newValueAnnot(holderElement);
    if (valueAnnot != null) {
      return new ValueStrategy(valueAnnot);
    }
    return new DefaultStrategy();
  }

  protected interface Strategy {

    void validateAcceptNull(HolderMeta holderMeta);

    void validateClass(HolderMeta holderMeta);

    void validateInitializer(HolderMeta holderMeta);

    void validateAccessorMethod(HolderMeta holderMeta);
  }

  protected class DefaultStrategy implements Strategy {

    @Override
    public void validateAcceptNull(HolderMeta holderMeta) {
      if (holderMeta.getBasicCtType().isPrimitive() && holderMeta.getAcceptNull()) {
        var holderAnnot = holderMeta.getHolderAnnot();
        throw new AptException(
            Message.DOMA4251,
            holderElement,
            holderAnnot.getAnnotationMirror(),
            holderAnnot.getAcceptNull());
      }
    }

    @Override
    public void validateClass(HolderMeta holderMeta) {
      if (holderElement.getKind() == ElementKind.CLASS) {
        if (holderMeta.providesConstructor()
            && holderElement.getModifiers().contains(Modifier.ABSTRACT)) {
          throw new AptException(Message.DOMA4132, holderElement);
        }
        if (holderElement.getNestingKind().isNested()) {
          validateEnclosingElement(holderElement);
        }
      } else if (holderElement.getKind() == ElementKind.ENUM) {
        if (holderMeta.providesConstructor()) {
          var holderAnnot = holderMeta.getHolderAnnot();
          throw new AptException(
              Message.DOMA4184,
              holderElement,
              holderAnnot.getAnnotationMirror(),
              holderAnnot.getFactoryMethod());
        }
        if (holderElement.getNestingKind().isNested()) {
          validateEnclosingElement(holderElement);
        }
      } else if (holderElement.getKind() == ElementKind.INTERFACE) {
        if (holderMeta.providesConstructor()) {
          throw new AptException(Message.DOMA4268, holderElement);
        }
        if (holderElement.getNestingKind().isNested()) {
          validateEnclosingElement(holderElement);
        }
      } else {
        var holderAnnot = holderMeta.getHolderAnnot();
        throw new AptException(Message.DOMA4105, holderElement, holderAnnot.getAnnotationMirror());
      }
    }

    private void validateEnclosingElement(Element element) {
      var typeElement = ctx.getElements().toTypeElement(element);
      if (typeElement == null) {
        return;
      }
      var simpleName = typeElement.getSimpleName().toString();
      if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
          || simpleName.contains(Constants.DESC_NAME_DELIMITER)) {
        throw new AptException(
            Message.DOMA4277, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
      var nestingKind = typeElement.getNestingKind();
      if (nestingKind == NestingKind.TOP_LEVEL) {
        //noinspection UnnecessaryReturnStatement
        return;
      } else if (nestingKind == NestingKind.MEMBER) {
        var modifiers = typeElement.getModifiers();
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
    public void validateInitializer(HolderMeta holderMeta) {
      if (holderMeta.providesConstructor()) {
        validateConstructor(holderMeta);
      } else {
        validateFactoryMethod(holderMeta);
      }
    }

    private void validateConstructor(HolderMeta holderMeta) {
      for (var constructor : ElementFilter.constructorsIn(holderElement.getEnclosedElements())) {
        if (constructor.getModifiers().contains(Modifier.PRIVATE)) {
          continue;
        }
        var parameters = constructor.getParameters();
        if (parameters.size() != 1) {
          continue;
        }
        var parameterType = parameters.get(0).asType();
        if (ctx.getTypes().isSameType(parameterType, holderMeta.getValueType())) {
          return;
        }
      }
      throw new AptException(
          Message.DOMA4103, holderElement, new Object[] {holderMeta.getValueType()});
    }

    private void validateFactoryMethod(HolderMeta holderMeta) {
      outer:
      for (var method : ElementFilter.methodsIn(holderElement.getEnclosedElements())) {
        if (!method.getSimpleName().contentEquals(holderMeta.getFactoryMethod())) {
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
        var parameterType = method.getParameters().get(0).asType();
        if (!ctx.getTypes().isAssignable(holderMeta.getValueType(), parameterType)) {
          continue;
        }
        if (!ctx.getTypes().isAssignable(method.getReturnType(), holderMeta.getType())) {
          continue;
        }
        var classTypeParams = holderElement.getTypeParameters();
        var methodTypeParams = method.getTypeParameters();
        if (classTypeParams.size() != methodTypeParams.size()) {
          continue;
        }
        for (Iterator<? extends TypeParameterElement> cit = classTypeParams.iterator(),
                mit = methodTypeParams.iterator();
            cit.hasNext() && mit.hasNext(); ) {
          var classTypeParam = cit.next();
          var methodTypeParam = mit.next();
          if (!ctx.getTypes().isSameType(classTypeParam.asType(), methodTypeParam.asType())) {
            continue outer;
          }
        }
        return;
      }
      throw new AptException(
          Message.DOMA4106,
          holderElement,
          new Object[] {
            holderMeta.getFactoryMethod(), holderElement.asType(), holderMeta.getValueType()
          });
    }

    @Override
    public void validateAccessorMethod(HolderMeta holderMeta) {
      var typeElement = holderElement;
      var typeMirror = holderElement.asType();
      for (; typeElement != null && typeMirror.getKind() != TypeKind.NONE; ) {
        for (var method : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
          if (!method.getSimpleName().contentEquals(holderMeta.getAccessorMethod())) {
            continue;
          }
          if (method.getModifiers().contains(Modifier.PRIVATE)) {
            continue;
          }
          if (!method.getParameters().isEmpty()) {
            continue;
          }
          var returnType = method.getReturnType();
          if (ctx.getTypes().isAssignable(returnType, holderMeta.getValueType())) {
            return;
          }
          var typeVariable = ctx.getTypes().toTypeVariable(returnType);
          if (typeVariable != null) {
            var inferredReturnType = inferType(typeVariable, typeElement, typeMirror);
            if (inferredReturnType != null) {
              if (ctx.getTypes().isAssignable(inferredReturnType, holderMeta.getValueType())) {
                return;
              }
            }
          }
        }
        typeMirror = typeElement.getSuperclass();
        typeElement = ctx.getTypes().toTypeElement(typeMirror);
      }
      throw new AptException(
          Message.DOMA4104,
          holderElement,
          new Object[] {holderMeta.getAccessorMethod(), holderMeta.getValueType()});
    }

    protected TypeMirror inferType(
        TypeVariable typeVariable, TypeElement classElement, TypeMirror classMirror) {
      var declaredType = ctx.getTypes().toDeclaredType(classMirror);
      if (declaredType == null) {
        return null;
      }
      var args = declaredType.getTypeArguments();
      if (args.isEmpty()) {
        return null;
      }
      var argsSize = args.size();
      var index = 0;
      for (TypeParameterElement typeParam : classElement.getTypeParameters()) {
        if (index >= argsSize) {
          break;
        }
        if (ctx.getTypes().isSameType(typeVariable, typeParam.asType())) {
          return args.get(index);
        }
        index++;
      }
      return null;
    }
  }

  protected class ValueStrategy extends DefaultStrategy {

    private final ValueAnnot valueAnnot;

    public ValueStrategy(ValueAnnot valueAnnot) {
      this.valueAnnot = valueAnnot;
    }

    @Override
    public void validateInitializer(HolderMeta holderMeta) {
      if (!valueAnnot.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4428,
            holderElement,
            valueAnnot.getAnnotationMirror(),
            valueAnnot.getStaticConstructor());
      }
    }

    @Override
    public void validateAccessorMethod(HolderMeta holderMeta) {
      var field = findSingleField(holderMeta);
      var accessorMethod = inferAccessorMethod(field);
      if (!accessorMethod.equals(holderMeta.getAccessorMethod())) {
        var holderAnnot = holderMeta.getHolderAnnot();
        throw new AptException(
            Message.DOMA4429,
            holderElement,
            holderAnnot.getAnnotationMirror(),
            holderAnnot.getAccessorMethod(),
            new Object[] {accessorMethod, holderMeta.getAccessorMethod()});
      }
    }

    private String inferAccessorMethod(VariableElement field) {
      var name = field.getSimpleName().toString();
      var capitalizedName = StringUtil.capitalize(name);
      if (field.asType().getKind() == TypeKind.BOOLEAN) {
        if (name.startsWith("is") && (name.length() > 2 && Character.isUpperCase(name.charAt(2)))) {
          return name;
        }
        return "is" + capitalizedName;
      }
      return "get" + capitalizedName;
    }

    private VariableElement findSingleField(HolderMeta holderMeta) {
      var fields =
          ElementFilter.fieldsIn(holderElement.getEnclosedElements())
              .stream()
              .filter(field -> !field.getModifiers().contains(Modifier.STATIC))
              .collect(Collectors.toList());
      if (fields.size() == 0) {
        throw new AptException(Message.DOMA4430, holderElement);
      }
      if (fields.size() > 1) {
        throw new AptException(Message.DOMA4431, holderElement);
      }
      var field = fields.get(0);
      if (!ctx.getTypes().isAssignable(field.asType(), holderMeta.getValueType())) {
        throw new AptException(
            Message.DOMA4432, field, new Object[] {field.asType(), holderMeta.getValueType()});
      }
      return field;
    }
  }
}
