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
package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Holder;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.reflection.HolderConvertersReflection;
import org.seasar.doma.internal.apt.reflection.Reflections;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.holder.HolderConverter;
import org.seasar.doma.message.Message;

public class HolderCtType extends AbstractCtType {

    protected final BasicCtType basicCtType;

    protected final boolean external;

    protected final String metaClassName;

    private final String typeArgDecl;

    private boolean isRawType;

    private boolean isWildcardType;

    public HolderCtType(TypeMirror holderType, ProcessingEnvironment env,
            BasicCtType basicCtType, boolean external) {
        super(holderType, env);
        assertNotNull(basicCtType);
        this.basicCtType = basicCtType;
        this.external = external;
        int pos = metaTypeName.indexOf('<');
        if (pos > -1) {
            this.metaClassName = metaTypeName.substring(0, pos);
            this.typeArgDecl = metaTypeName.substring(pos);
        } else {
            this.metaClassName = metaTypeName;
            this.typeArgDecl = "";
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

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

    public boolean isRawType() {
        return isRawType;
    }

    public boolean isWildcardType() {
        return isWildcardType;
    }

    public String getInstantiationCommand() {
        return normalize(metaClassName) + "." + typeArgDecl
                + "getSingletonInternal()";
    }

    @Override
    public String getMetaTypeName() {
        return normalize(metaTypeName);
    }

    protected String normalize(String name) {
        if (external) {
            return Constants.EXTERNAL_HOLDER_METATYPE_ROOT_PACKAGE + "." + name;
        }
        return name;
    }

    public static HolderCtType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return null;
        }
        HolderInfo info = getHolderInfo(typeElement, env);
        if (info == null) {
            return null;
        }
        BasicCtType basicCtType = BasicCtType.newInstance(info.valueType, env);
        if (basicCtType == null) {
            return null;
        }
        return new HolderCtType(type, env, basicCtType, info.external);
    }

    protected static HolderInfo getHolderInfo(TypeElement typeElement,
            ProcessingEnvironment env) {
        Holder holder = typeElement.getAnnotation(Holder.class);
        if (holder != null) {
            return getHolderInfo(typeElement, holder);
        }
        return getExternalHolderInfo(typeElement, env);
    }

    protected static HolderInfo getHolderInfo(TypeElement typeElement,
            Holder holder) {
        try {
            holder.valueType();
        } catch (MirroredTypeException e) {
            return new HolderInfo(e.getTypeMirror(), false);
        }
        throw new AptIllegalStateException("unreachable.");
    }

    protected static HolderInfo getExternalHolderInfo(TypeElement typeElement,
            ProcessingEnvironment env) {
        String csv = Options.getHolderConverters(env);
        if (csv != null) {
            TypeMirror holderType = typeElement.asType();
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
                HolderConvertersReflection convertersMirror = new Reflections(
                        env).newHolderConvertersReflection(
                                convertersProviderElement);
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
                            || !TypeMirrorUtil.isSameType(holderType,
                                    argTypes[0], env)) {
                        continue;
                    }
                    TypeMirror valueType = argTypes[1];
                    return new HolderInfo(valueType, true);
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
            if (!TypeMirrorUtil.isAssignable(supertype, HolderConverter.class,
                    env)) {
                continue;
            }
            if (TypeMirrorUtil
                    .isSameType(supertype, HolderConverter.class, env)) {
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
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitHolderCtType(this, p);
    }

    private static class HolderInfo {
        private final TypeMirror valueType;

        private final boolean external;

        public HolderInfo(TypeMirror valueType, boolean external) {
            assertNotNull(valueType);
            this.valueType = valueType;
            this.external = external;
        }
    }

}
