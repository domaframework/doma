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
package org.seasar.doma.internal.apt.meta.holder;

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
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.CanonicalName;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.reflection.HolderReflection;
import org.seasar.doma.internal.apt.reflection.ValueReflection;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.message.Message;

public class HolderMetaFactory implements TypeElementMetaFactory<HolderMeta> {

    private final Context ctx;

    private final TypeElement holderElement;

    private final HolderReflection holderReflection;

    public HolderMetaFactory(Context ctx, TypeElement classElement) {
        assertNotNull(ctx);
        this.ctx = ctx;
        this.holderElement = classElement;
        holderReflection = ctx.getReflections().newHolderReflection(classElement);
        if (holderReflection == null) {
            throw new AptIllegalStateException("holderReflection");
        }
    }

    @Override
    public HolderMeta createTypeElementMeta() {
        assertNotNull(holderElement);
        BasicCtType basicCtType = createBasicCtType();
        CanonicalName holderDescCanonicalName = createHolderDescCanonicalName();
        HolderMeta holderMeta = new HolderMeta(holderElement, holderElement.asType(),
                holderReflection, basicCtType, holderDescCanonicalName);
        Strategy strategy = createStrategy();
        strategy.validateAcceptNull(holderMeta);
        strategy.validateClass(holderMeta);
        strategy.validateInitializer(holderMeta);
        strategy.validateAccessorMethod(holderMeta);
        return holderMeta;
    }

    private BasicCtType createBasicCtType() {
        TypeMirror valueType = holderReflection.getValueTypeValue();
        BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(valueType);
        if (basicCtType == null) {
            throw new AptException(Message.DOMA4102, holderElement,
                    holderReflection.getAnnotationMirror(), holderReflection.getValueType(),
                    new Object[] { valueType });
        }
        return basicCtType;
    }

    private CanonicalName createHolderDescCanonicalName() {
        HolderDescCanonicalNameFactory factory = new HolderDescCanonicalNameFactory(ctx,
                holderElement);
        return factory.create();
    }

    private Strategy createStrategy() {
        ValueReflection valueReflection = ctx.getReflections().newValueReflection(holderElement);
        if (valueReflection != null) {
            return new ValueStragety(valueReflection);
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
                HolderReflection holderReflection = holderMeta.getHolderReflection();
                throw new AptException(Message.DOMA4251, holderElement,
                        holderReflection.getAnnotationMirror(), holderReflection.getAcceptNull());
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
                    HolderReflection holderReflection = holderMeta.getHolderReflection();
                    throw new AptException(Message.DOMA4184, holderElement,
                            holderReflection.getAnnotationMirror(),
                            holderReflection.getFactoryMethod());
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
                HolderReflection holderReflection = holderMeta.getHolderReflection();
                throw new AptException(Message.DOMA4105, holderElement,
                        holderReflection.getAnnotationMirror());
            }
        }

        private void validateEnclosingElement(Element element) {
            TypeElement typeElement = ctx.getElements().toTypeElement(element);
            if (typeElement == null) {
                return;
            }
            String simpleName = typeElement.getSimpleName().toString();
            if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
                    || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
                throw new AptException(Message.DOMA4277, typeElement,
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
                    throw new AptException(Message.DOMA4275, typeElement,
                            new Object[] { typeElement.getQualifiedName() });
                }
            } else {
                throw new AptException(Message.DOMA4276, typeElement,
                        new Object[] { typeElement.getQualifiedName() });
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
            for (ExecutableElement constructor : ElementFilter
                    .constructorsIn(holderElement.getEnclosedElements())) {
                if (constructor.getModifiers().contains(Modifier.PRIVATE)) {
                    continue;
                }
                List<? extends VariableElement> parameters = constructor.getParameters();
                if (parameters.size() != 1) {
                    continue;
                }
                TypeMirror parameterType = parameters.get(0).asType();
                if (ctx.getTypes().isSameType(parameterType, holderMeta.getValueType())) {
                    return;
                }
            }
            throw new AptException(Message.DOMA4103, holderElement,
                    new Object[] { holderMeta.getValueType() });
        }

        private void validateFactoryMethod(HolderMeta holderMeta) {
            outer: for (ExecutableElement method : ElementFilter
                    .methodsIn(holderElement.getEnclosedElements())) {
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
                TypeMirror parameterType = method.getParameters().get(0).asType();
                if (!ctx.getTypes().isAssignable(holderMeta.getValueType(), parameterType)) {
                    continue;
                }
                if (!ctx.getTypes().isAssignable(method.getReturnType(), holderMeta.getType())) {
                    continue;
                }
                List<? extends TypeParameterElement> classTypeParams = holderElement
                        .getTypeParameters();
                List<? extends TypeParameterElement> methodTypeParams = method.getTypeParameters();
                if (classTypeParams.size() != methodTypeParams.size()) {
                    continue;
                }
                for (Iterator<? extends TypeParameterElement> cit = classTypeParams
                        .iterator(), mit = methodTypeParams.iterator(); cit.hasNext()
                                && mit.hasNext();) {
                    TypeParameterElement classTypeParam = cit.next();
                    TypeParameterElement methodTypeParam = mit.next();
                    if (!ctx.getTypes().isSameType(classTypeParam.asType(),
                            methodTypeParam.asType())) {
                        continue outer;
                    }
                }
                return;
            }
            throw new AptException(Message.DOMA4106, holderElement,
                    new Object[] { holderMeta.getFactoryMethod(), holderElement.asType(),
                            holderMeta.getValueType() });
        }

        @Override
        public void validateAccessorMethod(HolderMeta holderMeta) {
            TypeElement typeElement = holderElement;
            TypeMirror typeMirror = holderElement.asType();
            for (; typeElement != null && typeMirror.getKind() != TypeKind.NONE;) {
                for (ExecutableElement method : ElementFilter
                        .methodsIn(typeElement.getEnclosedElements())) {
                    if (!method.getSimpleName().contentEquals(holderMeta.getAccessorMethod())) {
                        continue;
                    }
                    if (method.getModifiers().contains(Modifier.PRIVATE)) {
                        continue;
                    }
                    if (!method.getParameters().isEmpty()) {
                        continue;
                    }
                    TypeMirror returnType = method.getReturnType();
                    if (ctx.getTypes().isAssignable(returnType, holderMeta.getValueType())) {
                        return;
                    }
                    TypeVariable typeVariable = ctx.getTypes().toTypeVariable(returnType);
                    if (typeVariable != null) {
                        TypeMirror inferredReturnType = inferType(typeVariable, typeElement,
                                typeMirror);
                        if (inferredReturnType != null) {
                            if (ctx.getTypes().isAssignable(inferredReturnType,
                                    holderMeta.getValueType())) {
                                return;
                            }
                        }
                    }
                }
                typeMirror = typeElement.getSuperclass();
                typeElement = ctx.getTypes().toTypeElement(typeMirror);
            }
            throw new AptException(Message.DOMA4104, holderElement,
                    new Object[] { holderMeta.getAccessorMethod(), holderMeta.getValueType() });
        }

        protected TypeMirror inferType(TypeVariable typeVariable, TypeElement classElement,
                TypeMirror classMirror) {
            DeclaredType declaredType = ctx.getTypes().toDeclaredType(classMirror);
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
                if (ctx.getTypes().isSameType(typeVariable, typeParam.asType())) {
                    return args.get(index);
                }
                index++;
            }
            return null;
        }

    }

    protected class ValueStragety extends DefaultStrategy {

        private final ValueReflection valueReflection;

        public ValueStragety(ValueReflection valueReflection) {
            this.valueReflection = valueReflection;
        }

        @Override
        public void validateInitializer(HolderMeta holderMeta) {
            if (!valueReflection.getStaticConstructorValue().isEmpty()) {
                throw new AptException(Message.DOMA4428, holderElement,
                        valueReflection.getAnnotationMirror(),
                        valueReflection.getStaticConstructor());
            }
        }

        @Override
        public void validateAccessorMethod(HolderMeta holderMeta) {
            VariableElement field = findSingleField(holderMeta);
            String accessorMethod = inferAccessorMethod(field);
            if (!accessorMethod.equals(holderMeta.getAccessorMethod())) {
                HolderReflection holderReflection = holderMeta.getHolderReflection();
                throw new AptException(Message.DOMA4429, holderElement,
                        holderReflection.getAnnotationMirror(),
                        holderReflection.getAccessorMethod(),
                        new Object[] { accessorMethod, holderMeta.getAccessorMethod() });
            }
        }

        private String inferAccessorMethod(VariableElement field) {
            String name = field.getSimpleName().toString();
            String capitalizedName = StringUtil.capitalize(name);
            if (field.asType().getKind() == TypeKind.BOOLEAN) {
                if (name.startsWith("is")
                        && (name.length() > 2 && Character.isUpperCase(name.charAt(2)))) {
                    return name;
                }
                return "is" + capitalizedName;
            }
            return "get" + capitalizedName;
        }

        private VariableElement findSingleField(HolderMeta holderMeta) {
            List<VariableElement> fields = ElementFilter
                    .fieldsIn(holderElement.getEnclosedElements())
                    .stream()
                    .filter(field -> !field.getModifiers().contains(Modifier.STATIC))
                    .collect(Collectors.toList());
            if (fields.size() == 0) {
                throw new AptException(Message.DOMA4430, holderElement);
            }
            if (fields.size() > 1) {
                throw new AptException(Message.DOMA4431, holderElement);
            }
            VariableElement field = fields.get(0);
            if (!ctx.getTypes().isAssignable(field.asType(), holderMeta.getValueType())) {
                throw new AptException(Message.DOMA4432, field,
                        new Object[] { field.asType(), holderMeta.getValueType() });
            }
            return field;
        }

    }

}
