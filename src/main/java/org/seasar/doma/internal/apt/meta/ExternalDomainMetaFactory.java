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

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class ExternalDomainMetaFactory implements
        TypeElementMetaFactory<ExternalDomainMeta> {

    private final ProcessingEnvironment env;

    public ExternalDomainMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    @Override
    public ExternalDomainMeta createTypeElementMeta(TypeElement convElement) {
        validateConverter(convElement);
        TypeMirror[] argTypes = getConverterArgTypes(convElement.asType());
        if (argTypes == null) {
            throw new AptIllegalStateException(
                    "converter doesn't have type args: "
                            + convElement.getQualifiedName());
        }
        ExternalDomainMeta meta = new ExternalDomainMeta(convElement);
        doDomainType(convElement, argTypes[0], meta);
        doValueType(convElement, argTypes[1], meta);
        return meta;
    }

    protected void validateConverter(TypeElement convElement) {
        if (!TypeMirrorUtil.isAssignable(convElement.asType(),
                DomainConverter.class, env)) {
            throw new AptException(Message.DOMA4191, env, convElement,
                    new Object[] { convElement.getQualifiedName() });
        }
        if (convElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4198, env, convElement,
                    new Object[] { convElement.getQualifiedName() });
        }
        if (convElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4192, env, convElement,
                    new Object[] { convElement.getQualifiedName() });
        }
        ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                convElement, env);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4193, env, convElement,
                    new Object[] { convElement.getQualifiedName() });
        }
    }

    protected TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(
                typeMirror)) {
            if (!TypeMirrorUtil.isAssignable(supertype, DomainConverter.class,
                    env)) {
                continue;
            }
            if (TypeMirrorUtil
                    .isSameType(supertype, DomainConverter.class, env)) {
                DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(
                        supertype, env);
                assertNotNull(declaredType);
                List<? extends TypeMirror> args = declaredType
                        .getTypeArguments();
                assertEquals(2, args.size());
                return new TypeMirror[] { args.get(0), args.get(1) };
            }
            TypeMirror[] argTypes = getConverterArgTypes(supertype);
            if (argTypes != null) {
                return argTypes;
            }
        }
        return null;
    }

    protected void doDomainType(TypeElement convElement, TypeMirror domainType,
            ExternalDomainMeta meta) {
        TypeElement domainElement = TypeMirrorUtil.toTypeElement(domainType,
                env);
        if (domainElement == null) {
            throw new AptIllegalStateException(domainType.toString());
        }
        if (domainElement.getNestingKind().isNested()) {
            validateEnclosingElement(domainElement);
        }
        PackageElement pkgElement = env.getElementUtils().getPackageOf(
                domainElement);
        if (pkgElement.isUnnamed()) {
            throw new AptException(Message.DOMA4197, env, convElement,
                    new Object[] { domainElement.getQualifiedName(),
                            convElement.getQualifiedName() });
        }
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(domainType,
                env);
        if (declaredType == null) {
            throw new AptIllegalStateException(domainType.toString());
        }
        for (TypeMirror typeArg : declaredType.getTypeArguments()) {
            if (typeArg.getKind() != TypeKind.WILDCARD) {
                throw new AptException(Message.DOMA4203, env, convElement,
                        new Object[] { domainElement.getQualifiedName(),
                                convElement.getQualifiedName() });
            }
        }
        meta.setDomainElement(domainElement);
    }

    protected void validateEnclosingElement(Element element) {
        TypeElement typeElement = ElementUtil.toTypeElement(element, env);
        if (typeElement == null) {
            return;
        }
        String simpleName = typeElement.getSimpleName().toString();
        if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
                || simpleName.contains(Constants.METATYPE_NAME_DELIMITER)) {
            throw new AptException(Message.DOMA4280, env, typeElement,
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
                throw new AptException(Message.DOMA4278, env, typeElement,
                        new Object[] { typeElement.getQualifiedName() });
            }
        } else {
            throw new AptException(Message.DOMA4279, env, typeElement,
                    new Object[] { typeElement.getQualifiedName() });
        }
    }

    protected void doValueType(TypeElement convElement, TypeMirror valueType,
            ExternalDomainMeta meta) {
        String valueTypeName = TypeMirrorUtil.getTypeName(valueType, env);
        meta.setValueTypeName(valueTypeName);

        BasicCtType basicCtType = BasicCtType.newInstance(valueType, env);
        if (basicCtType == null) {
            throw new AptException(Message.DOMA4194, env, convElement,
                    new Object[] { valueTypeName,
                            convElement.getQualifiedName() });
        }
        meta.setWrapperCtType(basicCtType.getWrapperCtType());
    }
}
