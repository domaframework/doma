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
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Embeddable;
import org.seasar.doma.EntityField;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.ParameterName;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.reflection.AllArgsConstructorReflection;
import org.seasar.doma.internal.apt.reflection.EmbeddableReflection;
import org.seasar.doma.internal.apt.reflection.ValueReflection;
import org.seasar.doma.message.Message;

/**
 * @author nakamura-to
 *
 */
public class EmbeddableMetaFactory implements
        TypeElementMetaFactory<EmbeddableMeta> {

    private final Context ctx;

    private final EmbeddablePropertyMetaFactory propertyMetaFactory;

    public EmbeddableMetaFactory(Context ctx,
            EmbeddablePropertyMetaFactory propertyMetaFactory) {
        assertNotNull(ctx, propertyMetaFactory);
        this.ctx = ctx;
        this.propertyMetaFactory = propertyMetaFactory;
    }

    @Override
    public EmbeddableMeta createTypeElementMeta(TypeElement embeddableElement) {
        EmbeddableReflection reflection = ctx.getReflections()
                .newEmbeddableReflection(embeddableElement);
        if (reflection == null) {
            throw new AptIllegalStateException("mirror must not be null");
        }
        EmbeddableMeta embeddableMeta = new EmbeddableMeta(reflection,
                embeddableElement);
        Strategy strategy = createStrategy(embeddableElement, embeddableMeta);
        strategy.validateClass(embeddableElement, embeddableMeta);
        strategy.doFieldElements(embeddableElement, embeddableMeta);
        strategy.doConstructor(embeddableElement, embeddableMeta);
        return embeddableMeta;
    }

    private Strategy createStrategy(TypeElement embeddableElement,
            EmbeddableMeta embeddableMeta) {
        ValueReflection valueReflection = ctx.getReflections()
                .newValueReflection(embeddableElement);
        if (valueReflection != null) {
            return new ValueStrategy(ctx, propertyMetaFactory, valueReflection);
        }
        AllArgsConstructorReflection allArgsConstructorReflection = ctx
                .getReflections()
                .newAllArgsConstructorReflection(embeddableElement);
        if (allArgsConstructorReflection != null) {
            return new AllArgsConstructorStrategy(ctx, propertyMetaFactory,
                    allArgsConstructorReflection);
        }
        return new DefaultStrategy(ctx, propertyMetaFactory);
    }

    private interface Strategy {

        void validateClass(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta);

        void doFieldElements(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta);

        void doConstructor(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta);
    }

    private static class DefaultStrategy implements Strategy {

        private final Context ctx;

        private final EmbeddablePropertyMetaFactory propertyMetaFactory;

        public DefaultStrategy(Context ctx,
                EmbeddablePropertyMetaFactory propertyMetaFactory) {
            assertNotNull(ctx, propertyMetaFactory);
            this.ctx = ctx;
            this.propertyMetaFactory = propertyMetaFactory;
        }

        @Override
        public void validateClass(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta) {
            if (embeddableElement.getKind() != ElementKind.CLASS) {
                EmbeddableReflection embeddableReflection = embeddableMeta
                        .getEmbeddableReflection();
                throw new AptException(Message.DOMA4283, embeddableElement,
                        embeddableReflection.getAnnotationMirror());
            }
            if (!embeddableElement.getTypeParameters().isEmpty()) {
                throw new AptException(Message.DOMA4285, embeddableElement);
            }
            validateEnclosingElement(embeddableElement);
        }

        private void validateEnclosingElement(Element element) {
            TypeElement typeElement = ctx.getElements().toTypeElement(element);
            if (typeElement == null) {
                return;
            }
            String simpleName = typeElement.getSimpleName().toString();
            if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
                    || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
                throw new AptException(Message.DOMA4417, typeElement, new Object[] { typeElement.getQualifiedName() });
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
                    throw new AptException(Message.DOMA4415, typeElement, new Object[] { typeElement.getQualifiedName() });
                }
            } else {
                throw new AptException(Message.DOMA4416, typeElement, new Object[] { typeElement.getQualifiedName() });
            }
        }

        @Override
        public void doFieldElements(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta) {
            for (VariableElement fieldElement : getFieldElements(embeddableElement)) {
                try {
                    if (fieldElement.getAnnotation(Transient.class) != null) {
                        continue;
                    } else if (fieldElement.getModifiers().contains(
                            Modifier.STATIC)) {
                        continue;
                    } else if (fieldElement.getAnnotation(OriginalStates.class) != null) {
                        throw new AptException(Message.DOMA4286, fieldElement);
                    } else if (fieldElement.getAnnotation(Id.class) != null) {
                        throw new AptException(Message.DOMA4289, fieldElement);
                    } else if (fieldElement.getAnnotation(Version.class) != null) {
                        throw new AptException(Message.DOMA4290, fieldElement);
                    } else if (fieldElement.getAnnotation(GeneratedValue.class) != null) {
                        throw new AptException(Message.DOMA4291, fieldElement);
                    } else {
                        doEmbeddablePropertyMeta(fieldElement, embeddableMeta);
                    }
                } catch (AptException e) {
                    ctx.getNotifier().notify(e);
                    embeddableMeta.setError(true);
                }
            }
        }

        private void doEmbeddablePropertyMeta(VariableElement fieldElement,
                EmbeddableMeta embeddableMeta) {
            validateFieldAnnotation(fieldElement, embeddableMeta);
            EmbeddablePropertyMeta propertyMeta = propertyMetaFactory
                    .createEmbeddablePropertyMeta(fieldElement, embeddableMeta);
            embeddableMeta.addEmbeddablePropertyMeta(propertyMeta);
        }

        private List<VariableElement> getFieldElements(
                TypeElement embeddableElement) {
            return ctx.getElements().getUnhiddenFields(embeddableElement,
                    t -> t.getAnnotation(Embeddable.class) != null);
        }

        private void validateFieldAnnotation(VariableElement fieldElement,
                EmbeddableMeta embeddableMeta) {
            TypeElement foundAnnotationTypeElement = null;
            for (AnnotationMirror annotation : fieldElement
                    .getAnnotationMirrors()) {
                DeclaredType declaredType = annotation.getAnnotationType();
                TypeElement typeElement = ctx.getTypes()
                        .toTypeElement(declaredType);
                if (typeElement.getAnnotation(EntityField.class) != null) {
                    if (foundAnnotationTypeElement != null) {
                        throw new AptException(Message.DOMA4288, fieldElement,
                                new Object[] {
                                        foundAnnotationTypeElement
                                                .getQualifiedName(),
                                        typeElement.getQualifiedName() });
                    }
                    foundAnnotationTypeElement = typeElement;
                }
            }
        }

        @Override
        public void doConstructor(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta) {
            if (embeddableMeta.isAbstract()) {
                return;
            }
            EmbeddableConstructorMeta constructorMeta = getConstructorMeta(
                    embeddableElement, embeddableMeta);
            if (constructorMeta == null) {
                throw new AptException(Message.DOMA4293, embeddableElement);
            }
            if (constructorMeta.getConstructorElement().getModifiers()
                    .contains(Modifier.PRIVATE)) {
                throw new AptException(Message.DOMA4294, embeddableElement);
            }
            embeddableMeta.setConstructorMeta(constructorMeta);
        }

        private EmbeddableConstructorMeta getConstructorMeta(
                TypeElement embeddapleElement, EmbeddableMeta embeddableMeta) {
            Map<String, EmbeddablePropertyMeta> propertyMetaMap = new HashMap<String, EmbeddablePropertyMeta>();
            for (EmbeddablePropertyMeta propertyMeta : embeddableMeta
                    .getEmbeddablePropertyMetas()) {
                propertyMetaMap.put(propertyMeta.getName(), propertyMeta);
            }
            outer: for (ExecutableElement constructor : ElementFilter
                    .constructorsIn(embeddapleElement.getEnclosedElements())) {
                List<EmbeddablePropertyMeta> propertyMetaList = new ArrayList<>();
                for (VariableElement param : constructor.getParameters()) {
                    String name = param.getSimpleName().toString();
                    ParameterName parameterName = param
                            .getAnnotation(ParameterName.class);
                    if (parameterName != null) {
                        name = parameterName.value();
                    }
                    TypeMirror paramType = param.asType();
                    EmbeddablePropertyMeta propertyMeta = propertyMetaMap
                            .get(name);
                    if (propertyMeta == null) {
                        continue outer;
                    }
                    TypeMirror propertyType = propertyMeta.getType();
                    if (!ctx.getTypes().isSameType(paramType, propertyType)) {
                        continue outer;
                    }
                    propertyMetaList.add(propertyMeta);
                }
                if (propertyMetaMap.size() == propertyMetaList.size()) {
                    return new EmbeddableConstructorMeta(constructor,
                            propertyMetaList);
                }
            }
            return null;
        }

    }

    private static class AllArgsConstructorStrategy extends DefaultStrategy {

        private final AllArgsConstructorReflection allArgsConstructorReflection;

        public AllArgsConstructorStrategy(Context ctx,
                EmbeddablePropertyMetaFactory propertyMetaFactory,
                AllArgsConstructorReflection allArgsConstructorReflection) {
            super(ctx, propertyMetaFactory);
            assertNotNull(allArgsConstructorReflection);
            this.allArgsConstructorReflection = allArgsConstructorReflection;
        }

        @Override
        public void doConstructor(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta) {
            if (!allArgsConstructorReflection.getStaticNameValue().isEmpty()) {
                throw new AptException(Message.DOMA4424, embeddableElement,
                        allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getStaticName());
            }
            if (allArgsConstructorReflection.isAccessPrivate()) {
                throw new AptException(Message.DOMA4425, embeddableElement,
                        allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getAccess());
            }
            if (allArgsConstructorReflection.isAccessNone()) {
                throw new AptException(Message.DOMA4427, embeddableElement,
                        allArgsConstructorReflection.getAnnotationMirror(),
                        allArgsConstructorReflection.getAccess());
            }
        }

    }

    private static class ValueStrategy extends DefaultStrategy {

        private final ValueReflection valueReflection;

        public ValueStrategy(Context ctx,
                EmbeddablePropertyMetaFactory propertyMetaFactory,
                ValueReflection valueReflection) {
            super(ctx, propertyMetaFactory);
            assertNotNull(valueReflection);
            this.valueReflection = valueReflection;
        }

        @Override
        public void doConstructor(TypeElement embeddableElement,
                EmbeddableMeta embeddableMeta) {
            if (!valueReflection.getStaticConstructorValue().isEmpty()) {
                throw new AptException(Message.DOMA4423, embeddableElement,
                        valueReflection.getAnnotationMirror(),
                        valueReflection.getStaticConstructor());
            }
        }
    }
}
