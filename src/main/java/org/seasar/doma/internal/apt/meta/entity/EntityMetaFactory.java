package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Entity;
import org.seasar.doma.EntityField;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AllArgsConstructorAnnot;
import org.seasar.doma.internal.apt.annot.EntityAnnot;
import org.seasar.doma.internal.apt.annot.ValueAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.NullEntityListener;
import org.seasar.doma.message.Message;

public class EntityMetaFactory implements TypeElementMetaFactory<EntityMeta> {

  private final Context ctx;

  private final TypeElement entityElement;

  private final EntityAnnot entityAnnot;

  private boolean error;

  public EntityMetaFactory(Context ctx, TypeElement entityElement) {
    assertNotNull(ctx, entityElement);
    this.ctx = ctx;
    this.entityElement = entityElement;
    entityAnnot = ctx.getAnnots().newEntityAnnot(entityElement);
    if (entityAnnot == null) {
      throw new AptIllegalStateException("entityAnnot.");
    }
  }

  @Override
  public EntityMeta createTypeElementMeta() {
    var entityMeta = new EntityMeta(entityAnnot, entityElement);
    entityMeta.setNamingType(resolveNamingType());
    entityMeta.setImmutable(resolveImmutable());
    entityMeta.setEntityTypeName(ctx.getTypes().getTypeName(entityElement.asType()));

    var strategy = createStrategy(entityMeta);
    strategy.doClass(entityMeta);
    strategy.doFields(entityMeta);
    strategy.validateGeneratedId(entityMeta);
    strategy.validateOriginalStates(entityMeta);
    strategy.doConstructor(entityMeta);
    return error ? null : entityMeta;
  }

  private Strategy createStrategy(EntityMeta entityMeta) {
    var valueAnnot = ctx.getAnnots().newValueAnnot(entityElement);
    if (valueAnnot != null) {
      return new ValueStrategy(valueAnnot);
    }
    var allArgsConstructorAnnot = ctx.getAnnots().newAllArgsConstructorAnnot(entityElement);
    if (allArgsConstructorAnnot != null) {
      return new AllArgsConstructorStrategy(allArgsConstructorAnnot);
    }
    return new DefaultStrategy();
  }

  private TypeMirror resolveListener() {
    return getEntityAnnotationElementValues(EntityAnnot.LISTENER)
        .map(AnnotationValueUtil::toType)
        .peek(
            t -> {
              if (t == null) {
                throw new AptIllegalStateException(EntityAnnot.LISTENER);
              }
            })
        .findFirst()
        .orElseGet(() -> ctx.getTypes().getType(NullEntityListener.class));
  }

  private NamingType resolveNamingType() {
    return getEntityAnnotationElementValues(EntityAnnot.NAMING)
        .map(AnnotationValueUtil::toEnumConstant)
        .peek(
            e -> {
              if (e == null) {
                throw new AptIllegalStateException(EntityAnnot.NAMING);
              }
            })
        .map(VariableElement::getSimpleName)
        .map(Name::toString)
        .map(NamingType::valueOf)
        .findFirst()
        .orElse(null);
  }

  private boolean resolveImmutable() {
    var values =
        getEntityAnnotationElementValues(EntityAnnot.IMMUTABLE)
            .map(AnnotationValueUtil::toBoolean)
            .peek(
                b -> {
                  if (b == null) {
                    throw new AptIllegalStateException(EntityAnnot.IMMUTABLE);
                  }
                })
            .collect(Collectors.toList());
    if (values.contains(Boolean.TRUE) && values.contains(Boolean.FALSE)) {
      throw new AptException(
          Message.DOMA4226,
          entityElement,
          entityAnnot.getAnnotationMirror(),
          entityAnnot.getImmutable());
    }
    return values.stream().findAny().orElse(false);
  }

  private Stream<AnnotationValue> getEntityAnnotationElementValues(String entityElementName) {
    return ctx.getElements()
        .hierarchy(entityElement)
        .stream()
        .map(t -> ctx.getElements().getAnnotationMirror(t, Entity.class))
        .filter(Objects::nonNull)
        .flatMap(a -> a.getElementValues().entrySet().stream())
        .filter(e -> e.getKey().getSimpleName().contentEquals(entityElementName))
        .map(Map.Entry::getValue);
  }

  private interface Strategy {

    void doClass(EntityMeta entityMeta);

    void doFields(EntityMeta entityMeta);

    void validateGeneratedId(EntityMeta entityMeta);

    void validateOriginalStates(EntityMeta entityMeta);

    void doConstructor(EntityMeta entityMeta);
  }

  private class DefaultStrategy implements Strategy {

    public void doClass(EntityMeta entityMeta) {
      validateClass(entityMeta);

      doEntityListener(entityMeta);
      doTable(entityMeta);
    }

    protected void validateClass(EntityMeta entityMeta) {
      if (entityElement.getKind() != ElementKind.CLASS) {
        throw new AptException(Message.DOMA4015, entityElement, entityAnnot.getAnnotationMirror());
      }
      if (!entityElement.getTypeParameters().isEmpty()) {
        throw new AptException(Message.DOMA4051, entityElement);
      }
      validateEnclosingElement(entityElement);
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
            Message.DOMA4317, typeElement, new Object[] {typeElement.getQualifiedName()});
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
              Message.DOMA4315, typeElement, new Object[] {typeElement.getQualifiedName()});
        }
      } else {
        throw new AptException(
            Message.DOMA4316, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    }

    private void doEntityListener(EntityMeta entityMeta) {
      var listenerType = entityAnnot.getListenerValue();
      var listenerElement = ctx.getTypes().toTypeElement(listenerType);
      if (listenerElement == null) {
        throw new AptIllegalStateException("listenerElement");
      }
      validateListener(listenerElement);

      var inheritedListenerType = resolveListener();
      var inheritedListenerElement = ctx.getTypes().toTypeElement(inheritedListenerType);
      if (inheritedListenerElement == null) {
        throw new AptIllegalStateException("inheritedListenerElement");
      }
      if (!ctx.getTypes().isSameType(listenerType, inheritedListenerType)) {
        validateInheritedListener(entityMeta, inheritedListenerElement);
      }

      entityMeta.setListenerElement(inheritedListenerElement);
    }

    private void validateListener(TypeElement listenerElement) {
      if (listenerElement.getModifiers().contains(Modifier.ABSTRACT)) {
        throw new AptException(
            Message.DOMA4166,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {listenerElement.getQualifiedName()});
      }

      var constructor = ctx.getElements().getNoArgConstructor(listenerElement);
      if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
        throw new AptException(
            Message.DOMA4167,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {listenerElement.getQualifiedName()});
      }
      if (listenerElement.getTypeParameters().size() > 0) {
        validateGenericEntityListener(listenerElement);
      } else {
        validateNonGenericEntityListener(listenerElement);
      }
    }

    private void validateGenericEntityListener(TypeElement listenerElement) {
      var typeParams = listenerElement.getTypeParameters();
      if (typeParams.size() == 0) {
        throw new AptIllegalStateException("typeParams size should be more than 0");
      }
      if (typeParams.size() > 1) {
        throw new AptException(
            Message.DOMA4227,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener());
      }
      var typeParam = typeParams.get(0);
      for (TypeMirror bound : typeParam.getBounds()) {
        if (!ctx.getTypes().isAssignable(entityElement.asType(), bound)) {
          throw new AptException(
              Message.DOMA4229,
              entityElement,
              entityAnnot.getAnnotationMirror(),
              entityAnnot.getListener(),
              new Object[] {typeParam.getSimpleName(), bound, entityElement.getQualifiedName()});
        }
      }
      if (findListenerTypeParam(listenerElement, 0) == null) {
        throw new AptException(
            Message.DOMA4228,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {typeParam.getSimpleName()});
      }
    }

    private TypeParameterElement findListenerTypeParam(
        TypeElement listenerElement, int typeParamIndex) {
      var typeParam = listenerElement.getTypeParameters().get(typeParamIndex);

      for (TypeMirror iface : listenerElement.getInterfaces()) {
        var declaredType = ctx.getTypes().toDeclaredType(iface);
        if (declaredType == null) {
          continue;
        }
        var i = -1;
        for (TypeMirror typeArg : declaredType.getTypeArguments()) {
          i++;
          var typeVariable = ctx.getTypes().toTypeVariable(typeArg);
          if (typeVariable == null) {
            continue;
          }
          if (typeParam.getSimpleName().equals(typeVariable.asElement().getSimpleName())) {
            if (ctx.getTypes().isSameType(declaredType, EntityListener.class)) {
              return typeParam;
            }
            var typeElement = ctx.getTypes().toTypeElement(declaredType);
            if (typeElement == null) {
              throw new AptIllegalStateException(declaredType.toString());
            }
            var candidate = findListenerTypeParam(typeElement, i);
            if (candidate != null) {
              return candidate;
            }
          }
        }
      }

      var superclass = listenerElement.getSuperclass();
      var declaredType = ctx.getTypes().toDeclaredType(superclass);
      if (declaredType == null) {
        return null;
      }
      var i = -1;
      for (TypeMirror typeArg : declaredType.getTypeArguments()) {
        i++;
        var typeVariable = ctx.getTypes().toTypeVariable(typeArg);
        if (typeVariable == null) {
          continue;
        }
        if (typeParam.getSimpleName().equals(typeVariable.asElement().getSimpleName())) {
          if (ctx.getTypes().isSameType(declaredType, EntityListener.class)) {
            return typeParam;
          }
          var typeElement = ctx.getTypes().toTypeElement(declaredType);
          if (typeElement == null) {
            throw new AptIllegalStateException(declaredType.toString());
          }
          var candidate = findListenerTypeParam(typeElement, i);
          if (candidate != null) {
            return candidate;
          }
        }
      }

      return null;
    }

    private void validateNonGenericEntityListener(TypeElement listenerElement) {
      var listenerType = listenerElement.asType();
      var argumentType = getListenerArgumentType(listenerType);
      if (argumentType == null) {
        throw new AptException(
            Message.DOMA4202,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener());
      }
      if (!ctx.getTypes().isAssignable(entityElement.asType(), argumentType)) {
        throw new AptException(
            Message.DOMA4038,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {listenerType, argumentType, entityElement.getQualifiedName()});
      }
    }

    private void validateInheritedListener(
        EntityMeta entityMeta, TypeElement inheritedListenerElement) {
      var typeParams = inheritedListenerElement.getTypeParameters();
      if (typeParams.size() == 0) {
        throw new AptException(
            Message.DOMA4230,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            new Object[] {
              inheritedListenerElement.getQualifiedName(), entityElement.getQualifiedName()
            });
      }
      var typeParam = typeParams.get(0);
      for (TypeMirror bound : typeParam.getBounds()) {
        if (!ctx.getTypes().isAssignable(entityElement.asType(), bound)) {
          throw new AptException(
              Message.DOMA4231,
              entityElement,
              entityAnnot.getAnnotationMirror(),
              new Object[] {
                inheritedListenerElement.getQualifiedName(),
                typeParam.getSimpleName(),
                bound,
                entityElement.getQualifiedName()
              });
        }
      }
    }

    private TypeMirror getListenerArgumentType(TypeMirror typeMirror) {
      for (TypeMirror supertype : ctx.getTypes().directSupertypes(typeMirror)) {
        if (!ctx.getTypes().isAssignable(supertype, EntityListener.class)) {
          continue;
        }
        if (ctx.getTypes().isSameType(supertype, EntityListener.class)) {
          var declaredType = ctx.getTypes().toDeclaredType(supertype);
          if (declaredType == null) {
            throw new AptIllegalStateException("declaredType");
          }
          var args = declaredType.getTypeArguments();
          if (args.size() != 1) {
            return null;
          }
          return args.get(0);
        }
        var argumentType = getListenerArgumentType(supertype);
        if (argumentType != null) {
          return argumentType;
        }
      }
      return null;
    }

    private void doTable(EntityMeta entityMeta) {
      var tableAnnot = ctx.getAnnots().newTableAnnot(entityElement);
      if (tableAnnot == null) {
        return;
      }
      entityMeta.setTableMirror(tableAnnot);
    }

    public void doFields(EntityMeta entityMeta) {
      for (var fieldElement : getFields()) {
        try {
          if (fieldElement.getAnnotation(Transient.class) != null) {
            //noinspection UnnecessaryContinue
            continue;
          } else if (fieldElement.getModifiers().contains(Modifier.STATIC)) {
            //noinspection UnnecessaryContinue
            continue;
          } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
            doOriginalStatesField(fieldElement, entityMeta);
          } else {
            doEntityPropertyMeta(fieldElement, entityMeta);
          }
        } catch (AptException e) {
          ctx.getNotifier().send(e);
          error = true;
        }
      }
    }

    private List<VariableElement> getFields() {
      return ctx.getElements()
          .getUnhiddenFields(entityElement, t -> t.getAnnotation(Entity.class) != null);
    }

    private void doOriginalStatesField(VariableElement fieldElement, EntityMeta entityMeta) {
      if (entityMeta.hasOriginalStatesMeta()) {
        throw new AptException(Message.DOMA4125, fieldElement);
      }
      if (entityElement.equals(fieldElement.getEnclosingElement())) {
        if (!ctx.getTypes().isSameType(fieldElement.asType(), entityElement.asType())) {
          throw new AptException(Message.DOMA4135, fieldElement);
        }
      }
      var enclosingElement = ctx.getElements().toTypeElement(fieldElement.getEnclosingElement());
      if (enclosingElement == null) {
        throw new AptIllegalStateException(fieldElement.toString());
      }
      if (entityMeta.isImmutable() && entityElement.equals(enclosingElement)) {
        throw new AptException(Message.DOMA4224, fieldElement);
      }
      var originalStatesMeta =
          new OriginalStatesMeta(entityElement, fieldElement, enclosingElement);
      entityMeta.setOriginalStatesMeta(originalStatesMeta);
    }

    private void doEntityPropertyMeta(VariableElement fieldElement, EntityMeta entityMeta) {
      validateFieldAnnotation(fieldElement, entityMeta);
      var propertyMetaFactory = new EntityPropertyMetaFactory(ctx, fieldElement);
      var propertyMeta = propertyMetaFactory.createEntityPropertyMeta();
      if (propertyMeta.getIdGeneratorMeta() != null && entityMeta.hasGeneratedIdPropertyMeta()) {
        throw new AptException(Message.DOMA4037, fieldElement);
      }
      if (propertyMeta.isVersion() && entityMeta.hasVersionPropertyMeta()) {
        throw new AptException(Message.DOMA4024, fieldElement);
      }
      entityMeta.addPropertyMeta(propertyMeta);
      validateField(fieldElement, entityMeta);
    }

    private void validateFieldAnnotation(VariableElement fieldElement, EntityMeta entityMeta) {
      TypeElement foundAnnotationTypeElement = null;
      for (AnnotationMirror annotation : fieldElement.getAnnotationMirrors()) {
        var declaredType = annotation.getAnnotationType();
        var typeElement = ctx.getTypes().toTypeElement(declaredType);
        if (typeElement.getAnnotation(EntityField.class) != null) {
          if (foundAnnotationTypeElement != null) {
            throw new AptException(
                Message.DOMA4086,
                fieldElement,
                new Object[] {
                  foundAnnotationTypeElement.getQualifiedName(), typeElement.getQualifiedName()
                });
          }
          foundAnnotationTypeElement = typeElement;
        }
      }
    }

    protected void validateField(VariableElement fieldElement, EntityMeta entityMeta) {
      if (entityMeta.isImmutable() && !fieldElement.getModifiers().contains(Modifier.FINAL)) {
        throw new AptException(Message.DOMA4225, fieldElement);
      }
    }

    public void validateGeneratedId(EntityMeta entityMeta) {
      if (entityMeta.hasGeneratedIdPropertyMeta() && entityMeta.getIdPropertyMetas().size() > 1) {
        throw new AptException(Message.DOMA4036, entityElement);
      }
    }

    public void validateOriginalStates(EntityMeta entityMeta) {
      if (entityMeta.hasOriginalStatesMeta() && entityMeta.hasEmbeddedProperties()) {
        throw new AptException(Message.DOMA4305, entityElement);
      }
    }

    public void doConstructor(EntityMeta entityMeta) {
      if (entityElement.getModifiers().contains(Modifier.ABSTRACT)) {
        return;
      }
      if (entityMeta.isImmutable()) {
        var constructorMeta = getConstructorMeta(entityMeta);
        if (constructorMeta == null) {
          throw new AptException(Message.DOMA4281, entityElement);
        }
        if (constructorMeta.getConstructorElement().getModifiers().contains(Modifier.PRIVATE)) {
          throw new AptException(Message.DOMA4221, entityElement);
        }
      } else {
        var constructor = ctx.getElements().getNoArgConstructor(entityElement);
        if (constructor == null || constructor.getModifiers().contains(Modifier.PRIVATE)) {
          throw new AptException(Message.DOMA4124, entityElement);
        }
      }
    }

    private EntityConstructorMeta getConstructorMeta(EntityMeta entityMeta) {
      Map<String, EntityPropertyMeta> entityPropertyMetaMap = new HashMap<>();
      for (var propertyMeta : entityMeta.getAllPropertyMetas()) {
        entityPropertyMetaMap.put(propertyMeta.getName(), propertyMeta);
      }
      outer:
      for (var constructor : ElementFilter.constructorsIn(entityElement.getEnclosedElements())) {
        List<EntityPropertyMeta> entityPropertyMetaList = new ArrayList<>();
        for (VariableElement param : constructor.getParameters()) {
          var name = ctx.getElements().getParameterName(param);
          var paramType = param.asType();
          var propertyMeta = entityPropertyMetaMap.get(name);
          if (propertyMeta == null) {
            continue outer;
          }
          var propertyType = propertyMeta.getType();
          if (!ctx.getTypes().isSameType(paramType, propertyType)) {
            continue outer;
          }
          entityPropertyMetaList.add(propertyMeta);
        }
        if (entityPropertyMetaMap.size() == entityPropertyMetaList.size()) {
          return new EntityConstructorMeta(constructor, entityPropertyMetaList);
        }
      }
      return null;
    }
  }

  private class AllArgsConstructorStrategy extends DefaultStrategy {

    private final AllArgsConstructorAnnot allArgsConstructorAnnot;

    public AllArgsConstructorStrategy(AllArgsConstructorAnnot allArgsConstructorAnnot) {
      assertNotNull(allArgsConstructorAnnot);
      this.allArgsConstructorAnnot = allArgsConstructorAnnot;
    }

    @Override
    protected void validateClass(EntityMeta entityMeta) {
      if (!entityMeta.isImmutable()) {
        throw new AptException(
            Message.DOMA4420, entityElement, allArgsConstructorAnnot.getAnnotationMirror());
      }
      super.validateClass(entityMeta);
    }

    @Override
    public void doConstructor(EntityMeta entityMeta) {
      if (!allArgsConstructorAnnot.getStaticNameValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4421,
            entityElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getStaticName());
      }
      if (allArgsConstructorAnnot.isAccessPrivate()) {
        throw new AptException(
            Message.DOMA4422,
            entityElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess());
      }
      if (allArgsConstructorAnnot.isAccessNone()) {
        throw new AptException(
            Message.DOMA4426,
            entityElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess());
      }
    }

    @Override
    protected void validateField(VariableElement fieldElement, EntityMeta entityMeta) {
      // doNothing
    }
  }

  private class ValueStrategy extends DefaultStrategy {

    private final ValueAnnot valueAnnot;

    public ValueStrategy(ValueAnnot valueAnnot) {
      assertNotNull(valueAnnot);
      this.valueAnnot = valueAnnot;
    }

    @Override
    protected void validateClass(EntityMeta entityMeta) {
      if (!entityMeta.isImmutable()) {
        throw new AptException(
            Message.DOMA4418,
            entityElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getImmutable());
      }
      super.validateClass(entityMeta);
    }

    @Override
    public void doConstructor(EntityMeta entityMeta) {
      if (!valueAnnot.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4419,
            entityElement,
            valueAnnot.getAnnotationMirror(),
            valueAnnot.getStaticConstructor());
      }
    }

    @Override
    protected void validateField(VariableElement fieldElement, EntityMeta entityMeta) {
      // doNothing
    }
  }
}
