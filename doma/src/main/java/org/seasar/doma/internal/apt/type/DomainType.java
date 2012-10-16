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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Domain;
import org.seasar.doma.EnumDomain;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.AptOptionException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.message.Message;

@SuppressWarnings("deprecation")
public class DomainType extends AbstractDataType {

    protected BasicType basicType;

    public DomainType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public BasicType getBasicType() {
        return basicType;
    }

    public static DomainType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return null;
        }
        TypeMirror valueTypeMirror = getValueType(typeElement, env);
        if (valueTypeMirror == null) {
            return null;
        }
        BasicType basicType = BasicType.newInstance(valueTypeMirror, env);
        if (basicType == null) {
            return null;
        }
        DomainType domainType = new DomainType(type, env);
        domainType.basicType = basicType;
        return domainType;
    }

    protected static TypeMirror getValueType(TypeElement typeElement,
            ProcessingEnvironment env) {
        Domain domain = typeElement.getAnnotation(Domain.class);
        if (domain != null) {
            return getValueType(domain);
        }
        EnumDomain enumDomain = typeElement.getAnnotation(EnumDomain.class);
        if (enumDomain != null) {
            return getValueType(enumDomain);
        }
        return getExternalDomainBoundValueType(typeElement, env);
    }

    protected static TypeMirror getValueType(Domain domain) {
        try {
            domain.valueType();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException("unreachable.");
    }

    protected static TypeMirror getValueType(EnumDomain enumDomain) {
        try {
            enumDomain.valueType();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException("unreachable.");
    }

    protected static TypeMirror getExternalDomainBoundValueType(
            TypeElement typeElement, ProcessingEnvironment env) {
        Map<String, TypeMirror> valueTypeMap = createValueTypeMap(env);
        return valueTypeMap.get(typeElement.getQualifiedName().toString());
    }

    protected static Map<String, TypeMirror> createValueTypeMap(
            ProcessingEnvironment env) {
        Map<String, TypeMirror> valueTypeMap = new HashMap<String, TypeMirror>();
        String csv = Options.getDomainConverters(env);
        if (csv != null) {
            for (String value : csv.split(",")) {
                String className = value.trim();
                if (className.isEmpty()) {
                    continue;
                }
                TypeElement e = ElementUtil.getTypeElement(className, env);
                if (e == null) {
                    throw new AptOptionException(
                            Message.DOMA4200.getMessage(className));
                }
                TypeMirror[] argumentTypes = getConverterArgumentTypes(
                        e.asType(), env);
                if (argumentTypes == null) {
                    continue;
                }
                TypeMirror domainType = argumentTypes[0];
                TypeMirror valueType = argumentTypes[1];
                TypeElement domainElement = TypeMirrorUtil.toTypeElement(
                        domainType, env);
                if (domainElement == null) {
                    continue;
                }
                valueTypeMap.put(domainElement.getQualifiedName().toString(),
                        valueType);
            }
        }
        return valueTypeMap;
    }

    protected static TypeMirror[] getConverterArgumentTypes(
            TypeMirror typeMirror, ProcessingEnvironment env) {
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
            TypeMirror[] argumentTypes = getConverterArgumentTypes(supertype,
                    env);
            if (argumentTypes != null) {
                return argumentTypes;
            }
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainType(this, p);
    }
}
