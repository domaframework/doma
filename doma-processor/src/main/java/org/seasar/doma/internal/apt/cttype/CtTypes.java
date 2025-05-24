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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.DataType;
import org.seasar.doma.Domain;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.SelectOptions;

public class CtTypes {

  private final RoundContext ctx;
  private final BasicCtTypeFactory basicCtTypeFactory;
  private final InternalDomainCtTypeFactory internalDomainCtTypeFactory;
  private final ExternalDomainCtTypeFactory externalDomainCtTypeFactory;
  private final Map<String, Function<TypeMirror, CtType>> nameToTypeHandlers;
  private final List<Function<TypeMirror, CtType>> typeHandlers;

  public CtTypes(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
    this.basicCtTypeFactory = new BasicCtTypeFactory(ctx);
    this.internalDomainCtTypeFactory = new InternalDomainCtTypeFactory(ctx);
    this.externalDomainCtTypeFactory = new ExternalDomainCtTypeFactory(ctx);
    this.nameToTypeHandlers = createNameToTypeHandlers();
    this.typeHandlers = createTypeHandlers();
  }

  private Map<String, Function<TypeMirror, CtType>> createNameToTypeHandlers() {
    return Map.ofEntries(
        // Basic types (except enum types)
        Map.entry(Array.class.getName(), basicCtTypeFactory::newArrayCtType),
        Map.entry(BigDecimal.class.getName(), basicCtTypeFactory::newBigDecimalCtType),
        Map.entry(BigInteger.class.getName(), basicCtTypeFactory::newBigIntegerCtType),
        Map.entry(Blob.class.getName(), basicCtTypeFactory::newBlobCtType),
        Map.entry(Boolean.class.getName(), basicCtTypeFactory::newBooleanCtType),
        Map.entry(Byte.class.getName(), basicCtTypeFactory::newByteCtType),
        Map.entry(Clob.class.getName(), basicCtTypeFactory::newClobCtType),
        Map.entry(Date.class.getName(), basicCtTypeFactory::newDateCtType),
        Map.entry(Double.class.getName(), basicCtTypeFactory::newDoubleCtType),
        Map.entry(Float.class.getName(), basicCtTypeFactory::newFloatCtType),
        Map.entry(Integer.class.getName(), basicCtTypeFactory::newIntegerCtType),
        Map.entry(LocalDate.class.getName(), basicCtTypeFactory::newLocalDateCtType),
        Map.entry(LocalDateTime.class.getName(), basicCtTypeFactory::newLocalDateTimeCtType),
        Map.entry(LocalTime.class.getName(), basicCtTypeFactory::newLocalTimeCtType),
        Map.entry(Long.class.getName(), basicCtTypeFactory::newLongCtType),
        Map.entry(NClob.class.getName(), basicCtTypeFactory::newNClobCtType),
        Map.entry(Object.class.getName(), basicCtTypeFactory::newObjectCtType),
        Map.entry(String.class.getName(), basicCtTypeFactory::newStringCtType),
        Map.entry(Short.class.getName(), basicCtTypeFactory::newShortCtType),
        Map.entry(SQLXML.class.getName(), basicCtTypeFactory::newSQLXMLCtType),
        Map.entry(Time.class.getName(), basicCtTypeFactory::newTimeCtType),
        Map.entry(Timestamp.class.getName(), basicCtTypeFactory::newTimestampCtType),
        Map.entry(java.util.Date.class.getName(), basicCtTypeFactory::newUtilDateCtType),
        // Iterable types
        Map.entry(Iterable.class.getName(), this::newIterableCtType),
        Map.entry(Collection.class.getName(), this::newIterableCtType),
        Map.entry(List.class.getName(), this::newIterableCtType),
        Map.entry(Set.class.getName(), this::newIterableCtType),
        // Others
        Map.entry(Optional.class.getName(), this::newOptionalCtType),
        Map.entry(OptionalDouble.class.getName(), this::newOptionalDoubleCtType),
        Map.entry(OptionalInt.class.getName(), this::newOptionalIntCtType),
        Map.entry(OptionalLong.class.getName(), this::newOptionalLongCtType),
        Map.entry(Map.class.getName(), this::newMapCtType),
        Map.entry(Stream.class.getName(), this::newStreamCtType),
        Map.entry(Collector.class.getName(), this::newCollectorCtType),
        Map.entry(SelectOptions.class.getName(), this::newSelectOptionsCtType),
        Map.entry(Function.class.getName(), this::newFunctionCtType),
        Map.entry(BiFunction.class.getName(), this::newBiFunctionCtType),
        Map.entry(Reference.class.getName(), this::newReferenceCtType),
        Map.entry(PreparedSql.class.getName(), this::newPreparedSqlCtType),
        Map.entry(Config.class.getName(), this::newConfigCtType),
        Map.entry(Result.class.getName(), this::newResultCtType),
        Map.entry(BatchResult.class.getName(), this::newBatchResultCtType),
        Map.entry(MultiResult.class.getName(), this::newMultiResultCtType));
  }

  private List<Function<TypeMirror, CtType>> createTypeHandlers() {
    // Check external domain types first
    return List.of(
        externalDomainCtTypeFactory::newDomainCtType,
        // types that implement Iterable
        this::newIterableCtType,
        // enum types, byte array type or types that implement java.sql.Array/Blob/Clob/NClob/SQLXml
        basicCtTypeFactory::newBasicCtType,
        // types that implement Function
        this::newFunctionCtType,
        // types that implement Collector
        this::newCollectorCtType,
        // types that extend Reference
        this::newReferenceCtType,
        // types that implement BiFunction
        this::newBiFunctionCtType,
        // Array types (except byte array types)
        this::newArrayCtType);
  }

  public AggregateStrategyCtType newAggregateStrategyCtType(TypeMirror type) {
    Objects.requireNonNull(type);
    if (ctx.getMoreTypes().isSameTypeWithErasure(type, Void.class)) {
      return null;
    }
    var typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    var aggregateStrategyAnnot = ctx.getAnnotations().newAggregateStrategyAnnot(typeElement);
    if (aggregateStrategyAnnot == null) {
      return null;
    }
    var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    var typeClassName = ClassNames.newAggregateStrategyTypeClassName(binaryName);
    return new AggregateStrategyCtType(ctx, type, typeClassName, aggregateStrategyAnnot);
  }

  private AnyCtType newAnyCtType(TypeMirror type) {
    return new AnyCtType(ctx, type);
  }

  private BatchResultCtType newBatchResultCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, BatchResult.class)) {
      return null;
    }
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var elementCtType = getTypeArgument(declaredType);
    return new BatchResultCtType(ctx, type, elementCtType);
  }

  public BasicCtType newBasicCtType(TypeMirror type) {
    Objects.requireNonNull(type);
    return basicCtTypeFactory.newBasicCtType(type);
  }

  public BiFunctionCtType newBiFunctionCtType(TypeMirror type) {
    Objects.requireNonNull(type);
    var declaredType = getSuperDeclaredType(type, BiFunction.class);
    if (declaredType == null) {
      return null;
    }
    var triple = getThreeTypeArguments(declaredType);
    return new BiFunctionCtType(ctx, type, triple.first, triple.second, triple.third);
  }

  public BiConsumerCtType newBiConsumerCtType(TypeMirror type) {
    Objects.requireNonNull(type);
    var declaredType = getSuperDeclaredType(type, BiConsumer.class);
    if (declaredType == null) {
      return null;
    }
    var pair = getTwoTypeArguments(declaredType);
    return new BiConsumerCtType(ctx, type, pair.first, pair.second);
  }

  private CollectorCtType newCollectorCtType(TypeMirror type) {
    var declaredType = getSuperDeclaredType(type, Collector.class);
    if (declaredType == null) {
      return null;
    }
    var triple = getThreeTypeArguments(declaredType);
    return new CollectorCtType(ctx, type, triple.first, triple.third);
  }

  private ConfigCtType newConfigCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Config.class)) {
      return null;
    }
    return new ConfigCtType(ctx, type);
  }

  private EmbeddableCtType newEmbeddableCtType(
      TypeMirror type, TypeElement typeElement, Embeddable embeddable) {
    var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    var typeClassName = ClassNames.newEmbeddableTypeClassName(binaryName);
    return new EmbeddableCtType(ctx, type, typeClassName);
  }

  public EntityCtType newEntityCtType(TypeMirror type) {
    Objects.requireNonNull(type);
    var typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    var entity = typeElement.getAnnotation(Entity.class);
    if (entity == null) {
      return null;
    }
    return newEntityCtType(type, typeElement, entity);
  }

  private EntityCtType newEntityCtType(TypeMirror type, TypeElement typeElement, Entity entity) {
    var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    var typeClassName = ClassNames.newEntityTypeClassName(binaryName);
    var immutable = typeElement.getKind() == ElementKind.RECORD || entity.immutable();
    return new EntityCtType(ctx, type, immutable, typeClassName);
  }

  private FunctionCtType newFunctionCtType(TypeMirror type) {
    DeclaredType declaredType = getSuperDeclaredType(type, Function.class);
    if (declaredType == null) {
      return null;
    }
    var pair = getTwoTypeArguments(declaredType);
    return new FunctionCtType(ctx, type, pair.first, pair.second);
  }

  private IterableCtType newIterableCtType(TypeMirror type) {
    var declaredType = getSuperDeclaredType(type, Iterable.class);
    if (declaredType == null) {
      return null;
    }
    var elementCtType = getTypeArgument(declaredType);
    return new IterableCtType(ctx, type, elementCtType);
  }

  private ArrayCtType newArrayCtType(TypeMirror type) {
    if (type.getKind() != TypeKind.ARRAY) {
      return null;
    }
    var componentType = ((ArrayType) type).getComponentType();
    if (componentType.getKind() == TypeKind.BYTE) {
      return null;
    }
    var elementCtType = newCtType(componentType);
    return new ArrayCtType(ctx, type, elementCtType);
  }

  private MultiResultCtType newMultiResultCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, MultiResult.class)) {
      return null;
    }
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var elementCtType = getTypeArgument(declaredType);
    return new MultiResultCtType(ctx, type, elementCtType);
  }

  private MapCtType newMapCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Map.class)) {
      return null;
    }
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var typeArgs = declaredType.getTypeArguments();
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
    var type = ctx.getMoreTypes().getNoType(TypeKind.NONE);
    return new NoneCtType(ctx, type);
  }

  private OptionalCtType newOptionalCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Optional.class)) {
      return null;
    }
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var elementCtType = getTypeArgument(declaredType);
    return new OptionalCtType(ctx, type, elementCtType);
  }

  private OptionalDoubleCtType newOptionalDoubleCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, OptionalDouble.class)) {
      return null;
    }
    var primitiveType = ctx.getMoreTypes().getPrimitiveType(TypeKind.DOUBLE);
    var elementCtType = basicCtTypeFactory.newPrimitiveDoubleCtType(primitiveType);
    return new OptionalDoubleCtType(ctx, type, elementCtType);
  }

  private OptionalIntCtType newOptionalIntCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, OptionalInt.class)) {
      return null;
    }
    var primitiveType = ctx.getMoreTypes().getPrimitiveType(TypeKind.INT);
    var elementCtType = basicCtTypeFactory.newPrimitiveIntCtType(primitiveType);
    return new OptionalIntCtType(ctx, type, elementCtType);
  }

  private OptionalLongCtType newOptionalLongCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, OptionalLong.class)) {
      return null;
    }
    var primitiveType = ctx.getMoreTypes().getPrimitiveType(TypeKind.LONG);
    var elementCtType = basicCtTypeFactory.newPrimitiveLongCtType(primitiveType);
    return new OptionalLongCtType(ctx, type, elementCtType);
  }

  private PreparedSqlCtType newPreparedSqlCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, PreparedSql.class)) {
      return null;
    }
    return new PreparedSqlCtType(ctx, type);
  }

  private ReferenceCtType newReferenceCtType(TypeMirror type) {
    var declaredType = getSuperDeclaredType(type, Reference.class);
    if (declaredType == null) {
      return null;
    }
    var referentCtType = getTypeArgument(declaredType);
    return new ReferenceCtType(ctx, type, referentCtType);
  }

  private ResultCtType newResultCtType(TypeMirror type) {
    if (!ctx.getMoreTypes().isSameTypeWithErasure(type, Result.class)) {
      return null;
    }
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var elementCtType = getTypeArgument(declaredType);
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
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var elementCtType = getTypeArgument(declaredType);
    return new StreamCtType(ctx, type, elementCtType);
  }

  private DeclaredType getSuperDeclaredType(TypeMirror type, Class<?> superclass) {
    if (ctx.getMoreTypes().isSameTypeWithErasure(type, superclass)) {
      return ctx.getMoreTypes().toDeclaredType(type);
    }
    for (var supertype : ctx.getMoreTypes().directSupertypes(type)) {
      if (ctx.getMoreTypes().isSameTypeWithErasure(supertype, superclass)) {
        return ctx.getMoreTypes().toDeclaredType(supertype);
      }
      var result = getSuperDeclaredType(supertype, superclass);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private CtType getTypeArgument(DeclaredType declaredType) {
    var typeArguments = declaredType.getTypeArguments().iterator();
    return typeArguments.hasNext() ? newCtType(typeArguments.next()) : newNoneCtType();
  }

  private Pair getTwoTypeArguments(DeclaredType declaredType) {
    var typeArguments = declaredType.getTypeArguments().iterator();
    CtType first = typeArguments.hasNext() ? newCtType(typeArguments.next()) : newNoneCtType();
    CtType second = typeArguments.hasNext() ? newCtType(typeArguments.next()) : newNoneCtType();
    return new Pair(first, second);
  }

  private Triple getThreeTypeArguments(DeclaredType declaredType) {
    var typeArguments = declaredType.getTypeArguments().iterator();
    CtType first = typeArguments.hasNext() ? newCtType(typeArguments.next()) : newNoneCtType();
    CtType second = typeArguments.hasNext() ? newCtType(typeArguments.next()) : newNoneCtType();
    CtType third = typeArguments.hasNext() ? newCtType(typeArguments.next()) : newNoneCtType();
    return new Triple(first, second, third);
  }

  List<CtType> getAllTypeArguments(TypeElement typeElement, DeclaredType declaredType) {
    var typeArguments = declaredType.getTypeArguments().iterator();
    return typeElement.getTypeParameters().stream()
        .map(__ -> typeArguments.hasNext() ? newCtType(typeArguments.next()) : newNoneCtType())
        .collect(toList());
  }

  public CtType newCtType(TypeMirror type) {
    Objects.requireNonNull(type);
    return newCtTypeInternal(type, new SimpleCtTypeVisitor<>());
  }

  public CtType newCtType(TypeMirror type, CtTypeVisitor<Void, Void, AptException> validator) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(validator);
    return newCtTypeInternal(type, validator);
  }

  private CtType newCtTypeInternal(
      TypeMirror type, CtTypeVisitor<Void, Void, AptException> validator) {
    var ctType = findCtType(type);
    ctType.accept(validator, null);
    return ctType;
  }

  /**
   * Finds the appropriate CtType for the given TypeMirror.
   *
   * <p>The processing order in this method is critical for performance:
   *
   * <ol>
   *   <li>First checks primitive types (fastest check)
   *   <li>Then checks for annotations on the type element
   *   <li>Then checks by type name
   *   <li>Finally tries all registered type handlers
   *   <li>If no match is found, returns a generic AnyCtType
   * </ol>
   *
   * <p>This order is optimized to handle the most common cases first and perform more expensive
   * operations only when necessary.
   *
   * @param type the TypeMirror to find a CtType for
   * @return the appropriate CtType for the given TypeMirror
   */
  private CtType findCtType(TypeMirror type) {
    var kind = type.getKind();

    // handle primitive types first
    if (kind.isPrimitive()) {
      // find by primitive type kind
      var result = findCtTypeByPrimitiveTypeKind(type, kind);
      if (result != null) {
        return result;
      }
      // not found
      return newAnyCtType(type);
    }

    var typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement != null) {
      // find by annotation
      var result = findCtTypeByAnnotation(type, typeElement);
      if (result != null) {
        return result;
      }
      // find by name
      result = findCtTypeByName(type, typeElement);
      if (result != null) {
        return result;
      }
    }

    // find by other conditions
    for (var handler : typeHandlers) {
      var result = handler.apply(type);
      if (result != null) {
        return result;
      }
    }

    // not found
    return newAnyCtType(type);
  }

  private CtType findCtTypeByAnnotation(TypeMirror type, TypeElement typeElement) {
    // find by the Domain annotation
    var domain = typeElement.getAnnotation(Domain.class);
    if (domain != null) {
      var result = internalDomainCtTypeFactory.newDomainCtType(type, typeElement, domain);
      if (result != null) {
        return result;
      }
    }

    // find by the DataType annotation
    var dataType = typeElement.getAnnotation(DataType.class);
    if (dataType != null) {
      var result = internalDomainCtTypeFactory.newDataTypeCtType(type, typeElement, dataType);
      if (result != null) {
        return result;
      }
    }

    // find by the Embeddable annotation
    var embeddable = typeElement.getAnnotation(Embeddable.class);
    if (embeddable != null) {
      return newEmbeddableCtType(type, typeElement, embeddable);
    }

    // find by the Entity annotation
    var entity = typeElement.getAnnotation(Entity.class);
    if (entity != null) {
      return newEntityCtType(type, typeElement, entity);
    }

    // not found
    return null;
  }

  private CtType findCtTypeByName(TypeMirror type, TypeElement typeElement) {
    var name = typeElement.getQualifiedName().toString();
    var handler = nameToTypeHandlers.get(name);
    if (handler != null) {
      return handler.apply(type);
    }
    // not found
    return null;
  }

  private CtType findCtTypeByPrimitiveTypeKind(TypeMirror type, TypeKind kind) {
    return switch (kind) {
      case BOOLEAN -> basicCtTypeFactory.newPrimitiveBooleanCtType(type);
      case BYTE -> basicCtTypeFactory.newPrimitiveByteCtType(type);
      case DOUBLE -> basicCtTypeFactory.newPrimitiveDoubleCtType(type);
      case FLOAT -> basicCtTypeFactory.newPrimitiveFloatCtType(type);
      case INT -> basicCtTypeFactory.newPrimitiveIntCtType(type);
      case LONG -> basicCtTypeFactory.newPrimitiveLongCtType(type);
      case SHORT -> basicCtTypeFactory.newPrimitiveShortCtType(type);
      // not found
      default -> null;
    };
  }

  private record Pair(CtType first, CtType second) {}

  private record Triple(CtType first, CtType second, CtType third) {}
}
