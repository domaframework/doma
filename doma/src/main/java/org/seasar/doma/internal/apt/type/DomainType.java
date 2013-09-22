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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Domain;
import org.seasar.doma.EnumDomain;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.mirror.DomainConvertersMirror;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.message.Message;

@SuppressWarnings("deprecation")
public class DomainType extends AbstractDataType {

    protected final BasicType basicType;

    protected final boolean external;

    protected final String metaClassName;

    private final String typeParamDecl;

    private boolean isRawType;

    private boolean isWildcardType;

    public DomainType(TypeMirror domainType, ProcessingEnvironment env,
            BasicType basicType, boolean external) {
        super(domainType, env);
        assertNotNull(basicType);
        this.basicType = basicType;
        this.external = external;
        int pos = metaTypeName.indexOf('<');
        if (pos > -1) {
            this.metaClassName = metaTypeName.substring(0, pos);
            this.typeParamDecl = metaTypeName.substring(pos);
        } else {
            this.metaClassName = metaTypeName;
            this.typeParamDecl = "";
        }
        if (!typeElement.getTypeParameters().isEmpty()) {
            DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(
                    getTypeMirror(), env);
            if (declaredType == null) {
                throw new AptIllegalStateException(getTypeName());
            }
            if (declaredType.getTypeArguments().isEmpty()) {
                isRawType = true;
            }
            for (TypeMirror typeArg : declaredType.getTypeArguments()) {
                if (typeArg.getKind() == TypeKind.WILDCARD
                        || typeArg.getKind() == TypeKind.TYPEVAR) {
                    isWildcardType = true;
                }
            }
        }
    }

    public BasicType getBasicType() {
        return basicType;
    }

    public boolean isRawType() {
        return isRawType;
    }

    public boolean isWildcardType() {
        return isWildcardType;
    }

    public String getInstantiationCommand() {
        return normalize(metaClassName) + "." + typeParamDecl
                + "getSingletonInternal()";
    }

    @Override
    public String getMetaTypeName() {
        return normalize(metaTypeName);
    }

    @Override
    public String getMetaTypeNameAsTypeParameter() {
        return normalize(metaTypeNameAsTypeParameter);
    }

    protected String normalize(String name) {
        if (external) {
            return Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE + "." + name;
        }
        return name;
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
        return new DomainType(type, env, basicType, info.external);
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
            return new DomainInfo(e.getTypeMirror(), false);
        }
        throw new AptIllegalStateException("unreachable.");
    }

    protected static DomainInfo getEnumDomainInfo(TypeElement typeElement,
            EnumDomain enumDomain) {
        try {
            enumDomain.valueType();
        } catch (MirroredTypeException e) {
            return new DomainInfo(e.getTypeMirror(), false);
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
                TypeElement convertersProviderElement = ElementUtil
                        .getTypeElement(className, env);
                if (convertersProviderElement == null) {
                    throw new AptIllegalOptionException(
                            Message.DOMA4200.getMessage(className));
                }
                DomainConvertersMirror convertersMirror = DomainConvertersMirror
                        .newInstance(convertersProviderElement, env);
                if (convertersMirror == null) {
                    throw new AptIllegalOptionException(
                            Message.DOMA4201.getMessage(className));
                }
                for (TypeMirror converterType : convertersMirror
                        .getValueValue()) {
                    // converterType does not contain adequate information in
                    // eclipse incremental compile, so reload typeMirror
                    converterType = reloadTypeMirror(converterType, env);
                    if (converterType == null) {
                        continue;
                    }
                    TypeMirror[] argTypes = getConverterArgTypes(converterType,
                            env);
                    if (argTypes == null
                            || !TypeMirrorUtil.isSameType(domainType,
                                    argTypes[0], env)) {
                        continue;
                    }
                    TypeMirror valueType = argTypes[1];
                    return new DomainInfo(valueType, true);
                }
            }
        }
        return null;
    }

    protected static TypeMirror reloadTypeMirror(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(typeMirror, env);
        if (typeElement == null) {
            return null;
        }
        String binaryName = ElementUtil.getBinaryName(typeElement, env);
        typeElement = ElementUtil.getTypeElement(binaryName, env);
        if (typeElement == null) {
            return null;
        }
        return typeElement.asType();
    }

    protected static TypeMirror[] getConverterArgTypes(TypeMirror typeMirror,
            ProcessingEnvironment env) {
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
            TypeMirror[] argTypes = getConverterArgTypes(supertype, env);
            if (argTypes != null) {
                return argTypes;
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

        private final boolean external;

        public DomainInfo(TypeMirror valueType, boolean external) {
            assertNotNull(valueType);
            this.valueType = valueType;
            this.external = external;
        }
    }

}
