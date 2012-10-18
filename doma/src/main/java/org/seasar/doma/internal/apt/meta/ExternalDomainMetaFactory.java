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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.mirror.EnumDomainMirror;
import org.seasar.doma.internal.apt.type.BasicType;
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
        TypeMirror[] argTypes = getConverterArgTypes(convElement
                .asType());
        if (argTypes == null) {
            throw new AptIllegalStateException(
                    "converter doesn't have type args: "
                            + convElement.getQualifiedName());
        }
        TypeMirror domainType = argTypes[0];
        TypeElement domainElement = TypeMirrorUtil.toTypeElement(domainType,
                env);
        if (domainElement == null) {
            throw new AptIllegalStateException(domainType.toString());
        }
        ExternalDomainMeta meta = new ExternalDomainMeta(convElement);
        meta.setDomainElement(domainElement);

        TypeMirror valueType = argTypes[1];
        TypeElement valueElement = TypeMirrorUtil.toTypeElement(valueType, env);
        if (valueElement == null) {
            throw new AptIllegalStateException(valueType.toString());
        }
        meta.setValueElement(valueElement);

        BasicType basicType = BasicType.newInstance(valueType, env);
        if (basicType == null) {
            throw new AptException(Message.DOMA4194, env, convElement,
                    valueElement.getQualifiedName());
        }
        if (basicType.isEnum()) {
            EnumDomainMirror enumDomainMirror = EnumDomainMirror.newInstance(
                    valueElement, env);
            if (enumDomainMirror != null) {
                throw new AptException(Message.DOMA4195, env, convElement,
                        basicType.getQualifiedName());
            }
        }
        meta.setWrapperType(basicType.getWrapperType());
        return meta;
    }

    protected void validateConverter(TypeElement classElement) {
        if (!TypeMirrorUtil.isAssignable(classElement.asType(),
                DomainConverter.class, env)) {
            throw new AptException(Message.DOMA4191, env, classElement);
        }
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4192, env, classElement,
                    classElement.getQualifiedName());
        }
        ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                classElement, env);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4193, env, classElement,
                    classElement.getQualifiedName());
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
}
