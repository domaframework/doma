/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Entity;
import org.seasar.doma.EntityField;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.reflection.AllArgsConstructorReflection;
import org.seasar.doma.internal.apt.reflection.EntityReflection;
import org.seasar.doma.internal.apt.reflection.TableReflection;
import org.seasar.doma.internal.apt.reflection.ValueReflection;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.NullEntityListener;
import org.seasar.doma.message.Message;

/**
 * 
 * @author taedium
 * 
 */
public class EntityMetaFactory implements TypeElementMetaFactory<EntityMeta> {

    private final Context ctx;

    private final TypeElement entityElement;

    private final EntityReflection entityReflection;

    private boolean error;

    public EntityMetaFactory(Context ctx, TypeElement entityElement) {
        assertNotNull(ctx, entityElement);
        this.ctx = ctx;
        this.entityElement = entityElement;
        entityReflection = ctx.getReflections().newEntityReflection(entityElement);
        if (entityReflection == null) {
            throw new AptIllegalStateException("entityReflection.");
        }
    }

    @Override
    public EntityMeta createTypeElementMeta() {
        EntityMeta entityMeta = new EntityMeta(entityReflection, entityElement);
        entityMeta.setNamingType(resolveNamingType());
        entityMeta.setImmutable(resolveImmutable());
        entityMeta.setEntityTypeName(ctx.getTypes().getTypeName(entityElement.asType()));

        Strategy strategy = createStrategy(entityMeta);
        strategy.doClass(entityMeta);
        strategy.doFields(entityMeta);
        strategy.validateGeneratedId(entityMeta);
        strategy.validateOriginalStates(entityMeta);
        strategy.doConstructor(entityMeta);
        return error ? null : entityMeta;
    }

    private Strategy createStrategy(EntityMeta entityMeta) {
        ValueReflection valueReflection = ctx.getReflections().newValueReflection(entityElement);
        if (valueReflection != null) {
            return new ValueStrategy(valueReflection);
        }
        AllArgsConstructorReflection allArgsConstructorReflection = ctx.getReflections()
                .newAllArgsConstructorReflection(entityElement);
        if (allArgsConstructorReflection != null) {
            return new AllArgsConstructorStrategy(allArgsConstructorReflection);
        }
        return new DefaultStrategy();
    }

    private TypeMirror resolveListener() {
        return getEntityAnnotationElementValues(EntityReflection.LISTENER)
                .map(AnnotationValueUtil::toType)
                .peek(t -> {
                    if (t == null) {
                        throw new AptIllegalStateException(EntityReflection.LISTENER);
                    }
                })
                .findFirst()
                .orElseGet(() -> ctx.getTypes().getType(NullEntityListener.class));
    }

    private NamingType resolveNamingType() {
        return getEntityAnnotationElementValues(EntityReflection.NAMING)
                .map(AnnotationValueUtil::toEnumConstant)
                .peek(e -> {
                    if (e == null) {
                        throw new AptIllegalStateException(EntityReflection.NAMING);
                    }
                })
                .map(VariableElement::getSimpleName)
                .map(Name::toString)
                .map(NamingType::valueOf)
                .findFirst()
                .orElse(null);
    }

    private boolean resolveImmutable() {
        List<Boolean> values = getEntityAnnotationElementValues(EntityReflection.IMMUTABLE)
                .map(AnnotationValueUtil::toBoolean)
                .peek(b -> {
                    if (b == null) {
                        throw new AptIllegalStateException(EntityReflection.IMMUTABLE);
                    }
                })
                .collect(Collectors.toList());
        if (values.contains(Boolean.TRUE) && values.contains(Boolean.FALSE)) {
            throw new AptException(Message.DOMA4226, entityElement,
                    entityReflection.getAnnotationMirror(), entityReflection.getImmutable());
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
                .map(e -> e.getValue());
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
                throw new AptException(Message.DOMA4015, entityElement,
                        entityReflection.getAnnotationMirror());
            }
            if (!entityElement.getTypeParameters().isEmpty()) {
                throw new AptException(Message.DOMA4051, entityElement);
            }
            validateEnclosingElement(entityElement);
        }

        private void validateEnclosingElement(Element element) {
            TypeElement typeElement = ctx.getElements().toTypeElement(element);
            if (typeElement == null) {
                return;
            }
            String simpleName = typeElement.getSimpleName().toString();
            if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
                    || simpleName.contains(Constants.DESC_NAME_DELIMITER)) {
                throw new AptException(Message.DOMA4317, typeElement,
                        new Object[] { typeElement.getQualifiedName() });
            }
            NestingKind nestingKind = typeElement.getNestingKind();
            if (nestingKind == NestingKind.TOP_LEVEL) {
                return;
            } else if (nestingKind == NestingKind.MEMBER) {
                Set<Modifier> modifiers = typeElement.getModifiers();
                if (modifiers.containsAll(Arrays.asList(Modifier.STATIC, Modifier.PUBLIC))) {
                    validateEnclosingElement(typeElement.getEnclosingElement());
                } else {
                    throw new AptException(Message.DOMA4315, typeElement,
                            new Object[] { typeElement.getQualifiedName() });
                }
            } else {
                throw new AptException(Message.DOMA4316, typeElement,
                        new Object[] { typeElement.getQualifiedName() });
            }
        }

        private void doEntityListener(EntityMeta entityMeta) {
            TypeMirror listenerType = entityReflection.getListenerValue();
            TypeElement listenerElement = ctx.getTypes().toTypeElement(listenerType);
            if (listenerElement == null) {
                throw new AptIllegalStateException("listenerElement");
            }
            validateListener(listenerElement);

            TypeMirror inheritedListenerType = resolveListener();
            TypeElement inheritedListenerElement = ctx.getTypes()
                    .toTypeElement(inheritedListenerType);
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
                throw new AptException(Message.DOMA4166, entityElement,
                        entityReflection.getAnnotationMirror(), entityReflection.getListener(),
                        new Object[] { listenerElement.getQualifiedName() });
            }

            ExecutableElement constructor = ctx.getElements().getNoArgConstructor(listenerElement);
            if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
                throw new AptException(Message.DOMA4167, entityElement,
                        entityReflection.getAnnotationMirror(), entityReflection.getListener(),
                        new Object[] { listenerElement.getQualifiedName() });
            }
            if (listenerElement.getTypeParameters().size() > 0) {
                validateGenericEntityListener(listenerElement);
            } else {
                validateNonGenericEntityListener(listenerElement);
            }
        }

        private void validateGenericEntityListener(TypeElement listenerElement) {
            List<? extends TypeParameterElement> typeParams = listenerElement.getTypeParameters();
            if (typeParams.size() == 0) {
                throw new AptIllegalStateException("typeParams size should be more than 0");
            }
            if (typeParams.size() > 1) {
                throw new AptException(Message.DOMA4227, entityElement,
                        entityReflection.getAnnotationMirror(), entityReflection.getListener());
            }
            TypeParameterElement typeParam = typeParams.get(0);
            for (TypeMirror bound : typeParam.getBounds()) {
                if (!ctx.getTypes().isAssignable(entityElement.asType(), bound)) {
                    throw new AptException(Message.DOMA4229, entityElement,
                            entityReflection.getAnnotationMirror(), entityReflection.getListener(),
                            new Object[] { typeParam.getSimpleName(), bound,
                                    entityElement.getQualifiedName() });
                }
            }
            if (findListenerTypeParam(listenerElement, 0) == null) {
                throw new AptException(Message.DOMA4228, entityElement,
                        entityReflection.getAnnotationMirror(), entityReflection.getListener(),
                        new Object[] { typeParam.getSimpleName() });
            }
        }

        private TypeParameterElement findListenerTypeParam(TypeElement listenerElement,
                int typeParamIndex) {
            TypeParameterElement typeParam = listenerElement.getTypeParameters()
                    .get(typeParamIndex);

            for (TypeMirror interfase : listenerElement.getInterfaces()) {
                DeclaredType declaredType = ctx.getTypes().toDeclaredType(interfase);
                if (declaredType == null) {
                    continue;
                }
                int i = -1;
                for (TypeMirror typeArg : declaredType.getTypeArguments()) {
                    i++;
                    TypeVariable typeVariable = ctx.getTypes().toTypeVariable(typeArg);
                    if (typeVariable == null) {
                        continue;
                    }
                    if (typeParam.getSimpleName()
                            .equals(typeVariable.asElement().getSimpleName())) {
                        if (ctx.getTypes().isSameType(declaredType, EntityListener.class)) {
                            return typeParam;
                        }
                        TypeElement typeElement = ctx.getTypes().toTypeElement(declaredType);
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
            DeclaredType declaredType = ctx.getTypes().toDeclaredType(superclass);
            if (declaredType == null) {
                return null;
            }
            int i = -1;
            for (TypeMirror typeArg : declaredType.getTypeArguments()) {
                i++;
                TypeVariable typeVariable = ctx.getTypes().toTypeVariable(typeArg);
                if (typeVariable == null) {
                    continue;
                }
                if (typeParam.getSimpleName().equals(typeVariable.asElement().getSimpleName())) {
                    if (ctx.getTypes().isSameType(declaredType, EntityListener.class)) {
                        return typeParam;
                    }
                    TypeElement typeElement = ctx.getTypes().toTypeElement(declaredType);
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

        private void validateNonGenericEntityListener(TypeElement listenerElement) {
            TypeMirror listenerType = listenerElement.asType();
            TypeMirror argumentType = getListenerArgumentType(listenerType);
            if (argumentType == null) {
                throw new AptException(Message.DOMA4202, entityElement,
                        entityReflection.getAnnotationMirror(), entityReflection.getListener());
            }
            if (!ctx.getTypes().isAssignable(entityElement.asType(), argumentType)) {
                throw new AptException(Message.DOMA4038, entityElement,
                        entityReflection.getAnnotationMirror(), entityReflection.getListener(),
                        new Object[] { listenerType, argumentType,
                                entityElement.getQualifiedName() });
            }
        }

        private void validateInheritedListener(EntityMeta entityMeta,
                TypeElement inheritedListenerElement) {
            List<? extends TypeParameterElement> typeParams = inheritedListenerElement
                    .getTypeParameters();
            if (typeParams.size() == 0) {
                throw new AptException(Message.DOMA4230, entityElement,
                        entityReflection.getAnnotationMirror(),
                        new Object[] { inheritedListenerElement.getQualifiedName(),
                                entityElement.getQualifiedName() });
            }
            TypeParameterElement typeParam = typeParams.get(0);
            for (TypeMirror bound : typeParam.getBounds()) {
                if (!ctx.getTypes().isAssignable(entityElement.asType(), bound)) {
                    throw new AptException(Message.DOMA4231, entityElement,
                            entityReflection.getAnnotationMirror(),
                            new Object[] { inheritedListenerElement.getQualifiedName(),
                                    typeParam.getSimpleName(), bound,
                                    entityElement.getQualifiedName() });
                }
            }
        }

        private TypeMirror getListenerArgumentType(TypeMirror typeMirror) {
            for (TypeMirror supertype : ctx.getTypes().directSupertypes(typeMirror)) {
                if (!ctx.getTypes().isAssignable(supertype, EntityListener.class)) {
                    continue;
                }
                if (ctx.getTypes().isSameType(supertype, EntityListener.class)) {
                    DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
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

        private void doTable(EntityMeta entityMeta) {
            TableReflection tableReflection = ctx.getReflections()
                    .newTableReflection(entityElement);
            if (tableReflection == null) {
                return;
            }
            entityMeta.setTableMirror(tableReflection);
        }

        public void doFields(EntityMeta entityMeta) {
            for (VariableElement fieldElement : getFields()) {
                try {
                    if (fieldElement.getAnnotation(Transient.class) != null) {
                        continue;
                    } else if (fieldElement.getModifiers().contains(Modifier.STATIC)) {
                        continue;
                    } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
                        doOriginalStatesField(fieldElement, entityMeta);
                    } else {
                        doEntityPropertyMeta(fieldElement, entityMeta);
                    }
                } catch (AptException e) {
                    ctx.getNotifier().notify(e);
                    error = true;
                }
            }
        }

        private List<VariableElement> getFields() {
            return ctx.getElements().getUnhiddenFields(entityElement,
                    t -> t.getAnnotation(Entity.class) != null);
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
            TypeElement enclosingElement = ctx.getElements()
                    .toTypeElement(fieldElement.getEnclosingElement());
            if (enclosingElement == null) {
                throw new AptIllegalStateException(fieldElement.toString());
            }
            if (entityMeta.isImmutable() && entityElement.equals(enclosingElement)) {
                throw new AptException(Message.DOMA4224, fieldElement);
            }
            OriginalStatesMeta originalStatesMeta = new OriginalStatesMeta(entityElement,
                    fieldElement, enclosingElement);
            entityMeta.setOriginalStatesMeta(originalStatesMeta);
        }

        private void doEntityPropertyMeta(VariableElement fieldElement, EntityMeta entityMeta) {
            validateFieldAnnotation(fieldElement, entityMeta);
            EntityPropertyMetaFactory propertyMetaFactory = new EntityPropertyMetaFactory(ctx,
                    fieldElement);
            EntityPropertyMeta propertyMeta = propertyMetaFactory.createEntityPropertyMeta();
            if (propertyMeta.getIdGeneratorMeta() != null
                    && entityMeta.hasGeneratedIdPropertyMeta()) {
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
                DeclaredType declaredType = annotation.getAnnotationType();
                TypeElement typeElement = ctx.getTypes().toTypeElement(declaredType);
                if (typeElement.getAnnotation(EntityField.class) != null) {
                    if (foundAnnotationTypeElement != null) {
                        throw new AptException(Message.DOMA4086, fieldElement,
                                new Object[] { foundAnnotationTypeElement.getQualifiedName(),
                                        typeElement.getQualifiedName() });
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
            if (entityMeta.hasGeneratedIdPropertyMeta()
                    && entityMeta.getIdPropertyMetas().size() > 1) {
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
                EntityConstructorMeta constructorMeta = getConstructorMeta(entityMeta);
                if (constructorMeta == null) {
                    throw new AptException(Message.DOMA4281, entityElement);
                }
                if (constructorMeta.getConstructorElement().getModifiers().contains(
                        Modifier.PRIVATE)) {
                    throw new AptException(Message.DOMA4221, entityElement);
                }
            } else {
                ExecutableElement constructor = ctx.getElements()
                        .getNoArgConstructor(entityElement);
                if (constructor == null || constructor.getModifiers().contains(Modifier.PRIVATE)) {
                    throw new AptException(Message.DOMA4124, entityElement);
                }
            }
        }

        private EntityConstructorMeta getConstructorMeta(EntityMeta entityMeta) {
            Map<String, EntityPropertyMeta> entityPropertyMetaMap = new HashMap<String, EntityPropertyMeta>();
            for (EntityPropertyMeta propertyMeta : entityMeta.getAllPropertyMetas()) {
                entityPropertyMetaMap.put(propertyMeta.getName(), propertyMeta);
            }
            outer: for (ExecutableElement constructor : ElementFilter
                    .constructorsIn(entityElement.getEnclosedElements())) {
                List<EntityPropertyMeta> entityPropertyMetaList = new ArrayList<>();
                for (VariableElement param : constructor.getParameters()) {
                    String name = ctx.getElements().getParameterName(param);
                    TypeMirror paramType = param.asType();
                    EntityPropertyMeta propertyMeta = entityPropertyMetaMap.get(name);
                    if (propertyMeta == null) {
                        continue outer;
                    }
                    TypeMirror propertyType = propertyMeta.getType();
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

        private final AllArgsConstructorReflection allArgsConstructorReflection;

        public AllArgsConstructorStrategy(
                AllArgsConstructorReflection allArgsConstructorReflection) {
            assertNotNull(allArgsConstructorReflection);
            this.allArgsConstructorReflection = allArgsConstructorReflection;
        }

        @Override
        protected void validateClass(EntityMeta entityMeta) {
            if (!entityMeta.isImmutable()) {
                throw new AptException(Message.DOMA4420, entityElement,
                        allArgsConstructorReflection.getAnnotationMirror());
            }
            super.validateClass(entityMeta);
        }

        @Override
        public void doConstructor(EntityMeta entityMeta) {
            if (!allArgsConstructorReflection.getStaticNameValue().isEmpty()) {
                throw new AptException(Message.DOMA4421, entityElement,
                        allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getStaticName());
            }
            if (allArgsConstructorReflection.isAccessPrivate()) {
                throw new AptException(Message.DOMA4422, entityElement,
                        allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getAccess());
            }
            if (allArgsConstructorReflection.isAccessNone()) {
                throw new AptException(Message.DOMA4426, entityElement,
                        allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getAccess());
            }
        }

        @Override
        protected void validateField(VariableElement fieldElement, EntityMeta entityMeta) {
            // doNothing
        }
    }

    private class ValueStrategy extends DefaultStrategy {

        private final ValueReflection valueReflection;

        public ValueStrategy(ValueReflection valueReflection) {
            assertNotNull(valueReflection);
            this.valueReflection = valueReflection;
        }

        @Override
        protected void validateClass(EntityMeta entityMeta) {
            if (!entityMeta.isImmutable()) {
                throw new AptException(Message.DOMA4418, entityElement,
                        entityReflection.getAnnotationMirror(), entityReflection.getImmutable());
            }
            super.validateClass(entityMeta);
        }

        @Override
        public void doConstructor(EntityMeta entityMeta) {
            if (!valueReflection.getStaticConstructorValue().isEmpty()) {
                throw new AptException(Message.DOMA4419, entityElement,
                        valueReflection.getAnnotationMirror(),
                        valueReflection.getStaticConstructor());
            }
        }

        @Override
        protected void validateField(VariableElement fieldElement, EntityMeta entityMeta) {
            // doNothing
        }
    }

}
