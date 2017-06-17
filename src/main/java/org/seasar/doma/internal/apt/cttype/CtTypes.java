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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.Holder;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.WrapperCtType.WrapperTypeMappingVisitor;
import org.seasar.doma.internal.apt.reflection.HolderConvertersReflection;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.holder.HolderConverter;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.EnumWrapper;

/**
 * @author nakamura
 *
 */
public class CtTypes {

    private final Context ctx;

    public CtTypes(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public AnyCtType newAnyCtType(TypeMirror type) {
        assertNotNull(type);
        return new AnyCtType(ctx, type);
    }

    public BasicCtType newBasicCtType(TypeMirror type) {
        assertNotNull(type);
        BasicCtType basicCtType = new BasicCtType(ctx, type);
        WrapperCtType wrapperCtType = newWrapperCtType(basicCtType);
        if (wrapperCtType == null) {
            return null;
        }
        basicCtType.wrapperCtType = wrapperCtType;
        return basicCtType;
    }

    public BiFunctionCtType newBiFunctionCtType(TypeMirror type) {
        assertNotNull(type);
        DeclaredType biFunctionDeclaredType = getDeclaredTypeFromHierarchy(type,
                BiFunction.class);
        if (biFunctionDeclaredType == null) {
            return null;
        }

        List<? extends TypeMirror> typeArguments = biFunctionDeclaredType
                .getTypeArguments();
        boolean isRawType = typeArguments.size() != 3;
        CtType firstArgCtType = null;
        CtType secondArgCtType = null;
        AnyCtType resultCtType = null;

        if (!isRawType) {
            TypeMirror firstArgTypeMirror = typeArguments.get(0);
            TypeMirror secondArgTypeMirror = typeArguments.get(1);
            TypeMirror resultTypeMirror = typeArguments.get(2);
            firstArgCtType = toCtType(firstArgTypeMirror,
                    List.of(this::newConfigCtType));
            secondArgCtType = toCtType(secondArgTypeMirror,
                    List.of(this::newPreparedSqlCtType));
            resultCtType = newAnyCtType(resultTypeMirror);
        }

        return new BiFunctionCtType(ctx, type, isRawType, firstArgCtType,
                secondArgCtType, resultCtType);
    }

    public CollectorCtType newCollectorCtType(TypeMirror type) {
        assertNotNull(type);
        DeclaredType collectorDeclaredType = getDeclaredTypeFromHierarchy(type,
                Collector.class);
        if (collectorDeclaredType == null) {
            return null;
        }

        List<? extends TypeMirror> typeArguments = collectorDeclaredType
                .getTypeArguments();
        CtType targetCtType = null;
        AnyCtType returnCtType = null;
        if (typeArguments.size() == 3) {
            TypeMirror targetTypeMirror = typeArguments.get(0);
            TypeMirror returnTypeMirror = typeArguments.get(2);
            targetCtType = toCtType(targetTypeMirror, List.of(
                    this::newEntityCtType, this::newOptionalCtType,
                    this::newOptionalIntCtType, this::newOptionalLongCtType,
                    this::newOptionalDoubleCtType, this::newHolderCtType,
                    this::newBasicCtType, this::newMapCtType));
            returnCtType = newAnyCtType(returnTypeMirror);
        }

        return new CollectorCtType(ctx, type, targetCtType, returnCtType);
    }

    public ConfigCtType newConfigCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, Config.class)) {
            return null;
        }
        return new ConfigCtType(ctx, type);
    }

    public EmbeddableCtType newEmbeddableCtType(TypeMirror type) {
        assertNotNull(type);
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        if (typeElement == null) {
            return null;
        }
        Embeddable embeddable = typeElement.getAnnotation(Embeddable.class);
        if (embeddable == null) {
            return null;
        }
        return new EmbeddableCtType(ctx, type);
    }

    public EntityCtType newEntityCtType(TypeMirror type) {
        assertNotNull(type);
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        if (typeElement == null) {
            return null;
        }
        Entity entity = typeElement.getAnnotation(Entity.class);
        if (entity == null) {
            return null;
        }
        return new EntityCtType(ctx, type, entity.immutable());
    }

    public FunctionCtType newFunctionCtType(TypeMirror type) {
        assertNotNull(type);
        DeclaredType functionDeclaredType = getDeclaredTypeFromHierarchy(type,
                Function.class);
        if (functionDeclaredType == null) {
            return null;
        }

        List<? extends TypeMirror> typeArguments = functionDeclaredType
                .getTypeArguments();
        CtType targetCtType = null;
        AnyCtType returnCtType = null;
        if (typeArguments.size() == 2) {
            TypeMirror targetTypeMirror = typeArguments.get(0);
            TypeMirror returnTypeMirror = typeArguments.get(1);
            targetCtType = toCtType(targetTypeMirror,
                    List.of(this::newStreamCtType, this::newPreparedSqlCtType));
            returnCtType = newAnyCtType(returnTypeMirror);
        }

        return new FunctionCtType(ctx, type, targetCtType, returnCtType);
    }

    public HolderCtType newHolderCtType(TypeMirror type) {
        assertNotNull(type);
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        if (typeElement == null) {
            return null;
        }
        HolderInfo info = getHolderInfo(typeElement);
        if (info == null) {
            return null;
        }
        BasicCtType basicCtType = newBasicCtType(info.valueType);
        if (basicCtType == null) {
            return null;
        }
        return new HolderCtType(ctx, type, basicCtType, info.external);
    }

    protected HolderInfo getHolderInfo(TypeElement typeElement) {
        Holder holder = typeElement.getAnnotation(Holder.class);
        if (holder != null) {
            return getHolderInfo(typeElement, holder);
        }
        return getExternalHolderInfo(typeElement);
    }

    protected HolderInfo getHolderInfo(TypeElement typeElement,
            Holder holder) {
        try {
            holder.valueType();
        } catch (MirroredTypeException e) {
            return new HolderInfo(e.getTypeMirror(), false);
        }
        throw new AptIllegalStateException("unreachable.");
    }

    protected HolderInfo getExternalHolderInfo(TypeElement typeElement) {
        String csv = ctx.getOptions().getHolderConverters();
        if (csv != null) {
            TypeMirror holderType = typeElement.asType();
            for (String value : csv.split(",")) {
                String className = value.trim();
                if (className.isEmpty()) {
                    continue;
                }
                TypeElement convertersProviderElement = ctx.getElements()
                        .getTypeElement(className);
                if (convertersProviderElement == null) {
                    throw new AptIllegalOptionException(
                            Message.DOMA4200.getMessage(className));
                }
                HolderConvertersReflection convertersMirror = ctx
                        .getReflections().newHolderConvertersReflection(
                                convertersProviderElement);
                if (convertersMirror == null) {
                    throw new AptIllegalOptionException(
                            Message.DOMA4201.getMessage(className));
                }
                for (TypeMirror converterType : convertersMirror
                        .getValueValue()) {
                    // converterType does not contain adequate information in
                    // eclipse incremental compile, so reload typeMirror
                    converterType = reloadTypeMirror(converterType);
                    if (converterType == null) {
                        continue;
                    }
                    TypeMirror[] argTypes = getConverterArgTypes(converterType);
                    if (argTypes == null || !ctx.getTypes()
                            .isSameType(holderType, argTypes[0])) {
                        continue;
                    }
                    TypeMirror valueType = argTypes[1];
                    return new HolderInfo(valueType, true);
                }
            }
        }
        return null;
    }

    protected TypeMirror reloadTypeMirror(TypeMirror typeMirror) {
        TypeElement typeElement = ctx.getTypes().toTypeElement(typeMirror);
        if (typeElement == null) {
            return null;
        }
        String binaryName = ctx.getElements().getBinaryName(typeElement)
                .toString();
        typeElement = ctx.getElements().getTypeElement(binaryName);
        if (typeElement == null) {
            return null;
        }
        return typeElement.asType();
    }

    protected TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
        for (TypeMirror supertype : ctx.getTypes()
                .directSupertypes(typeMirror)) {
            if (!ctx.getTypes().isAssignable(supertype,
                    HolderConverter.class)) {
                continue;
            }
            if (ctx.getTypes().isSameType(supertype, HolderConverter.class)) {
                DeclaredType declaredType = ctx.getTypes()
                        .toDeclaredType(supertype);
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

    private static class HolderInfo {
        private final TypeMirror valueType;

        private final boolean external;

        public HolderInfo(TypeMirror valueType, boolean external) {
            assertNotNull(valueType);
            this.valueType = valueType;
            this.external = external;
        }
    }

    public IterableCtType newIterableCtType(TypeMirror type) {
        assertNotNull(type);
        TypeMirror supertype = ctx.getTypes().getSupertypeMirror(type,
                Iterable.class);
        if (supertype == null) {
            return null;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
        if (declaredType == null) {
            return null;
        }
        boolean isList = ctx.getTypes().isSameType(type, List.class);
        TypeMirror elementTypeMirror = null;
        CtType elementCtType = null;
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() > 0) {
            elementTypeMirror = typeArgs.get(0);
            elementCtType = toCtType(elementTypeMirror,
                    List.of(this::newEntityCtType, this::newOptionalCtType,
                            this::newOptionalIntCtType,
                            this::newOptionalLongCtType,
                            this::newOptionalDoubleCtType,
                            this::newHolderCtType, this::newBasicCtType,
                            this::newMapCtType));
        }
        return new IterableCtType(ctx, type, isList, elementTypeMirror,
                elementCtType);
    }

    public MapCtType newMapCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, Map.class)) {
            return null;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() != 2) {
            return null;
        }
        if (!ctx.getTypes().isSameType(typeArgs.get(0), String.class)) {
            return null;
        }
        if (!ctx.getTypes().isSameType(typeArgs.get(1), Object.class)) {
            return null;
        }
        return new MapCtType(ctx, type);
    }

    public OptionalCtType newOptionalCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, Optional.class)) {
            return null;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return null;
        }
        CtType elementCtType;
        boolean isRawType = false;
        boolean isWildcardType = false;
        if (declaredType.getTypeArguments().isEmpty()) {
            isRawType = true;
            elementCtType = null;
        } else {
            TypeMirror typeArg = declaredType.getTypeArguments().get(0);
            if (typeArg.getKind() == TypeKind.WILDCARD
                    || typeArg.getKind() == TypeKind.TYPEVAR) {
                isWildcardType = true;
                elementCtType = null;
            } else {
                elementCtType = toCtType(typeArg,
                        List.of(this::newEntityCtType, this::newHolderCtType,
                                this::newBasicCtType, this::newMapCtType));
            }
        }
        return new OptionalCtType(ctx, type, elementCtType, isRawType,
                isWildcardType);
    }

    public OptionalDoubleCtType newOptionalDoubleCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, OptionalDouble.class)) {
            return null;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return null;
        }
        PrimitiveType primitiveType = ctx.getTypes()
                .getPrimitiveType(TypeKind.DOUBLE);
        CtType elementCtType = newBasicCtType(primitiveType);
        return new OptionalDoubleCtType(ctx, type, elementCtType);
    }

    public OptionalIntCtType newOptionalIntCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, OptionalInt.class)) {
            return null;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return null;
        }
        PrimitiveType primitiveType = ctx.getTypes()
                .getPrimitiveType(TypeKind.INT);
        CtType elementCtType = newBasicCtType(primitiveType);
        return new OptionalIntCtType(ctx, type, elementCtType);
    }

    public OptionalLongCtType newOptionalLongCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, OptionalLong.class)) {
            return null;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return null;
        }
        PrimitiveType primitiveType = ctx.getTypes()
                .getPrimitiveType(TypeKind.LONG);
        CtType elementCtType = newBasicCtType(primitiveType);
        return new OptionalLongCtType(ctx, type, elementCtType);
    }

    public PreparedSqlCtType newPreparedSqlCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, PreparedSql.class)) {
            return null;
        }
        return new PreparedSqlCtType(ctx, type);
    }

    public ReferenceCtType newReferenceCtType(TypeMirror type) {
        assertNotNull(type);
        DeclaredType referenceDeclaredType = getDeclaredTypeFromHierarchy(type,
                Reference.class);
        if (referenceDeclaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArguments = referenceDeclaredType
                .getTypeArguments();
        TypeMirror referentTypeMirror = null;
        CtType referentType = null;
        if (typeArguments.size() == 1) {
            referentTypeMirror = typeArguments.get(0);
            referentType = toCtType(referentTypeMirror,
                    List.of(this::newOptionalCtType, this::newOptionalIntCtType,
                            this::newOptionalLongCtType,
                            this::newOptionalDoubleCtType,
                            this::newHolderCtType, this::newBasicCtType));
        }
        return new ReferenceCtType(ctx, type, referentTypeMirror, referentType);
    }

    public SelectOptionsCtType newSelectOptionsCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isAssignable(type, SelectOptions.class)) {
            return null;
        }
        return new SelectOptionsCtType(ctx, type);
    }

    public StreamCtType newStreamCtType(TypeMirror type) {
        assertNotNull(type);
        if (!ctx.getTypes().isSameType(type, Stream.class)) {
            return null;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        TypeMirror elementTypeMirror = null;
        CtType elementCtType = null;
        if (typeArgs.size() > 0) {
            elementTypeMirror = typeArgs.get(0);
            elementCtType = toCtType(elementTypeMirror,
                    List.of(this::newEntityCtType, this::newOptionalCtType,
                            this::newOptionalIntCtType,
                            this::newOptionalLongCtType,
                            this::newOptionalDoubleCtType,
                            this::newHolderCtType, this::newBasicCtType,
                            this::newMapCtType));
        }
        return new StreamCtType(ctx, type, elementTypeMirror, elementCtType);
    }

    public WrapperCtType newWrapperCtType(BasicCtType basicCtType) {
        assertNotNull(basicCtType);
        Class<?> wrapperClass = basicCtType.getTypeMirror()
                .accept(new WrapperTypeMappingVisitor(ctx), null);
        if (wrapperClass == null) {
            return null;
        }
        TypeElement wrapperTypeElement = ctx.getElements()
                .getTypeElement(wrapperClass);
        if (wrapperTypeElement == null) {
            return null;
        }
        WrapperCtType wrapperCtType;
        if (wrapperClass == EnumWrapper.class) {
            DeclaredType declaredType = ctx.getTypes().getDeclaredType(
                    wrapperTypeElement, basicCtType.getTypeMirror());
            wrapperCtType = new EnumWrapperCtType(ctx, declaredType);
        } else {
            wrapperCtType = new WrapperCtType(ctx, wrapperTypeElement.asType());
        }
        wrapperCtType.basicCtType = basicCtType;
        return wrapperCtType;
    }

    protected DeclaredType getDeclaredTypeFromHierarchy(TypeMirror type,
            Class<?> clazz) {
        if (ctx.getTypes().isSameType(type, clazz)) {
            return ctx.getTypes().toDeclaredType(type);
        }
        for (TypeMirror supertype : ctx.getTypes().directSupertypes(type)) {
            if (ctx.getTypes().isSameType(supertype, clazz)) {
                return ctx.getTypes().toDeclaredType(supertype);
            }
            DeclaredType result = getDeclaredTypeFromHierarchy(supertype,
                    clazz);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public CtType toCtType(
            TypeMirror typeMirror,
            List<Function<TypeMirror, CtType>> factories) {
        return factories.stream()
                .map(f -> f.apply(typeMirror))
                .filter(Objects::nonNull).findFirst()
                .orElseGet(() -> newAnyCtType(typeMirror));
    }

}
