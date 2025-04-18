/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.cttype;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleTypeVisitor14;
import org.seasar.doma.DataType;
import org.seasar.doma.Domain;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.AggregateStrategyAnnot;
import org.seasar.doma.internal.apt.annot.DomainConvertersAnnot;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.ArrayWrapper;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.BigIntegerWrapper;
import org.seasar.doma.wrapper.BlobWrapper;
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.ByteWrapper;
import org.seasar.doma.wrapper.BytesWrapper;
import org.seasar.doma.wrapper.ClobWrapper;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.DoubleWrapper;
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.FloatWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.LocalDateTimeWrapper;
import org.seasar.doma.wrapper.LocalDateWrapper;
import org.seasar.doma.wrapper.LocalTimeWrapper;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.NClobWrapper;
import org.seasar.doma.wrapper.ObjectWrapper;
import org.seasar.doma.wrapper.PrimitiveBooleanWrapper;
import org.seasar.doma.wrapper.PrimitiveByteWrapper;
import org.seasar.doma.wrapper.PrimitiveDoubleWrapper;
import org.seasar.doma.wrapper.PrimitiveFloatWrapper;
import org.seasar.doma.wrapper.PrimitiveIntWrapper;
import org.seasar.doma.wrapper.PrimitiveLongWrapper;
import org.seasar.doma.wrapper.PrimitiveShortWrapper;
import org.seasar.doma.wrapper.SQLXMLWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;

public class CtTypes {

  private final RoundContext ctx;

  public CtTypes(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
  }

  public AggregateStrategyCtType newAggregateStrategyCtType(TypeMirror type) {
    if (ctx.getMoreTypes().isSameTypeWithErasure(type, Void.class)) {
      return null;
    }
    TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    AggregateStrategyAnnot aggregateStrategyAnnot =
        ctx.getAnnotations().newAggregateStrategyAnnot(typeElement);
    if (aggregateStrategyAnnot == null) {
      return null;
    }
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    ClassName typeClassName = ClassNames.newAggregateStrategyTypeClassName(binaryName);
    return new AggregateStrategyCtType(ctx, type, typeClassName, aggregateStrategyAnnot);
  }

  private AnyCtType newAnyCtType(TypeMirror type) {
    return new AnyCtType(ctx, type);
  }

  private BatchResultCtType newBatchResultCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, BatchResult.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new BatchResultCtType(ctx, type, elementCtType);
  }

  public BasicCtType newBasicCtType(TypeMirror type) {
    assertNotNull(type);
    Pair<TypeElement, TypeMirror> wrapperElementAndType = getWrapperElementAndType(type);
    if (wrapperElementAndType == null) {
      return null;
    }
    return new BasicCtType(ctx, type, wrapperElementAndType);
  }

  private Pair<TypeElement, TypeMirror> getWrapperElementAndType(TypeMirror type) {
    Class<?> wrapperClass = type.accept(new WrapperClassResolver(), null);
    if (wrapperClass == null) {
      return null;
    }
    TypeElement wrapperTypeElement = ctx.getMoreElements().getTypeElement(wrapperClass);
    if (wrapperTypeElement == null) {
      return null;
    }
    if (wrapperClass == EnumWrapper.class) {
      TypeMirror declaredType = ctx.getMoreTypes().getDeclaredType(wrapperTypeElement, type);
      return new Pair<>(wrapperTypeElement, declaredType);
    }
    return new Pair<>(wrapperTypeElement, wrapperTypeElement.asType());
  }

  public BiFunctionCtType newBiFunctionCtType(TypeMirror type) {
    DeclaredType declaredType = getSuperDeclaredType(type, BiFunction.class);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType firstArgCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    CtType secondArgCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    CtType resultCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new BiFunctionCtType(ctx, type, firstArgCtType, secondArgCtType, resultCtType);
  }

  private CollectorCtType newCollectorCtType(TypeMirror type) {
    DeclaredType declaredType = getSuperDeclaredType(type, Collector.class);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType targetCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    @SuppressWarnings("unused")
    CtType secondCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    CtType returnCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new CollectorCtType(ctx, type, targetCtType, returnCtType);
  }

  private ConfigCtType newConfigCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Config.class)) {
      return null;
    }
    return new ConfigCtType(ctx, type);
  }

  public DomainCtType newDomainCtType(TypeMirror type) {
    assertNotNull(type);

    if (type.getKind() == TypeKind.ARRAY) {
      DomainInfo info = getExternalDomainInfo(type);
      if (info == null) {
        return null;
      }
      BasicCtType basicCtType = newBasicCtType(info.valueType);
      if (basicCtType == null) {
        return null;
      }
      Name name = ctx.getNames().createExternalDomainName(type);
      ClassName typeClassName = ClassNames.newExternalDomainTypeClassName(name);
      return new DomainCtType(ctx, type, basicCtType, Collections.emptyList(), typeClassName);
    }

    TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    DomainInfo info = getDomainInfo(typeElement);
    if (info == null) {
      return null;
    }
    BasicCtType basicCtType = newBasicCtType(info.valueType);
    if (basicCtType == null) {
      return null;
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    List<CtType> typeArgCtTypes =
        typeElement.getTypeParameters().stream()
            .map(__ -> typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType())
            .collect(toList());
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    ClassName typeClassName;
    if (info.external) {
      Name name = ctx.getNames().createExternalDomainName(type);
      typeClassName = ClassNames.newExternalDomainTypeClassName(name);
    } else {
      typeClassName = ClassNames.newDomainTypeClassName(binaryName);
    }
    return new DomainCtType(ctx, type, basicCtType, typeArgCtTypes, typeClassName);
  }

  private DomainInfo getDomainInfo(TypeElement typeElement) {
    Domain domain = typeElement.getAnnotation(Domain.class);
    if (domain != null) {
      return getDomainInfo(domain);
    }
    DataType dataType = typeElement.getAnnotation(DataType.class);
    if (dataType != null) {
      return getDomainInfo(typeElement, dataType);
    }
    return getExternalDomainInfo(typeElement.asType());
  }

  private DomainInfo getDomainInfo(Domain domain) {
    try {
      //noinspection ResultOfMethodCallIgnored
      domain.valueType();
    } catch (MirroredTypeException e) {
      return new DomainInfo(e.getTypeMirror(), false);
    }
    throw new AptIllegalStateException("unreachable.");
  }

  private DomainInfo getDomainInfo(TypeElement typeElement, DataType dataType) {
    List<ExecutableElement> constructors =
        ElementFilter.constructorsIn(typeElement.getEnclosedElements()).stream()
            .filter(c -> !c.getModifiers().contains(Modifier.PRIVATE))
            .filter(c -> c.getParameters().size() == 1)
            .toList();
    if (constructors.size() != 1) {
      throw new AptIllegalStateException(
          String.format("%s : %d", typeElement.getQualifiedName(), constructors.size()));
    }
    ExecutableElement constructor = constructors.iterator().next();
    VariableElement param = constructor.getParameters().iterator().next();
    if (param == null) {
      throw new AptIllegalStateException("param is null");
    }
    return new DomainInfo(param.asType(), false);
  }

  private DomainInfo getExternalDomainInfo(TypeMirror domainType) {
    DomainInfo info = getExternalDomainInfoFromOptions(domainType);
    if (info != null) {
      return info;
    }
    info = getExternalDomainInfoFromMetadata(domainType);
    if (info != null) {
      return info;
    }
    return getExternalDomainInfoFromRoundContext(domainType);
  }

  private DomainInfo getExternalDomainInfoFromRoundContext(TypeMirror domainType) {
    return ctx.getExternalDomainMetaList().stream()
        .filter(it -> ctx.getMoreTypes().isSameTypeWithErasure(it.asType(), domainType))
        .findFirst()
        .map(it -> new DomainInfo(it.getValueType(), true))
        .orElse(null);
  }

  private DomainInfo getExternalDomainInfoFromMetadata(TypeMirror domainType) {
    TypeElement domainTypeElement = ctx.getMoreTypes().toTypeElement(domainType);
    if (domainTypeElement == null) {
      return null;
    }
    ClassName className =
        ClassNames.newExternalDomainTypeClassName(domainTypeElement.getQualifiedName());
    TypeElement externalDomainMetadataElement = ctx.getMoreElements().getTypeElement(className);
    if (externalDomainMetadataElement == null) {
      return null;
    }
    TypeMirror[] argTypes = getDomainTypeArgTypes(externalDomainMetadataElement.asType());
    if (argTypes == null) {
      return null;
    }
    return new DomainInfo(argTypes[0], true);
  }

  private TypeMirror[] getDomainTypeArgTypes(TypeMirror typeMirror) {
    for (TypeMirror supertype : ctx.getMoreTypes().directSupertypes(typeMirror)) {
      if (!ctx.getMoreTypes().isAssignableWithErasure(supertype, DomainType.class)) {
        continue;
      }
      if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, DomainType.class)) {
        DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(supertype);
        assertNotNull(declaredType);
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        assertEquals(2, args.size());
        return new TypeMirror[] {args.get(0), args.get(1)};
      }
      TypeMirror[] argTypes = getDomainTypeArgTypes(supertype);
      if (argTypes != null) {
        return argTypes;
      }
    }
    return null;
  }

  private DomainInfo getExternalDomainInfoFromOptions(TypeMirror domainType) {
    String csv = ctx.getOptions().getDomainConverters();
    if (csv != null) {
      for (String value : csv.split(",")) {
        String className = value.trim();
        if (className.isEmpty()) {
          continue;
        }
        TypeElement convertersProviderElement =
            ctx.getMoreElements().getTypeElementFromBinaryName(className);
        if (convertersProviderElement == null) {
          throw new AptIllegalOptionException(Message.DOMA4200.getMessage(className));
        }
        DomainConvertersAnnot convertersMirror =
            ctx.getAnnotations().newDomainConvertersAnnot(convertersProviderElement);
        if (convertersMirror == null) {
          throw new AptIllegalOptionException(Message.DOMA4201.getMessage(className));
        }
        for (TypeMirror converterType : convertersMirror.getValueValue()) {
          // converterType does not contain adequate information in
          // eclipse incremental compile, so reload type
          converterType = reloadTypeMirror(converterType);
          if (converterType == null) {
            continue;
          }
          TypeMirror[] argTypes = getConverterArgTypes(converterType);
          if (argTypes == null
              || !ctx.getMoreTypes().isSameTypeWithErasure(domainType, argTypes[0])) {
            continue;
          }
          TypeMirror valueType = argTypes[1];
          return new DomainInfo(valueType, true);
        }
      }
    }
    return null;
  }

  private TypeMirror reloadTypeMirror(TypeMirror typeMirror) {
    TypeElement typeElement = ctx.getMoreTypes().toTypeElement(typeMirror);
    if (typeElement == null) {
      return null;
    }
    typeElement = ctx.getMoreElements().getTypeElement(typeElement.getQualifiedName());
    if (typeElement == null) {
      return null;
    }
    return typeElement.asType();
  }

  private TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
    for (TypeMirror supertype : ctx.getMoreTypes().directSupertypes(typeMirror)) {
      if (!ctx.getMoreTypes().isAssignableWithErasure(supertype, DomainConverter.class)) {
        continue;
      }
      if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, DomainConverter.class)) {
        DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(supertype);
        assertNotNull(declaredType);
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        assertEquals(2, args.size());
        return new TypeMirror[] {args.get(0), args.get(1)};
      }
      TypeMirror[] argTypes = getConverterArgTypes(supertype);
      if (argTypes != null) {
        return argTypes;
      }
    }
    return null;
  }

  private static class DomainInfo {
    private final TypeMirror valueType;

    private final boolean external;

    DomainInfo(TypeMirror valueType, boolean external) {
      assertNotNull(valueType);
      this.valueType = valueType;
      this.external = external;
    }
  }

  private EmbeddableCtType newEmbeddableCtType(TypeMirror type) {
    TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    Embeddable embeddable = typeElement.getAnnotation(Embeddable.class);
    if (embeddable == null) {
      return null;
    }
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    ClassName typeClassName = ClassNames.newEmbeddableTypeClassName(binaryName);
    return new EmbeddableCtType(ctx, type, typeClassName);
  }

  public EntityCtType newEntityCtType(TypeMirror type) {
    TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    Entity entity = typeElement.getAnnotation(Entity.class);
    if (entity == null) {
      return null;
    }
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    ClassName typeClassName = ClassNames.newEntityTypeClassName(binaryName);
    boolean immutable = typeElement.getKind() == ElementKind.RECORD || entity.immutable();
    return new EntityCtType(ctx, type, immutable, typeClassName);
  }

  private FunctionCtType newFunctionCtType(TypeMirror type) {
    DeclaredType declaredType = getSuperDeclaredType(type, Function.class);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType targetCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    CtType returnCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new FunctionCtType(ctx, type, targetCtType, returnCtType);
  }

  public IterableCtType newIterableCtType(TypeMirror type) {
    assertNotNull(type);
    DeclaredType declaredType = getSuperDeclaredType(type, Iterable.class);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new IterableCtType(ctx, type, elementCtType);
  }

  public ArrayCtType newArrayCtType(TypeMirror type) {
    assertNotNull(type);
    if (type.getKind() != TypeKind.ARRAY) {
      return null;
    }
    TypeMirror componentType = ((ArrayType) type).getComponentType();
    if (componentType.getKind() == TypeKind.BYTE) {
      return null;
    }
    CtType elementCtType = newCtType(componentType);
    return new ArrayCtType(ctx, type, elementCtType);
  }

  private MultiResultCtType newMultiResultCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, MultiResult.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new MultiResultCtType(ctx, type, elementCtType);
  }

  private MapCtType newMapCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Map.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() != 2) {
      return null;
    }
    if (!ctx.getMoreTypes().isSameTypeWithErasure(typeArgs.get(0), String.class)) {
      return null;
    }
    if (!ctx.getMoreTypes().isSameTypeWithErasure(typeArgs.get(1), Object.class)) {
      return null;
    }
    return new MapCtType(ctx, type);
  }

  private NoneCtType newNoneCtType() {
    TypeMirror type = ctx.getMoreTypes().getNoType(TypeKind.NONE);
    return new NoneCtType(ctx, type);
  }

  private OptionalCtType newOptionalCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Optional.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new OptionalCtType(ctx, type, elementCtType);
  }

  private OptionalDoubleCtType newOptionalDoubleCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, OptionalDouble.class)) {
      return null;
    }
    PrimitiveType primitiveType = ctx.getMoreTypes().getPrimitiveType(TypeKind.DOUBLE);
    BasicCtType elementCtType = newBasicCtType(primitiveType);
    return new OptionalDoubleCtType(ctx, type, elementCtType);
  }

  private OptionalIntCtType newOptionalIntCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, OptionalInt.class)) {
      return null;
    }
    PrimitiveType primitiveType = ctx.getMoreTypes().getPrimitiveType(TypeKind.INT);
    BasicCtType elementCtType = newBasicCtType(primitiveType);
    return new OptionalIntCtType(ctx, type, elementCtType);
  }

  private OptionalLongCtType newOptionalLongCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, OptionalLong.class)) {
      return null;
    }
    PrimitiveType primitiveType = ctx.getMoreTypes().getPrimitiveType(TypeKind.LONG);
    BasicCtType elementCtType = newBasicCtType(primitiveType);
    return new OptionalLongCtType(ctx, type, elementCtType);
  }

  private PreparedSqlCtType newPreparedSqlCtType(TypeMirror type) {
    assertNotNull(type);
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, PreparedSql.class)) {
      return null;
    }
    return new PreparedSqlCtType(ctx, type);
  }

  private ReferenceCtType newReferenceCtType(TypeMirror type) {
    DeclaredType declaredType = getSuperDeclaredType(type, Reference.class);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType referentCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new ReferenceCtType(ctx, type, referentCtType);
  }

  private ResultCtType newResultCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Result.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new ResultCtType(ctx, type, elementCtType);
  }

  private SelectOptionsCtType newSelectOptionsCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isAssignableWithErasure(type, SelectOptions.class)) {
      return null;
    }
    return new SelectOptionsCtType(ctx, type);
  }

  private StreamCtType newStreamCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Stream.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new StreamCtType(ctx, type, elementCtType);
  }

  public CtType newCtType(TypeMirror type, CtTypeVisitor<Void, Void, AptException> validator) {
    assertNotNull(type, validator);
    return newCtTypeInternal(type, validator);
  }

  public CtType newCtType(TypeMirror type) {
    return newCtTypeInternal(type, new SimpleCtTypeVisitor<>());
  }

  private CtType newCtTypeInternal(
      TypeMirror type, CtTypeVisitor<Void, Void, AptException> validator) {
    // Apply newDomainCtType, newEmbeddableCtType and newEntityCtType functions first.
    List<Function<TypeMirror, CtType>> functions =
        Arrays.asList(
            this::newDomainCtType,
            this::newEmbeddableCtType,
            this::newEntityCtType,
            this::newIterableCtType,
            this::newStreamCtType,
            this::newOptionalCtType,
            this::newOptionalIntCtType,
            this::newOptionalLongCtType,
            this::newOptionalDoubleCtType,
            this::newBasicCtType,
            this::newMapCtType,
            this::newSelectOptionsCtType,
            this::newFunctionCtType,
            this::newCollectorCtType,
            this::newReferenceCtType,
            this::newBiFunctionCtType,
            this::newPreparedSqlCtType,
            this::newConfigCtType,
            this::newResultCtType,
            this::newBatchResultCtType,
            this::newMultiResultCtType,
            this::newArrayCtType);
    CtType ctType =
        functions.stream()
            .map(f -> f.apply(type))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(() -> newAnyCtType(type));
    ctType.accept(validator, null);
    return ctType;
  }

  private DeclaredType getSuperDeclaredType(TypeMirror type, Class<?> superclass) {
    if (ctx.getMoreTypes().isSameTypeWithErasure(type, superclass)) {
      return ctx.getMoreTypes().toDeclaredType(type);
    }
    for (TypeMirror supertype : ctx.getMoreTypes().directSupertypes(type)) {
      if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, superclass)) {
        return ctx.getMoreTypes().toDeclaredType(supertype);
      }
      DeclaredType result = getSuperDeclaredType(supertype, superclass);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private class WrapperClassResolver extends SimpleTypeVisitor14<Class<?>, Void> {

    @Override
    public Class<?> visitArray(ArrayType t, Void p) {
      if (t.getComponentType().getKind() == TypeKind.BYTE) {
        return BytesWrapper.class;
      }
      return null;
    }

    @Override
    public Class<?> visitDeclared(DeclaredType t, Void p) {
      TypeElement typeElement = ctx.getMoreTypes().toTypeElement(t);
      if (typeElement == null) {
        return null;
      }
      if (typeElement.getKind() == ElementKind.ENUM) {
        return EnumWrapper.class;
      }
      String name = typeElement.getQualifiedName().toString();
      if (String.class.getName().equals(name)) {
        return StringWrapper.class;
      }
      if (Boolean.class.getName().equals(name)) {
        return BooleanWrapper.class;
      }
      if (Byte.class.getName().equals(name)) {
        return ByteWrapper.class;
      }
      if (Short.class.getName().equals(name)) {
        return ShortWrapper.class;
      }
      if (Integer.class.getName().equals(name)) {
        return IntegerWrapper.class;
      }
      if (Long.class.getName().equals(name)) {
        return LongWrapper.class;
      }
      if (Float.class.getName().equals(name)) {
        return FloatWrapper.class;
      }
      if (Double.class.getName().equals(name)) {
        return DoubleWrapper.class;
      }
      if (Object.class.getName().equals(name)) {
        return ObjectWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, BigDecimal.class)) {
        return BigDecimalWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, BigInteger.class)) {
        return BigIntegerWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Time.class)) {
        return TimeWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Timestamp.class)) {
        return TimestampWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Date.class)) {
        return DateWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, java.util.Date.class)) {
        return UtilDateWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, LocalTime.class)) {
        return LocalTimeWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, LocalDateTime.class)) {
        return LocalDateTimeWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, LocalDate.class)) {
        return LocalDateWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Array.class)) {
        return ArrayWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Blob.class)) {
        return BlobWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, NClob.class)) {
        return NClobWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, Clob.class)) {
        return ClobWrapper.class;
      }
      if (ctx.getMoreTypes().isAssignableWithErasure(t, SQLXML.class)) {
        return SQLXMLWrapper.class;
      }
      return null;
    }

    @Override
    public Class<?> visitPrimitive(PrimitiveType t, Void p) {
      return switch (t.getKind()) {
        case BOOLEAN -> PrimitiveBooleanWrapper.class;
        case BYTE -> PrimitiveByteWrapper.class;
        case SHORT -> PrimitiveShortWrapper.class;
        case INT -> PrimitiveIntWrapper.class;
        case LONG -> PrimitiveLongWrapper.class;
        case FLOAT -> PrimitiveFloatWrapper.class;
        case DOUBLE -> PrimitiveDoubleWrapper.class;
        case CHAR -> null;
        default -> assertUnreachable();
      };
    }
  }
}
