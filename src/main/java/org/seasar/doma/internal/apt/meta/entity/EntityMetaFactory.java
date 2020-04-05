package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
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
import org.seasar.doma.Entity;
import org.seasar.doma.EntityField;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.ParameterName;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.AllArgsConstructorAnnot;
import org.seasar.doma.internal.apt.annot.EntityAnnot;
import org.seasar.doma.internal.apt.annot.TableAnnot;
import org.seasar.doma.internal.apt.annot.ValueAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementKindUtil;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.NullEntityListener;
import org.seasar.doma.message.Message;

public class EntityMetaFactory implements TypeElementMetaFactory<EntityMeta> {

  private final Context ctx;

  public EntityMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public EntityMeta createTypeElementMeta(TypeElement classElement) {
    assertNotNull(classElement);
    EntityAnnot entityAnnot = ctx.getAnnotations().newEntityAnnot(classElement);
    if (entityAnnot == null) {
      throw new AptIllegalStateException("entityAnnot.");
    }
    EntityMeta entityMeta = new EntityMeta(entityAnnot, classElement);
    TypeMirror entityListener = resolveEntityListener(classElement);
    TypeElement entityListenerElement = ctx.getMoreTypes().toTypeElement(entityListener);
    if (entityListenerElement == null) {
      throw new AptIllegalStateException("entityListener.");
    }
    entityMeta.setEntityListenerElement(entityListenerElement);
    entityMeta.setGenericEntityListener(!entityListenerElement.getTypeParameters().isEmpty());
    NamingType namingType = resolveNamingType(classElement);
    entityMeta.setNamingType(namingType);
    boolean immutable = resolveImmutable(classElement, entityAnnot);
    entityMeta.setImmutable(immutable);
    entityMeta.setEntityName(classElement.getSimpleName().toString());
    Strategy strategy = createStrategy(classElement, entityMeta);
    strategy.doClassElement(classElement, entityMeta);
    strategy.doFieldElements(classElement, entityMeta);
    strategy.validateGeneratedId(classElement, entityMeta);
    strategy.validateOriginalStates(classElement, entityMeta);
    strategy.doConstructor(classElement, entityMeta);
    return entityMeta;
  }

  private Strategy createStrategy(TypeElement classElement, EntityMeta entityMeta) {
    ValueAnnot valueAnnot = ctx.getAnnotations().newValueAnnot(classElement);
    if (valueAnnot != null) {
      return new ValueStrategy(ctx, valueAnnot);
    }
    AllArgsConstructorAnnot allArgsConstructorAnnot =
        ctx.getAnnotations().newAllArgsConstructorAnnot(classElement);
    if (allArgsConstructorAnnot != null) {
      return new AllArgsConstructorStrategy(ctx, allArgsConstructorAnnot);
    }
    return new DefaultStrategy(ctx);
  }

  private TypeMirror resolveEntityListener(TypeElement classElement) {
    TypeMirror result = ctx.getMoreTypes().getTypeMirror(NullEntityListener.class);
    for (AnnotationValue value : getEntityElementValueList(classElement, "listener")) {
      if (value != null) {
        TypeMirror listenerType = AnnotationValueUtil.toType(value);
        if (listenerType == null) {
          throw new AptIllegalStateException("listener");
        }
        result = listenerType;
      }
    }
    return result;
  }

  private NamingType resolveNamingType(TypeElement classElement) {
    NamingType result = null;
    for (AnnotationValue value : getEntityElementValueList(classElement, "naming")) {
      if (value != null) {
        VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(value);
        if (enumConstant == null) {
          throw new AptIllegalStateException("naming");
        }
        result = NamingType.valueOf(enumConstant.getSimpleName().toString());
      }
    }
    return result;
  }

  private boolean resolveImmutable(TypeElement classElement, EntityAnnot entityAnnot) {
    if (ElementKindUtil.isRecord(classElement.getKind())) {
      return true;
    }
    boolean result = false;
    List<Boolean> resolvedList = new ArrayList<>();
    for (AnnotationValue value : getEntityElementValueList(classElement, "immutable")) {
      if (value != null) {
        Boolean immutable = AnnotationValueUtil.toBoolean(value);
        if (immutable == null) {
          throw new AptIllegalStateException("immutable");
        }
        result = immutable;
        resolvedList.add(immutable);
      }
    }
    if (resolvedList.contains(Boolean.TRUE) && resolvedList.contains(Boolean.FALSE)) {
      throw new AptException(
          Message.DOMA4226,
          classElement,
          entityAnnot.getAnnotationMirror(),
          entityAnnot.getImmutable(),
          new Object[] {});
    }
    return result;
  }

  private List<AnnotationValue> getEntityElementValueList(
      TypeElement classElement, String entityElementName) {
    List<AnnotationValue> list = new LinkedList<>();
    for (TypeElement t = classElement;
        t != null && t.asType().getKind() != TypeKind.NONE;
        t = ctx.getMoreTypes().toTypeElement(t.getSuperclass())) {
      AnnotationMirror annMirror = ctx.getMoreElements().getAnnotationMirror(t, Entity.class);
      if (annMirror == null) {
        continue;
      }
      AnnotationValue value = null;
      for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
          annMirror.getElementValues().entrySet()) {
        ExecutableElement element = entry.getKey();
        if (entityElementName.equals(element.getSimpleName().toString())) {
          value = entry.getValue();
          break;
        }
      }
      list.add(value);
    }
    Collections.reverse(list);
    return list;
  }

  interface Strategy {

    void doClassElement(TypeElement classElement, EntityMeta entityMeta);

    void doFieldElements(TypeElement classElement, EntityMeta entityMeta);

    void validateGeneratedId(TypeElement classElement, EntityMeta entityMeta);

    void validateOriginalStates(TypeElement classElement, EntityMeta entityMeta);

    void doConstructor(TypeElement classElement, EntityMeta entityMeta);
  }

  protected static class DefaultStrategy implements Strategy {

    final Context ctx;

    DefaultStrategy(Context ctx) {
      assertNotNull(ctx);
      this.ctx = ctx;
    }

    public void doClassElement(TypeElement classElement, EntityMeta entityMeta) {
      validateClass(classElement, entityMeta);
      validateEntityListener(classElement, entityMeta);

      doTable(classElement, entityMeta);
    }

    void validateClass(TypeElement classElement, EntityMeta entityMeta) {
      EntityAnnot entityAnnot = entityMeta.getEntityAnnot();
      ElementKind kind = classElement.getKind();
      if (kind != ElementKind.CLASS && !ElementKindUtil.isRecord(kind)) {
        throw new AptException(
            Message.DOMA4015, classElement, entityAnnot.getAnnotationMirror(), new Object[] {});
      }
      if (!classElement.getTypeParameters().isEmpty()) {
        throw new AptException(Message.DOMA4051, classElement, new Object[] {});
      }
      validateEnclosingElement(classElement);
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
            Message.DOMA4317, typeElement, new Object[] {typeElement.getQualifiedName()});
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
              Message.DOMA4315, typeElement, new Object[] {typeElement.getQualifiedName()});
        }
      } else {
        throw new AptException(
            Message.DOMA4316, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    }

    void validateEntityListener(TypeElement classElement, EntityMeta entityMeta) {
      EntityAnnot entityAnnot = entityMeta.getEntityAnnot();
      TypeMirror listenerType = entityAnnot.getListenerValue();
      TypeElement listenerElement = ctx.getMoreTypes().toTypeElement(listenerType);
      if (listenerElement == null) {
        throw new AptIllegalStateException("failed to convert to TypeElement");
      }

      if (listenerElement.getModifiers().contains(Modifier.ABSTRACT)) {
        throw new AptException(
            Message.DOMA4166,
            classElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {listenerElement.getQualifiedName()});
      }

      ExecutableElement constructor = ctx.getMoreElements().getNoArgConstructor(listenerElement);
      if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
        throw new AptException(
            Message.DOMA4167,
            classElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {listenerElement.getQualifiedName()});
      }
      if (listenerElement.getTypeParameters().size() > 0) {
        validateGenericEntityListener(classElement, entityMeta, listenerElement);
      } else {
        validateNonGenericEntityListener(classElement, entityMeta, listenerType);
      }

      TypeElement inheritedListenerElement = entityMeta.getEntityListenerElement();
      if (!ctx.getMoreTypes()
          .isSameTypeWithErasure(listenerType, inheritedListenerElement.asType())) {
        validateInheritedEntityListener(classElement, entityMeta, inheritedListenerElement);
      }
    }

    void validateGenericEntityListener(
        TypeElement classElement, EntityMeta entityMeta, TypeElement listenerElement) {
      EntityAnnot entityAnnot = entityMeta.getEntityAnnot();
      List<? extends TypeParameterElement> typeParams = listenerElement.getTypeParameters();
      if (typeParams.size() == 0) {
        throw new AptIllegalStateException("typeParams size should be more than 0");
      }
      if (typeParams.size() > 1) {
        throw new AptException(
            Message.DOMA4227,
            classElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {});
      }
      TypeParameterElement typeParam = typeParams.get(0);
      for (TypeMirror bound : typeParam.getBounds()) {
        if (!ctx.getMoreTypes().isAssignableWithErasure(classElement.asType(), bound)) {
          throw new AptException(
              Message.DOMA4229,
              classElement,
              entityAnnot.getAnnotationMirror(),
              entityAnnot.getListener(),
              new Object[] {typeParam.getSimpleName(), bound, classElement.getQualifiedName()});
        }
      }
      if (findListenerTypeParam(listenerElement, 0) == null) {
        throw new AptException(
            Message.DOMA4228,
            classElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {typeParam.getSimpleName()});
      }
    }

    TypeParameterElement findListenerTypeParam(TypeElement listenerElement, int typeParamIndex) {
      TypeParameterElement typeParam = listenerElement.getTypeParameters().get(typeParamIndex);

      for (TypeMirror interfase : listenerElement.getInterfaces()) {
        DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(interfase);
        if (declaredType == null) {
          continue;
        }
        int i = -1;
        for (TypeMirror typeArg : declaredType.getTypeArguments()) {
          i++;
          TypeVariable typeVariable = ctx.getMoreTypes().toTypeVariable(typeArg);
          if (typeVariable == null) {
            continue;
          }
          if (typeParam.getSimpleName().equals(typeVariable.asElement().getSimpleName())) {
            if (ctx.getMoreTypes().isSameTypeWithErasure(declaredType, EntityListener.class)) {
              return typeParam;
            }
            TypeElement typeElement = ctx.getMoreTypes().toTypeElement(declaredType);
            if (typeElement == null) {
              throw new AptIllegalStateException(declaredType.toString());
            }
            TypeParameterElement candidate = findListenerTypeParam(typeElement, i);
            if (candidate != null) {
              return candidate;
            }
          }
        }
      }

      TypeMirror superclass = listenerElement.getSuperclass();
      DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(superclass);
      if (declaredType == null) {
        return null;
      }
      int i = -1;
      for (TypeMirror typeArg : declaredType.getTypeArguments()) {
        i++;
        TypeVariable typeVariable = ctx.getMoreTypes().toTypeVariable(typeArg);
        if (typeVariable == null) {
          continue;
        }
        if (typeParam.getSimpleName().equals(typeVariable.asElement().getSimpleName())) {
          if (ctx.getMoreTypes().isSameTypeWithErasure(declaredType, EntityListener.class)) {
            return typeParam;
          }
          TypeElement typeElement = ctx.getMoreTypes().toTypeElement(declaredType);
          if (typeElement == null) {
            throw new AptIllegalStateException(declaredType.toString());
          }
          TypeParameterElement candidate = findListenerTypeParam(typeElement, i);
          if (candidate != null) {
            return candidate;
          }
        }
      }

      return null;
    }

    void validateNonGenericEntityListener(
        TypeElement classElement, EntityMeta entityMeta, TypeMirror listenerType) {
      EntityAnnot entityAnnot = entityMeta.getEntityAnnot();
      TypeMirror argumentType = getListenerArgumentType(listenerType);
      if (argumentType == null) {
        throw new AptException(
            Message.DOMA4202,
            classElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {});
      }
      if (!ctx.getMoreTypes().isAssignableWithErasure(classElement.asType(), argumentType)) {
        throw new AptException(
            Message.DOMA4038,
            classElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getListener(),
            new Object[] {listenerType, argumentType, classElement.getQualifiedName()});
      }
    }

    void validateInheritedEntityListener(
        TypeElement classElement, EntityMeta entityMeta, TypeElement inheritedListenerElement) {
      EntityAnnot entityAnnot = entityMeta.getEntityAnnot();
      List<? extends TypeParameterElement> typeParams =
          inheritedListenerElement.getTypeParameters();
      if (typeParams.size() == 0) {
        throw new AptException(
            Message.DOMA4230,
            classElement,
            entityAnnot.getAnnotationMirror(),
            new Object[] {
              inheritedListenerElement.getQualifiedName(), classElement.getQualifiedName()
            });
      }
      TypeParameterElement typeParam = typeParams.get(0);
      for (TypeMirror bound : typeParam.getBounds()) {
        if (!ctx.getMoreTypes().isAssignableWithErasure(classElement.asType(), bound)) {
          throw new AptException(
              Message.DOMA4231,
              classElement,
              entityAnnot.getAnnotationMirror(),
              new Object[] {
                inheritedListenerElement.getQualifiedName(),
                typeParam.getSimpleName(),
                bound,
                classElement.getQualifiedName()
              });
        }
      }
    }

    TypeMirror getListenerArgumentType(TypeMirror typeMirror) {
      for (TypeMirror supertype : ctx.getMoreTypes().directSupertypes(typeMirror)) {
        if (!ctx.getMoreTypes().isAssignableWithErasure(supertype, EntityListener.class)) {
          continue;
        }
        if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, EntityListener.class)) {
          DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(supertype);
          if (declaredType == null) {
            throw new AptIllegalStateException("declaredType");
          }
          List<? extends TypeMirror> args = declaredType.getTypeArguments();
          if (args.size() != 1) {
            return null;
          }
          return args.get(0);
        }
        TypeMirror argumentType = getListenerArgumentType(supertype);
        if (argumentType != null) {
          return argumentType;
        }
      }
      return null;
    }

    void doTable(TypeElement classElement, EntityMeta entityMeta) {
      TableAnnot tableAnnot = ctx.getAnnotations().newTableAnnot(classElement);
      if (tableAnnot == null) {
        return;
      }
      entityMeta.setTableAnnot(tableAnnot);
    }

    public void doFieldElements(TypeElement classElement, EntityMeta entityMeta) {
      for (VariableElement fieldElement : getFieldElements(classElement)) {
        try {
          if (fieldElement.getAnnotation(Transient.class) != null) {
            continue;
          } else if (fieldElement.getModifiers().contains(Modifier.STATIC)) {
            continue;
          } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
            doOriginalStatesField(classElement, fieldElement, entityMeta);
          } else {
            doEntityPropertyMeta(classElement, fieldElement, entityMeta);
          }
        } catch (AptException e) {
          ctx.getReporter().report(e);
          entityMeta.setError(true);
        }
      }
    }

    List<VariableElement> getFieldElements(TypeElement classElement) {
      List<VariableElement> results = new LinkedList<>();
      for (TypeElement t = classElement;
          t != null && t.asType().getKind() != TypeKind.NONE;
          t = ctx.getMoreTypes().toTypeElement(t.getSuperclass())) {
        if (t.getAnnotation(Entity.class) == null) {
          continue;
        }
        List<VariableElement> fields =
            new LinkedList<>(ElementFilter.fieldsIn(t.getEnclosedElements()));
        Collections.reverse(fields);
        results.addAll(fields);
      }
      Collections.reverse(results);

      List<VariableElement> hiderFields = new LinkedList<>(results);
      for (Iterator<VariableElement> it = results.iterator(); it.hasNext(); ) {
        VariableElement hidden = it.next();
        for (VariableElement hider : hiderFields) {
          if (ctx.getMoreElements().hides(hider, hidden)) {
            it.remove();
          }
        }
      }
      return results;
    }

    void doOriginalStatesField(
        TypeElement classElement, VariableElement fieldElement, EntityMeta entityMeta) {
      if (entityMeta.hasOriginalStatesMeta()) {
        throw new AptException(Message.DOMA4125, fieldElement, new Object[] {});
      }
      if (classElement.equals(fieldElement.getEnclosingElement())) {
        if (!ctx.getMoreTypes()
            .isSameTypeWithErasure(fieldElement.asType(), classElement.asType())) {
          throw new AptException(Message.DOMA4135, fieldElement, new Object[] {});
        }
      }
      TypeElement enclosingElement =
          ctx.getMoreElements().toTypeElement(fieldElement.getEnclosingElement());
      if (enclosingElement == null) {
        throw new AptIllegalStateException(fieldElement.toString());
      }
      if (entityMeta.isImmutable() && classElement.equals(enclosingElement)) {
        throw new AptException(Message.DOMA4224, fieldElement, new Object[] {});
      }
      OriginalStatesMeta originalStatesMeta =
          new OriginalStatesMeta(classElement, fieldElement, enclosingElement);
      entityMeta.setOriginalStatesMeta(originalStatesMeta);
    }

    void doEntityPropertyMeta(
        TypeElement classElement, VariableElement fieldElement, EntityMeta entityMeta) {
      validateFieldAnnotation(fieldElement, entityMeta);
      EntityPropertyMetaFactory propertyMetaFactory =
          new EntityPropertyMetaFactory(ctx, entityMeta, fieldElement);
      EntityPropertyMeta propertyMeta = propertyMetaFactory.createEntityPropertyMeta();
      entityMeta.addPropertyMeta(propertyMeta);
      validateField(classElement, fieldElement, entityMeta);
    }

    void validateFieldAnnotation(VariableElement fieldElement, EntityMeta entityMeta) {
      TypeElement foundAnnotationTypeElement = null;
      for (AnnotationMirror annotation : fieldElement.getAnnotationMirrors()) {
        DeclaredType declaredType = annotation.getAnnotationType();
        TypeElement typeElement = ctx.getMoreTypes().toTypeElement(declaredType);
        if (typeElement.getAnnotation(EntityField.class) != null) {
          if (foundAnnotationTypeElement != null) {
            throw new AptException(
                Message.DOMA4086,
                fieldElement,
                new Object[] {
                  foundAnnotationTypeElement.getQualifiedName(), typeElement.getQualifiedName(),
                });
          }
          foundAnnotationTypeElement = typeElement;
        }
      }
    }

    void validateField(
        TypeElement classElement, VariableElement fieldElement, EntityMeta entityMeta) {
      if (entityMeta.isImmutable() && !fieldElement.getModifiers().contains(Modifier.FINAL)) {
        throw new AptException(Message.DOMA4225, fieldElement, new Object[] {});
      }
    }

    public void validateGeneratedId(TypeElement classElement, EntityMeta entityMeta) {
      if (entityMeta.hasGeneratedIdPropertyMeta() && entityMeta.getIdPropertyMetas().size() > 1) {
        throw new AptException(Message.DOMA4036, classElement, new Object[] {});
      }
    }

    public void validateOriginalStates(TypeElement classElement, EntityMeta entityMeta) {
      if (entityMeta.hasOriginalStatesMeta() && entityMeta.hasEmbeddedProperties()) {
        throw new AptException(Message.DOMA4305, classElement, new Object[] {});
      }
    }

    public void doConstructor(TypeElement classElement, EntityMeta entityMeta) {
      if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
        return;
      }
      if (entityMeta.isImmutable()) {
        EntityConstructorMeta constructorMeta = getConstructorMeta(classElement, entityMeta);
        if (constructorMeta == null) {
          throw new AptException(Message.DOMA4281, classElement, new Object[] {});
        }
        if (constructorMeta.getConstructorElement().getModifiers().contains(Modifier.PRIVATE)) {
          throw new AptException(Message.DOMA4221, classElement, new Object[] {});
        }
        entityMeta.setConstructorMeta(constructorMeta);
      } else {
        ExecutableElement constructor = ctx.getMoreElements().getNoArgConstructor(classElement);
        if (constructor == null || constructor.getModifiers().contains(Modifier.PRIVATE)) {
          throw new AptException(Message.DOMA4124, classElement, new Object[] {});
        }
      }
    }

    EntityConstructorMeta getConstructorMeta(TypeElement classElement, EntityMeta entityMeta) {
      Map<String, EntityPropertyMeta> entityPropertyMetaMap = new HashMap<>();
      for (EntityPropertyMeta propertyMeta : entityMeta.getAllPropertyMetas()) {
        entityPropertyMetaMap.put(propertyMeta.getName(), propertyMeta);
      }
      outer:
      for (ExecutableElement constructor :
          ElementFilter.constructorsIn(classElement.getEnclosedElements())) {
        List<EntityPropertyMeta> entityPropertyMetaList = new ArrayList<>();
        for (VariableElement param : constructor.getParameters()) {
          String name = param.getSimpleName().toString();
          ParameterName parameterName = param.getAnnotation(ParameterName.class);
          if (parameterName != null) {
            name = parameterName.value();
          }
          TypeMirror paramType = param.asType();
          EntityPropertyMeta propertyMeta = entityPropertyMetaMap.get(name);
          if (propertyMeta == null) {
            continue outer;
          }
          TypeMirror propertyType = propertyMeta.getType();
          if (!ctx.getMoreTypes().isSameTypeWithErasure(paramType, propertyType)) {
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

  protected static class AllArgsConstructorStrategy extends DefaultStrategy {

    final AllArgsConstructorAnnot allArgsConstructorAnnot;

    AllArgsConstructorStrategy(Context ctx, AllArgsConstructorAnnot allArgsConstructorAnnot) {
      super(ctx);
      assertNotNull(allArgsConstructorAnnot);
      this.allArgsConstructorAnnot = allArgsConstructorAnnot;
    }

    @Override
    protected void validateClass(TypeElement classElement, EntityMeta entityMeta) {
      if (!entityMeta.isImmutable()) {
        throw new AptException(
            Message.DOMA4420,
            classElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            new Object[] {});
      }
      super.validateClass(classElement, entityMeta);
    }

    @Override
    public void doConstructor(TypeElement classElement, EntityMeta entityMeta) {
      if (!allArgsConstructorAnnot.getStaticNameValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4421,
            classElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getStaticName(),
            new Object[] {});
      }
      if (allArgsConstructorAnnot.isAccessPrivate()) {
        throw new AptException(
            Message.DOMA4422,
            classElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess(),
            new Object[] {});
      }
      if (allArgsConstructorAnnot.isAccessNone()) {
        throw new AptException(
            Message.DOMA4426,
            classElement,
            allArgsConstructorAnnot.getAnnotationMirror(),
            allArgsConstructorAnnot.getAccess(),
            new Object[] {});
      }
    }

    @Override
    protected void validateField(
        TypeElement classElement, VariableElement fieldElement, EntityMeta entityMeta) {
      // doNothing
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
    protected void validateClass(TypeElement classElement, EntityMeta entityMeta) {
      if (!entityMeta.isImmutable()) {
        EntityAnnot entityAnnot = entityMeta.getEntityAnnot();
        throw new AptException(
            Message.DOMA4418,
            classElement,
            entityAnnot.getAnnotationMirror(),
            entityAnnot.getImmutable(),
            new Object[] {});
      }
      super.validateClass(classElement, entityMeta);
    }

    @Override
    public void doConstructor(TypeElement classElement, EntityMeta entityMeta) {
      if (!valueAnnot.getStaticConstructorValue().isEmpty()) {
        throw new AptException(
            Message.DOMA4419,
            classElement,
            valueAnnot.getAnnotationMirror(),
            valueAnnot.getStaticConstructor(),
            new Object[] {});
      }
    }

    @Override
    protected void validateField(
        TypeElement classElement, VariableElement fieldElement, EntityMeta entityMeta) {
      // doNothing
    }
  }
}
