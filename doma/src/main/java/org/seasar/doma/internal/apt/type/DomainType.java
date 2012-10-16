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

import java.util.List;

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
import org.seasar.doma.internal.jdbc.util.MetaTypeUtil;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.message.Message;

@SuppressWarnings("deprecation")
public class DomainType extends AbstractDataType {

    protected final BasicType basicType;

    protected final TypeMirror convType;

    protected final String convMetaTypeName;

    protected final String convMetaTypeNameAsTypeParameter;

    public DomainType(TypeMirror domainType, ProcessingEnvironment env,
            TypeMirror convType, BasicType basicType) {
        super(domainType, env);
        assertNotNull(convType, basicType);
        this.convType = convType;
        this.basicType = basicType;
        String convTypeName = TypeMirrorUtil.getTypeName(convType, env);
        String convTypeNameAsTypeParameter = TypeMirrorUtil
                .getTypeNameAsTypeParameter(convType, env);
        this.convMetaTypeName = MetaTypeUtil.getMetaTypeName(convTypeName);
        this.convMetaTypeNameAsTypeParameter = MetaTypeUtil
                .getMetaTypeName(convTypeNameAsTypeParameter);
    }

    public BasicType getBasicType() {
        return basicType;
    }

    @Override
    public String getMetaTypeName() {
        return convMetaTypeName;
    }

    @Override
    public String getMetaTypeNameAsTypeParameter() {
        return convMetaTypeNameAsTypeParameter;
    }

    public static DomainType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return null;
        }
        DomainInfo info = getDomainInfo(typeElement, env);
        if (info == null) {
            return null;
        }
        BasicType basicType = BasicType.newInstance(info.valueType, env);
        if (basicType == null) {
            return null;
        }
        return new DomainType(type, env, info.convType, basicType);
    }

    protected static DomainInfo getDomainInfo(TypeElement typeElement,
            ProcessingEnvironment env) {
        Domain domain = typeElement.getAnnotation(Domain.class);
        if (domain != null) {
            return getDomainInfo(typeElement, domain);
        }
        EnumDomain enumDomain = typeElement.getAnnotation(EnumDomain.class);
        if (enumDomain != null) {
            return getEnumDomainInfo(typeElement, enumDomain);
        }
        return getExternalDomainInfo(typeElement, env);
    }

    protected static DomainInfo getDomainInfo(TypeElement typeElement,
            Domain domain) {
        try {
            domain.valueType();
        } catch (MirroredTypeException e) {
            return new DomainInfo(e.getTypeMirror(), typeElement.asType());
        }
        throw new AptIllegalStateException("unreachable.");
    }

    protected static DomainInfo getEnumDomainInfo(TypeElement typeElement,
            EnumDomain enumDomain) {
        try {
            enumDomain.valueType();
        } catch (MirroredTypeException e) {
            return new DomainInfo(e.getTypeMirror(), typeElement.asType());
        }
        throw new AptIllegalStateException("unreachable.");
    }

    protected static DomainInfo getExternalDomainInfo(TypeElement typeElement,
            ProcessingEnvironment env) {
        String csv = Options.getDomainConverters(env);
        if (csv != null) {
            TypeMirror domainType = typeElement.asType();
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
                TypeMirror convType = e.asType();
                TypeMirror[] argumentTypes = getConverterArgumentTypes(
                        convType, env);
                if (argumentTypes == null
                        || !TypeMirrorUtil.isSameType(domainType,
                                argumentTypes[0], env)) {
                    continue;
                }
                return new DomainInfo(argumentTypes[1], convType);
            }
        }
        return null;
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

    private static class DomainInfo {
        private final TypeMirror valueType;

        private final TypeMirror convType;

        public DomainInfo(TypeMirror valueType, TypeMirror convType) {
            assertNotNull(valueType, convType);
            this.valueType = valueType;
            this.convType = convType;
        }
    }
}
