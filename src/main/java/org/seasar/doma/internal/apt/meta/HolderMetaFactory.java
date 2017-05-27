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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
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
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.reflection.HolderReflection;
import org.seasar.doma.internal.apt.reflection.Reflections;
import org.seasar.doma.internal.apt.reflection.ValueReflection;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.message.Message;

public class HolderMetaFactory implements TypeElementMetaFactory<HolderMeta> {

    private final ProcessingEnvironment env;

    public HolderMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    @Override
    public HolderMeta createTypeElementMeta(TypeElement classElement) {
        assertNotNull(classElement);
        HolderReflection holderReflection = new Reflections(env)
                .newHolderReflection(classElement);
        if (holderReflection == null) {
            throw new AptIllegalStateException("holderReflection");
        }
        HolderMeta holderMeta = new HolderMeta(classElement,
                classElement.asType());
        holderMeta.setHolderReflection(holderReflection);
        Strategy strategy = createStrategy(classElement, holderMeta);
        strategy.doWrapperCtType(classElement, holderMeta);
        strategy.validateAcceptNull(classElement, holderMeta);
        strategy.validateClass(classElement, holderMeta);
        strategy.validateInitializer(classElement, holderMeta);
        strategy.validateAccessorMethod(classElement, holderMeta);
        return holderMeta;
    }

    protected Strategy createStrategy(TypeElement classElement,
            HolderMeta holderMeta) {
        ValueReflection valueReflection = new Reflections(env)
                .newValueReflection(classElement);
        if (valueReflection != null) {
            return new ValueStragety(env, valueReflection);
        }
        return new DefaultStrategy(env);
    }

    protected interface Strategy {

        void doWrapperCtType(TypeElement classElement, HolderMeta holderMeta);

        void validateAcceptNull(TypeElement classElement, HolderMeta holderMeta);

        void validateClass(TypeElement classElement, HolderMeta holderMeta);

        void validateInitializer(TypeElement classElement, HolderMeta holderMeta);

        void validateAccessorMethod(TypeElement classElement,
                HolderMeta holderMeta);

    }

    protected static class DefaultStrategy implements Strategy {

        protected final ProcessingEnvironment env;

        public DefaultStrategy(ProcessingEnvironment env) {
            assertNotNull(env);
            this.env = env;
        }

        @Override
        public void doWrapperCtType(TypeElement classElement,
                HolderMeta holderMeta) {
            BasicCtType basicCtType = BasicCtType.newInstance(
                    holderMeta.getValueType(), env);
            if (basicCtType == null) {
                HolderReflection holderReflection = holderMeta.getHolderReflection();
                throw new AptException(Message.DOMA4102, env, classElement,
                        holderReflection.getAnnotationMirror(),
                        holderReflection.getValueType(), new Object[] {
                                holderReflection.getValueTypeValue(),
                                classElement.getQualifiedName() });
            }
            holderMeta.setBasicCtType(basicCtType);
            holderMeta.setWrapperCtType(basicCtType.getWrapperCtType());
        }

        @Override
        public void validateAcceptNull(TypeElement classElement,
                HolderMeta holderMeta) {
            if (holderMeta.getBasicCtType().isPrimitive()
                    && holderMeta.getAcceptNull()) {
                HolderReflection holderReflection = holderMeta.getHolderReflection();
                throw new AptException(Message.DOMA4251, env, classElement,
                        holderReflection.getAnnotationMirror(),
                        holderReflection.getAcceptNull(),
                        new Object[] { classElement.getQualifiedName() });
            }
        }

        @Override
        public void validateClass(TypeElement classElement,
                HolderMeta holderMeta) {
            if (classElement.getKind() == ElementKind.CLASS) {
                if (holderMeta.providesConstructor()
                        && classElement.getModifiers().contains(
                                Modifier.ABSTRACT)) {
                    throw new AptException(Message.DOMA4132, env, classElement,
                            new Object[] { classElement.getQualifiedName() });
                }
                if (classElement.getNestingKind().isNested()) {
                    validateEnclosingElement(classElement);
                }
            } else if (classElement.getKind() == ElementKind.ENUM) {
                if (holderMeta.providesConstructor()) {
                    HolderReflection holderReflection = holderMeta.getHolderReflection();
                    throw new AptException(Message.DOMA4184, env, classElement,
                            holderReflection.getAnnotationMirror(),
                            holderReflection.getFactoryMethod(),
                            new Object[] { classElement.getQualifiedName() });
                }
                if (classElement.getNestingKind().isNested()) {
                    validateEnclosingElement(classElement);
                }
            } else if (classElement.getKind() == ElementKind.INTERFACE) {
                if (holderMeta.providesConstructor()) {
                    throw new AptException(Message.DOMA4268, env, classElement,
                            new Object[] { classElement.getQualifiedName() });
                }
                if (classElement.getNestingKind().isNested()) {
                    validateEnclosingElement(classElement);
                }
            } else {
                HolderReflection holderReflection = holderMeta.getHolderReflection();
                throw new AptException(Message.DOMA4105, env, classElement,
                        holderReflection.getAnnotationMirror(),
                        new Object[] { classElement.getQualifiedName() });
            }
        }

        protected void validateEnclosingElement(Element element) {
            TypeElement typeElement = ElementUtil.toTypeElement(element, env);
            if (typeElement == null) {
                return;
            }
            String simpleName = typeElement.getSimpleName().toString();
            if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
                    || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
                throw new AptException(Message.DOMA4277, env, typeElement,
                        new Object[] { typeElement.getQualifiedName() });
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
                    throw new AptException(Message.DOMA4275, env, typeElement,
                            new Object[] { typeElement.getQualifiedName() });
                }
            } else {
                throw new AptException(Message.DOMA4276, env, typeElement,
                        new Object[] { typeElement.getQualifiedName() });
            }
        }

        @Override
        public void validateInitializer(TypeElement classElement,
                HolderMeta holderMeta) {
            if (holderMeta.providesConstructor()) {
                validateConstructor(classElement, holderMeta);
            } else {
                validateFactoryMethod(classElement, holderMeta);
            }
        }

        protected void validateConstructor(TypeElement classElement,
                HolderMeta holderMeta) {
            for (ExecutableElement constructor : ElementFilter
                    .constructorsIn(classElement.getEnclosedElements())) {
                if (constructor.getModifiers().contains(Modifier.PRIVATE)) {
                    continue;
                }
                List<? extends VariableElement> parameters = constructor
                        .getParameters();
                if (parameters.size() != 1) {
                    continue;
                }
                TypeMirror parameterType = env.getTypeUtils().erasure(
                        parameters.get(0).asType());
                if (env.getTypeUtils().isSameType(parameterType,
                        holderMeta.getValueType())) {
                    return;
                }
            }
            throw new AptException(Message.DOMA4103, env, classElement,
                    new Object[] { holderMeta.getValueType(),
                            classElement.getQualifiedName() });
        }

        protected void validateFactoryMethod(TypeElement classElement,
                HolderMeta holderMeta) {
            outer: for (ExecutableElement method : ElementFilter
                    .methodsIn(classElement.getEnclosedElements())) {
                if (!method.getSimpleName().contentEquals(
                        holderMeta.getFactoryMethod())) {
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
                TypeMirror parameterType = method.getParameters().get(0)
                        .asType();
                if (!env.getTypeUtils().isAssignable(holderMeta.getValueType(),
                        parameterType)) {
                    continue;
                }
                TypeMirror returnType = env.getTypeUtils().erasure(
                        method.getReturnType());
                if (!env.getTypeUtils().isAssignable(returnType,
                        holderMeta.getType())) {
                    continue;
                }
                List<? extends TypeParameterElement> classTypeParams = classElement
                        .getTypeParameters();
                List<? extends TypeParameterElement> methodTypeParams = method
                        .getTypeParameters();
                if (classTypeParams.size() != methodTypeParams.size()) {
                    continue;
                }
                for (Iterator<? extends TypeParameterElement> cit = classTypeParams
                        .iterator(), mit = methodTypeParams.iterator(); cit
                        .hasNext() && mit.hasNext();) {
                    TypeParameterElement classTypeParam = cit.next();
                    TypeParameterElement methodTypeParam = mit.next();
                    if (!TypeMirrorUtil.isSameType(classTypeParam.asType(),
                            methodTypeParam.asType(), env)) {
                        continue outer;
                    }
                }
                return;
            }
            throw new AptException(Message.DOMA4106, env, classElement,
                    new Object[] { holderMeta.getFactoryMethod(),
                            classElement.asType(), holderMeta.getValueType(),
                            holderMeta.getFactoryMethod(),
                            classElement.getQualifiedName() });
        }

        @Override
        public void validateAccessorMethod(TypeElement classElement,
                HolderMeta holderMeta) {
            TypeElement typeElement = classElement;
            TypeMirror typeMirror = classElement.asType();
            for (; typeElement != null && typeMirror.getKind() != TypeKind.NONE;) {
                for (ExecutableElement method : ElementFilter
                        .methodsIn(typeElement.getEnclosedElements())) {
                    if (!method.getSimpleName().contentEquals(
                            holderMeta.getAccessorMethod())) {
                        continue;
                    }
                    if (method.getModifiers().contains(Modifier.PRIVATE)) {
                        continue;
                    }
                    if (!method.getParameters().isEmpty()) {
                        continue;
                    }
                    TypeMirror returnType = method.getReturnType();
                    if (env.getTypeUtils().isAssignable(
                            env.getTypeUtils().erasure(returnType),
                            holderMeta.getValueType())) {
                        return;
                    }
                    TypeVariable typeVariable = TypeMirrorUtil.toTypeVariable(
                            returnType, env);
                    if (typeVariable != null) {
                        TypeMirror inferredReturnType = inferType(typeVariable,
                                typeElement, typeMirror);
                        if (inferredReturnType != null) {
                            if (env.getTypeUtils().isAssignable(
                                    inferredReturnType,
                                    holderMeta.getValueType())) {
                                return;
                            }
                        }
                    }
                }
                typeMirror = typeElement.getSuperclass();
                typeElement = TypeMirrorUtil.toTypeElement(typeMirror, env);
            }
            throw new AptException(Message.DOMA4104, env, classElement,
                    new Object[] { holderMeta.getAccessorMethod(),
                            holderMeta.getValueType(),
                            classElement.getQualifiedName() });
        }

        protected TypeMirror inferType(TypeVariable typeVariable,
                TypeElement classElement, TypeMirror classMirror) {
            DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(
                    classMirror, env);
            if (declaredType == null) {
                return null;
            }
            List<? extends TypeMirror> args = declaredType.getTypeArguments();
            if (args.isEmpty()) {
                return null;
            }
            int argsSize = args.size();
            int index = 0;
            for (TypeParameterElement typeParam : classElement
                    .getTypeParameters()) {
                if (index >= argsSize) {
                    break;
                }
                if (TypeMirrorUtil.isSameType(typeVariable, typeParam.asType(),
                        env)) {
                    return args.get(index);
                }
                index++;
            }
            return null;
        }

    }

    protected static class ValueStragety extends DefaultStrategy {

        protected final ValueReflection valueReflection;

        public ValueStragety(ProcessingEnvironment env, ValueReflection valueReflection) {
            super(env);
            assertNotNull(valueReflection);
            this.valueReflection = valueReflection;
        }

        @Override
        public void validateInitializer(TypeElement classElement,
                HolderMeta holderMeta) {
            if (!valueReflection.getStaticConstructorValue().isEmpty()) {
                throw new AptException(Message.DOMA4428, env, classElement,
                        valueReflection.getAnnotationMirror(),
                        valueReflection.getStaticConstructor(),
                        new Object[] { classElement.getQualifiedName() });
            }
        }

        @Override
        public void validateAccessorMethod(TypeElement classElement,
                HolderMeta holderMeta) {
            VariableElement field = findSingleField(classElement, holderMeta);
            String accessorMethod = inferAccessorMethod(field);
            if (!accessorMethod.equals(holderMeta.getAccessorMethod())) {
                HolderReflection holderReflection = holderMeta.getHolderReflection();
                throw new AptException(Message.DOMA4429, env, classElement,
                        holderReflection.getAnnotationMirror(),
                        holderReflection.getAccessorMethod(), new Object[] {
                                accessorMethod, holderMeta.getAccessorMethod(),
                                classElement.getQualifiedName() });
            }
        }

        protected String inferAccessorMethod(VariableElement field) {
            String name = field.getSimpleName().toString();
            String capitalizedName = StringUtil.capitalize(name);
            if (field.asType().getKind() == TypeKind.BOOLEAN) {
                if (name.startsWith("is") && (name.length() > 2
                        && Character.isUpperCase(name.charAt(2)))) {
                    return name;
                }
                return "is" + capitalizedName;
            }
            return "get" + capitalizedName;
        }

        protected VariableElement findSingleField(TypeElement classElement,
                HolderMeta holderMeta) {
            List<VariableElement> fields = ElementFilter
                    .fieldsIn(classElement.getEnclosedElements())
                    .stream()
                    .filter(field -> !field.getModifiers().contains(
                            Modifier.STATIC)).collect(Collectors.toList());
            if (fields.size() == 0) {
                throw new AptException(Message.DOMA4430, env, classElement,
                        new Object[] { classElement.getQualifiedName() });
            }
            if (fields.size() > 1) {
                throw new AptException(Message.DOMA4431, env, classElement,
                        new Object[] { classElement.getQualifiedName() });
            }
            VariableElement field = fields.get(0);
            if (!TypeMirrorUtil.isAssignable(field.asType(),
                    holderMeta.getValueType(), env)) {
                throw new AptException(Message.DOMA4432, env, field,
                        new Object[] { field.asType(),
                                holderMeta.getValueType(),
                                classElement.getQualifiedName(),
                                field.getSimpleName() });
            }
            return field;
        }

    }

}
