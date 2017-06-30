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
package org.seasar.doma.internal.apt.meta;

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

    private final EntityPropertyMetaFactory propertyMetaFactory;

    public EntityMetaFactory(Context ctx,
            EntityPropertyMetaFactory propertyMetaFactory) {
        assertNotNull(ctx, propertyMetaFactory);
        this.ctx = ctx;
        this.propertyMetaFactory = propertyMetaFactory;
    }

    @Override
    public EntityMeta createTypeElementMeta(TypeElement classElement) {
        assertNotNull(classElement);
        EntityReflection entityReflection = ctx.getReflections()
                .newEntityReflection(classElement);
        if (entityReflection == null) {
            throw new AptIllegalStateException("entityReflection.");
        }
        EntityMeta entityMeta = new EntityMeta(entityReflection, classElement);
        TypeMirror entityListener = resolveEntityListener(classElement);
        entityMeta.setEntityListener(entityListener);
        TypeElement entityListenerElement = ctx.getTypes()
                .toTypeElement(entityListener);
        if (entityListenerElement == null) {
            throw new AptIllegalStateException("entityListener.");
        }
        entityMeta.setEntityListenerElement(entityListenerElement);
        entityMeta.setGenericEntityListener(!entityListenerElement
                .getTypeParameters().isEmpty());
        NamingType namingType = resolveNamingType(classElement);
        entityMeta.setNamingType(namingType);
        boolean immutable = resolveImmutable(classElement, entityReflection);
        entityMeta.setImmutable(immutable);
        entityMeta.setEntityName(classElement.getSimpleName().toString());
        entityMeta.setEntityTypeName(
                ctx.getTypes().getTypeName(classElement.asType()));
        Strategy strategy = createStrategy(classElement, entityMeta);
        strategy.doClassElement(classElement, entityMeta);
        strategy.doFieldElements(classElement, entityMeta);
        strategy.validateGeneratedId(classElement, entityMeta);
        strategy.validateOriginalStates(classElement, entityMeta);
        strategy.doConstructor(classElement, entityMeta);
        return entityMeta;
    }

    private Strategy createStrategy(TypeElement classElement,
            EntityMeta entityMeta) {
        ValueReflection valueReflection = ctx.getReflections()
                .newValueReflection(classElement);
        if (valueReflection != null) {
            return new ValueStrategy(ctx, propertyMetaFactory, valueReflection);
        }
        AllArgsConstructorReflection allArgsConstructorReflection = ctx
                .getReflections()
                .newAllArgsConstructorReflection(classElement);
        if (allArgsConstructorReflection != null) {
            return new AllArgsConstructorStrategy(ctx, propertyMetaFactory,
                    allArgsConstructorReflection);
        }
        return new DefaultStrategy(ctx, propertyMetaFactory);
    }

    private TypeMirror resolveEntityListener(TypeElement classElement) {
        return getEntityElementValues(classElement, EntityReflection.LISTENER)
                .map(AnnotationValueUtil::toType).peek(t -> {
                    if (t == null) {
                        throw new AptIllegalStateException(
                                EntityReflection.LISTENER);
                    }
                }).findFirst().orElseGet(() -> ctx.getTypes()
                        .getType(NullEntityListener.class));
    }

    private NamingType resolveNamingType(TypeElement classElement) {
        return getEntityElementValues(classElement, EntityReflection.NAMING)
                .map(AnnotationValueUtil::toEnumConstant).peek(e -> {
                    if (e == null) {
                        throw new AptIllegalStateException(
                                EntityReflection.NAMING);
                    }
                }).map(VariableElement::getSimpleName).map(Name::toString)
                .map(NamingType::valueOf).findFirst().orElse(null);
    }

    private boolean resolveImmutable(TypeElement classElement,
            EntityReflection entityReflection) {
        List<Boolean> values = getEntityElementValues(classElement,
                EntityReflection.IMMUTABLE).map(AnnotationValueUtil::toBoolean)
                        .peek(b -> {
                            if (b == null) {
                                throw new AptIllegalStateException(
                                        EntityReflection.IMMUTABLE);
                            }
                        }).collect(Collectors.toList());
        if (values.contains(Boolean.TRUE)
                && values.contains(Boolean.FALSE)) {
            throw new AptException(Message.DOMA4226, classElement, entityReflection.getAnnotationMirror(),
                    entityReflection.getImmutable());
        }
        return values.stream().findAny().orElse(false);
    }

    private Stream<AnnotationValue> getEntityElementValues(
            TypeElement classElement, String entityElementName) {
        return ctx.getElements().hierarchy(classElement).stream()
                .map(t -> ctx.getElements().getAnnotationMirror(t,
                        Entity.class))
                .filter(Objects::nonNull)
                .flatMap(a -> a.getElementValues().entrySet().stream())
                .filter(e -> e.getKey().getSimpleName()
                        .contentEquals(entityElementName))
                .map(e -> e.getValue());
    }

    private interface Strategy {

        void doClassElement(TypeElement classElement, EntityMeta entityMeta);

        void doFieldElements(TypeElement classElement, EntityMeta entityMeta);

        void validateGeneratedId(TypeElement classElement, EntityMeta entityMeta);

        void validateOriginalStates(TypeElement classElement,
                EntityMeta entityMeta);

        void doConstructor(TypeElement classElement, EntityMeta entityMeta);
    }

    private static class DefaultStrategy implements Strategy {

        private final Context ctx;

        private final EntityPropertyMetaFactory propertyMetaFactory;

        public DefaultStrategy(Context ctx,
                EntityPropertyMetaFactory propertyMetaFactory) {
            assertNotNull(ctx, propertyMetaFactory);
            this.ctx = ctx;
            this.propertyMetaFactory = propertyMetaFactory;
        }

        public void doClassElement(TypeElement classElement,
                EntityMeta entityMeta) {
            validateClass(classElement, entityMeta);
            validateEntityListener(classElement, entityMeta);

            doTable(classElement, entityMeta);
        }

        protected void validateClass(TypeElement classElement,
                EntityMeta entityMeta) {
            EntityReflection entityReflection = entityMeta.getEntityReflection();
            if (classElement.getKind() != ElementKind.CLASS) {
                throw new AptException(Message.DOMA4015, classElement,
                        entityReflection.getAnnotationMirror());
            }
            if (!classElement.getTypeParameters().isEmpty()) {
                throw new AptException(Message.DOMA4051, classElement);
            }
            validateEnclosingElement(classElement);
        }

        private void validateEnclosingElement(Element element) {
            TypeElement typeElement = ctx.getElements().toTypeElement(element);
            if (typeElement == null) {
                return;
            }
            String simpleName = typeElement.getSimpleName().toString();
            if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
                    || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
                throw new AptException(Message.DOMA4317, typeElement, new Object[] { typeElement.getQualifiedName() });
            }
            NestingKind nestingKind = typeElement.getNestingKind();
            if (nestingKind == NestingKind.TOP_LEVEL) {
                return;
            } else if (nestingKind == NestingKind.MEMBER) {
                Set<Modifier> modifiers = typeElement.getModifiers();
                if (modifiers.containsAll(Arrays.asList(Modifier.STATIC,
                        Modifier.PUBLIC))) {
                    validateEnclosingElement(typeElement.getEnclosingElement());
                } else {
                    throw new AptException(Message.DOMA4315, typeElement, new Object[] { typeElement.getQualifiedName() });
                }
            } else {
                throw new AptException(Message.DOMA4316, typeElement, new Object[] { typeElement.getQualifiedName() });
            }
        }

        private void validateEntityListener(TypeElement classElement,
                EntityMeta entityMeta) {
            EntityReflection entityReflection = entityMeta.getEntityReflection();
            TypeMirror listenerType = entityReflection.getListenerValue();
            TypeElement listenerElement = ctx.getTypes()
                    .toTypeElement(listenerType);
            if (listenerElement == null) {
                throw new AptIllegalStateException(
                        "failed to convert to TypeElement");
            }

            if (listenerElement.getModifiers().contains(Modifier.ABSTRACT)) {
                throw new AptException(Message.DOMA4166, classElement, entityReflection.getAnnotationMirror(),
                        entityReflection.getListener(),
                        new Object[] { listenerElement.getQualifiedName() });
            }

            ExecutableElement constructor = ctx.getElements()
                    .getNoArgConstructor(listenerElement);
            if (constructor == null
                    || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
                throw new AptException(Message.DOMA4167, classElement, entityReflection.getAnnotationMirror(),
                        entityReflection.getListener(),
                        new Object[] { listenerElement.getQualifiedName() });
            }
            if (listenerElement.getTypeParameters().size() > 0) {
                validateGenericEntityListener(classElement, entityMeta,
                        listenerElement);
            } else {
                validateNonGenericEntityListener(classElement, entityMeta,
                        listenerType);
            }

            TypeElement inheritedListenerElement = entityMeta
                    .getEntityListenerElement();
            if (!ctx.getTypes().isSameType(listenerType,
                    inheritedListenerElement.asType())) {
                validateInheritedEntityListener(classElement, entityMeta,
                        inheritedListenerElement);
            }
        }

        private void validateGenericEntityListener(TypeElement classElement,
                EntityMeta entityMeta, TypeElement listenerElement) {
            EntityReflection entityReflection = entityMeta.getEntityReflection();
            List<? extends TypeParameterElement> typeParams = listenerElement
                    .getTypeParameters();
            if (typeParams.size() == 0) {
                throw new AptIllegalStateException(
                        "typeParams size should be more than 0");
            }
            if (typeParams.size() > 1) {
                throw new AptException(Message.DOMA4227, classElement, entityReflection.getAnnotationMirror(),
                        entityReflection.getListener());
            }
            TypeParameterElement typeParam = typeParams.get(0);
            for (TypeMirror bound : typeParam.getBounds()) {
                if (!ctx.getTypes().isAssignable(classElement.asType(),
                        bound)) {
                    throw new AptException(Message.DOMA4229, classElement, entityReflection.getAnnotationMirror(),
                            entityReflection.getListener(),
                            new Object[] {
                                    typeParam.getSimpleName(), bound,
                                    classElement.getQualifiedName() });
                }
            }
            if (findListenerTypeParam(listenerElement, 0) == null) {
                throw new AptException(Message.DOMA4228, classElement, entityReflection.getAnnotationMirror(),
                        entityReflection.getListener(),
                        new Object[] { typeParam.getSimpleName() });
            }
        }

        private TypeParameterElement findListenerTypeParam(
                TypeElement listenerElement, int typeParamIndex) {
            TypeParameterElement typeParam = listenerElement
                    .getTypeParameters().get(typeParamIndex);

            for (TypeMirror interfase : listenerElement.getInterfaces()) {
                DeclaredType declaredType = ctx.getTypes()
                        .toDeclaredType(interfase);
                if (declaredType == null) {
                    continue;
                }
                int i = -1;
                for (TypeMirror typeArg : declaredType.getTypeArguments()) {
                    i++;
                    TypeVariable typeVariable = ctx.getTypes()
                            .toTypeVariable(typeArg);
                    if (typeVariable == null) {
                        continue;
                    }
                    if (typeParam.getSimpleName().equals(
                            typeVariable.asElement().getSimpleName())) {
                        if (ctx.getTypes().isSameType(declaredType,
                                EntityListener.class)) {
                            return typeParam;
                        }
                        TypeElement typeElement = ctx.getTypes()
                                .toTypeElement(declaredType);
                        if (typeElement == null) {
                            throw new AptIllegalStateException(
                                    declaredType.toString());
                        }
                        TypeParameterElement candidate = findListenerTypeParam(
                                typeElement, i);
                        if (candidate != null) {
                            return candidate;
                        }
                    }
                }
            }

            TypeMirror superclass = listenerElement.getSuperclass();
            DeclaredType declaredType = ctx.getTypes()
                    .toDeclaredType(superclass);
            if (declaredType == null) {
                return null;
            }
            int i = -1;
            for (TypeMirror typeArg : declaredType.getTypeArguments()) {
                i++;
                TypeVariable typeVariable = ctx.getTypes()
                        .toTypeVariable(typeArg);
                if (typeVariable == null) {
                    continue;
                }
                if (typeParam.getSimpleName().equals(
                        typeVariable.asElement().getSimpleName())) {
                    if (ctx.getTypes().isSameType(declaredType,
                            EntityListener.class)) {
                        return typeParam;
                    }
                    TypeElement typeElement = ctx.getTypes()
                            .toTypeElement(declaredType);
                    if (typeElement == null) {
                        throw new AptIllegalStateException(
                                declaredType.toString());
                    }
                    TypeParameterElement candidate = findListenerTypeParam(
                            typeElement, i);
                    if (candidate != null) {
                        return candidate;
                    }
                }
            }

            return null;
        }

        private void validateNonGenericEntityListener(
                TypeElement classElement, EntityMeta entityMeta,
                TypeMirror listenerType) {
            EntityReflection entityReflection = entityMeta.getEntityReflection();
            TypeMirror argumentType = getListenerArgumentType(listenerType);
            if (argumentType == null) {
                throw new AptException(Message.DOMA4202, classElement, entityReflection.getAnnotationMirror(),
                        entityReflection.getListener());
            }
            if (!ctx.getTypes().isAssignable(classElement.asType(),
                    argumentType)) {
                throw new AptException(Message.DOMA4038, classElement, entityReflection.getAnnotationMirror(),
                        entityReflection.getListener(),
                        new Object[] {
                                listenerType, argumentType,
                                classElement.getQualifiedName() });
            }
        }

        private void validateInheritedEntityListener(
                TypeElement classElement, EntityMeta entityMeta,
                TypeElement inheritedListenerElement) {
            EntityReflection entityReflection = entityMeta.getEntityReflection();
            List<? extends TypeParameterElement> typeParams = inheritedListenerElement
                    .getTypeParameters();
            if (typeParams.size() == 0) {
                throw new AptException(Message.DOMA4230, classElement, entityReflection.getAnnotationMirror(),
                        new Object[] {
                                inheritedListenerElement.getQualifiedName(),
                                classElement.getQualifiedName() });
            }
            TypeParameterElement typeParam = typeParams.get(0);
            for (TypeMirror bound : typeParam.getBounds()) {
                if (!ctx.getTypes().isAssignable(classElement.asType(),
                        bound)) {
                    throw new AptException(
                            Message.DOMA4231,
                            classElement,
                            entityReflection.getAnnotationMirror(),
                            new Object[] {
                                    inheritedListenerElement.getQualifiedName(),
                                    typeParam.getSimpleName(), bound,
                                    classElement.getQualifiedName() });
                }
            }
        }

        private TypeMirror getListenerArgumentType(TypeMirror typeMirror) {
            for (TypeMirror supertype : ctx.getTypes()
                    .directSupertypes(typeMirror)) {
                if (!ctx.getTypes().isAssignable(supertype,
                        EntityListener.class)) {
                    continue;
                }
                if (ctx.getTypes().isSameType(supertype,
                        EntityListener.class)) {
                    DeclaredType declaredType = ctx.getTypes().toDeclaredType(
                            supertype);
                    if (declaredType == null) {
                        throw new AptIllegalStateException("declaredType");
                    }
                    List<? extends TypeMirror> args = declaredType
                            .getTypeArguments();
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

        private void doTable(TypeElement classElement, EntityMeta entityMeta) {
            TableReflection tableReflection = ctx.getReflections()
                    .newTableReflection(classElement);
            if (tableReflection == null) {
                return;
            }
            entityMeta.setTableMirror(tableReflection);
        }

        public void doFieldElements(TypeElement classElement,
                EntityMeta entityMeta) {
            for (VariableElement fieldElement : getFieldElements(classElement)) {
                try {
                    if (fieldElement.getAnnotation(Transient.class) != null) {
                        continue;
                    } else if (fieldElement.getModifiers().contains(
                            Modifier.STATIC)) {
                        continue;
                    } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
                        doOriginalStatesField(classElement, fieldElement,
                                entityMeta);
                    } else {
                        doEntityPropertyMeta(classElement, fieldElement,
                                entityMeta);
                    }
                } catch (AptException e) {
                    ctx.getNotifier().notify(e);
                    entityMeta.setError(true);
                }
            }
        }

        private List<VariableElement> getFieldElements(
                TypeElement classElement) {
            return ctx.getElements().getUnhiddenFields(classElement,
                    t -> t.getAnnotation(Entity.class) != null);
        }

        private void doOriginalStatesField(TypeElement classElement,
                VariableElement fieldElement, EntityMeta entityMeta) {
            if (entityMeta.hasOriginalStatesMeta()) {
                throw new AptException(Message.DOMA4125, fieldElement);
            }
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                if (!ctx.getTypes().isSameType(fieldElement.asType(),
                        classElement.asType())) {
                    throw new AptException(Message.DOMA4135, fieldElement);
                }
            }
            TypeElement enclosingElement = ctx.getElements()
                    .toTypeElement(fieldElement.getEnclosingElement());
            if (enclosingElement == null) {
                throw new AptIllegalStateException(fieldElement.toString());
            }
            if (entityMeta.isImmutable()
                    && classElement.equals(enclosingElement)) {
                throw new AptException(Message.DOMA4224, fieldElement);
            }
            OriginalStatesMeta originalStatesMeta = new OriginalStatesMeta(
                    classElement, fieldElement, enclosingElement);
            entityMeta.setOriginalStatesMeta(originalStatesMeta);
        }

        private void doEntityPropertyMeta(TypeElement classElement,
                VariableElement fieldElement, EntityMeta entityMeta) {
            validateFieldAnnotation(fieldElement, entityMeta);
            EntityPropertyMeta propertyMeta = propertyMetaFactory
                    .createEntityPropertyMeta(fieldElement, entityMeta);
            entityMeta.addPropertyMeta(propertyMeta);
            validateField(classElement, fieldElement, entityMeta);
        }

        private void validateFieldAnnotation(VariableElement fieldElement,
                EntityMeta entityMeta) {
            TypeElement foundAnnotationTypeElement = null;
            for (AnnotationMirror annotation : fieldElement
                    .getAnnotationMirrors()) {
                DeclaredType declaredType = annotation.getAnnotationType();
                TypeElement typeElement = ctx.getTypes()
                        .toTypeElement(declaredType);
                if (typeElement.getAnnotation(EntityField.class) != null) {
                    if (foundAnnotationTypeElement != null) {
                        throw new AptException(Message.DOMA4086, fieldElement,
                                new Object[] {
                                        foundAnnotationTypeElement
                                                .getQualifiedName(),
                                        typeElement.getQualifiedName() });
                    }
                    foundAnnotationTypeElement = typeElement;
                }
            }
        }

        protected void validateField(TypeElement classElement,
                VariableElement fieldElement, EntityMeta entityMeta) {
            if (entityMeta.isImmutable()
                    && !fieldElement.getModifiers().contains(Modifier.FINAL)) {
                throw new AptException(Message.DOMA4225, fieldElement);
            }
        }

        public void validateGeneratedId(TypeElement classElement,
                EntityMeta entityMeta) {
            if (entityMeta.hasGeneratedIdPropertyMeta()
                    && entityMeta.getIdPropertyMetas().size() > 1) {
                throw new AptException(Message.DOMA4036, classElement);
            }
        }

        public void validateOriginalStates(TypeElement classElement,
                EntityMeta entityMeta) {
            if (entityMeta.hasOriginalStatesMeta()
                    && entityMeta.hasEmbeddedProperties()) {
                throw new AptException(Message.DOMA4305, classElement);
            }
        }

        public void doConstructor(TypeElement classElement,
                EntityMeta entityMeta) {
            if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
                return;
            }
            if (entityMeta.isImmutable()) {
                EntityConstructorMeta constructorMeta = getConstructorMeta(
                        classElement, entityMeta);
                if (constructorMeta == null) {
                    throw new AptException(Message.DOMA4281, classElement);
                }
                if (constructorMeta.getConstructorElement().getModifiers()
                        .contains(Modifier.PRIVATE)) {
                    throw new AptException(Message.DOMA4221, classElement);
                }
                entityMeta.setConstructorMeta(constructorMeta);
            } else {
                ExecutableElement constructor = ctx.getElements()
                        .getNoArgConstructor(classElement);
                if (constructor == null
                        || constructor.getModifiers()
                                .contains(Modifier.PRIVATE)) {
                    throw new AptException(Message.DOMA4124, classElement);
                }
            }
        }

        private EntityConstructorMeta getConstructorMeta(
                TypeElement classElement, EntityMeta entityMeta) {
            Map<String, EntityPropertyMeta> entityPropertyMetaMap = new HashMap<String, EntityPropertyMeta>();
            for (EntityPropertyMeta propertyMeta : entityMeta
                    .getAllPropertyMetas()) {
                entityPropertyMetaMap.put(propertyMeta.getName(), propertyMeta);
            }
            outer: for (ExecutableElement constructor : ElementFilter
                    .constructorsIn(classElement.getEnclosedElements())) {
                List<EntityPropertyMeta> entityPropertyMetaList = new ArrayList<>();
                for (VariableElement param : constructor.getParameters()) {
                    String name = ctx.getElements().getParameterName(param);
                    TypeMirror paramType = param.asType();
                    EntityPropertyMeta propertyMeta = entityPropertyMetaMap
                            .get(name);
                    if (propertyMeta == null) {
                        continue outer;
                    }
                    TypeMirror propertyType = propertyMeta.getType();
                    if (!ctx.getTypes().isSameType(paramType, propertyType)) {
                        continue outer;
                    }
                    entityPropertyMetaList.add(propertyMeta);
                }
                if (entityPropertyMetaMap.size() == entityPropertyMetaList
                        .size()) {
                    return new EntityConstructorMeta(constructor,
                            entityPropertyMetaList);
                }
            }
            return null;
        }

    }

    private static class AllArgsConstructorStrategy extends DefaultStrategy {

        private final AllArgsConstructorReflection allArgsConstructorReflection;

        public AllArgsConstructorStrategy(Context ctx,
                EntityPropertyMetaFactory propertyMetaFactory,
                AllArgsConstructorReflection allArgsConstructorReflection) {
            super(ctx, propertyMetaFactory);
            assertNotNull(allArgsConstructorReflection);
            this.allArgsConstructorReflection = allArgsConstructorReflection;
        }

        @Override
        protected void validateClass(TypeElement classElement,
                EntityMeta entityMeta) {
            if (!entityMeta.isImmutable()) {
                throw new AptException(Message.DOMA4420, classElement,
                        allArgsConstructorReflection.getAnnotationMirror());
            }
            super.validateClass(classElement, entityMeta);
        }

        @Override
        public void doConstructor(TypeElement classElement,
                EntityMeta entityMeta) {
            if (!allArgsConstructorReflection.getStaticNameValue().isEmpty()) {
                throw new AptException(Message.DOMA4421, classElement, allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getStaticName());
            }
            if (allArgsConstructorReflection.isAccessPrivate()) {
                throw new AptException(Message.DOMA4422, classElement, allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getAccess());
            }
            if (allArgsConstructorReflection.isAccessNone()) {
                throw new AptException(Message.DOMA4426, classElement, allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getAccess());
            }
        }

        @Override
        protected void validateField(TypeElement classElement,
                VariableElement fieldElement, EntityMeta entityMeta) {
            // doNothing
        }
    }

    private static class ValueStrategy extends DefaultStrategy {

        private final ValueReflection valueReflection;

        public ValueStrategy(Context ctx,
                EntityPropertyMetaFactory propertyMetaFactory,
                ValueReflection valueReflection) {
            super(ctx, propertyMetaFactory);
            assertNotNull(valueReflection);
            this.valueReflection = valueReflection;
        }

        @Override
        protected void validateClass(TypeElement classElement,
                EntityMeta entityMeta) {
            if (!entityMeta.isImmutable()) {
                EntityReflection entityReflection = entityMeta.getEntityReflection();
                throw new AptException(Message.DOMA4418, classElement, entityReflection.getAnnotationMirror(),
                        entityReflection.getImmutable());
            }
            super.validateClass(classElement, entityMeta);
        }

        @Override
        public void doConstructor(TypeElement classElement,
                EntityMeta entityMeta) {
            if (!valueReflection.getStaticConstructorValue().isEmpty()) {
                throw new AptException(Message.DOMA4419, classElement, valueReflection.getAnnotationMirror(),
                        valueReflection.getStaticConstructor());
            }
        }

        @Override
        protected void validateField(TypeElement classElement,
                VariableElement fieldElement, EntityMeta entityMeta) {
            // doNothing
        }
    }

}
