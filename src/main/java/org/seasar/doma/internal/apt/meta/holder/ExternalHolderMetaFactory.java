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

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.CanonicalName;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.jdbc.holder.HolderConverter;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class ExternalHolderMetaFactory implements TypeElementMetaFactory<ExternalHolderMeta> {

    private final Context ctx;

    private final TypeElement convElement;

    public ExternalHolderMetaFactory(Context ctx, TypeElement convElement) {
        assertNotNull(ctx, convElement);
        this.ctx = ctx;
        this.convElement = convElement;
    }

    @Override
    public ExternalHolderMeta createTypeElementMeta() {
        validateConverter();
        TypeMirror[] argTypes = getConverterArgTypes(convElement.asType());
        if (argTypes == null) {
            throw new AptIllegalStateException(
                    "converter doesn't have type args: " + convElement.getQualifiedName());
        }
        TypeElement holderElement = createHolderElement(argTypes[0]);
        BasicCtType basicCtType = createBasicCtType(argTypes[1]);
        CanonicalName externalHolderCanonicalName = createExteranlHolderDescCanonicalName(
                holderElement);
        return new ExternalHolderMeta(convElement, holderElement, basicCtType,
                externalHolderCanonicalName);
    }

    private void validateConverter() {
        if (!ctx.getTypes().isAssignable(convElement.asType(), HolderConverter.class)) {
            throw new AptException(Message.DOMA4191, convElement);
        }
        if (convElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4198, convElement);
        }
        if (convElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4192, convElement,
                    new Object[] { convElement.getQualifiedName() });
        }
        ExecutableElement constructor = ctx.getElements().getNoArgConstructor(convElement);
        if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4193, convElement,
                    new Object[] { convElement.getQualifiedName() });
        }
    }

    private TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
        for (TypeMirror supertype : ctx.getTypes().directSupertypes(typeMirror)) {
            if (!ctx.getTypes().isAssignable(supertype, HolderConverter.class)) {
                continue;
            }
            if (ctx.getTypes().isSameType(supertype, HolderConverter.class)) {
                DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
                assertNotNull(declaredType);
                List<? extends TypeMirror> args = declaredType.getTypeArguments();
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

    private TypeElement createHolderElement(TypeMirror holderType) {
        TypeElement holderElement = ctx.getTypes().toTypeElement(holderType);
        if (holderElement == null) {
            throw new AptIllegalStateException(holderType.toString());
        }
        if (holderElement.getNestingKind().isNested()) {
            validateEnclosingElement(holderElement);
        }
        PackageElement pkgElement = ctx.getElements().getPackageOf(holderElement);
        if (pkgElement.isUnnamed()) {
            throw new AptException(Message.DOMA4197, convElement,
                    new Object[] { holderElement.getQualifiedName() });
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(holderType);
        if (declaredType == null) {
            throw new AptIllegalStateException(holderType.toString());
        }
        for (TypeMirror typeArg : declaredType.getTypeArguments()) {
            if (typeArg.getKind() != TypeKind.WILDCARD) {
                throw new AptException(Message.DOMA4203, convElement,
                        new Object[] { holderElement.getQualifiedName() });
            }
        }
        return holderElement;
    }

    private void validateEnclosingElement(Element element) {
        TypeElement typeElement = ctx.getElements().toTypeElement(element);
        if (typeElement == null) {
            return;
        }
        String simpleName = typeElement.getSimpleName().toString();
        if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
                || simpleName.contains(Constants.DESC_NAME_DELIMITER)) {
            throw new AptException(Message.DOMA4280, typeElement,
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
                throw new AptException(Message.DOMA4278, typeElement,
                        new Object[] { typeElement.getQualifiedName() });
            }
        } else {
            throw new AptException(Message.DOMA4279, typeElement,
                    new Object[] { typeElement.getQualifiedName() });
        }
    }

    private BasicCtType createBasicCtType(TypeMirror valueType) {
        BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(valueType);
        if (basicCtType == null) {
            throw new AptException(Message.DOMA4194, convElement, new Object[] { valueType });
        }
        return basicCtType;
    }

    private CanonicalName createExteranlHolderDescCanonicalName(TypeElement holderElement) {
        ExternalHolderDescCanonicalNameFactory factory = new ExternalHolderDescCanonicalNameFactory(
                ctx, holderElement);
        return factory.create();
    }
}
