package org.seasar.doma.internal.apt.cttype;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.Domain;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.DomainConvertersAnnot;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.domain.DomainConverter;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.EnumWrapper;

public class CtTypes {

  private final Context ctx;

  public CtTypes(Context ctx) {
    this.ctx = ctx;
  }

  public AnyCtType newAnyCtType(TypeMirror type) {
    return new AnyCtType(ctx, type);
  }

  private BatchResultCtType newBatchResultCtType(TypeMirror type) {
    if (!ctx.getTypes().isSameTypeWithErasure(type, BatchResult.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new BatchResultCtType(ctx, type, elementCtType);
  }

  public BasicCtType newBasicCtType(TypeMirror type) {
    assertNotNull(type);
    WrapperCtType wrapperCtType = newWrapperCtType(type);
    if (wrapperCtType == null) {
      return null;
    }
    return new BasicCtType(ctx, type, wrapperCtType);
  }

  private BiFunctionCtType newBiFunctionCtType(TypeMirror type) {
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
    if (!ctx.getTypes().isSameTypeWithErasure(type, Config.class)) {
      return null;
    }
    return new ConfigCtType(ctx, type);
  }

  public DomainCtType newDomainCtType(TypeMirror type) {
    assertNotNull(type);
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
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
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    List<CtType> typeArgCtTypes =
        typeElement
            .getTypeParameters()
            .stream()
            .map(__ -> typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType())
            .collect(toList());
    return new DomainCtType(ctx, type, typeElement, basicCtType, typeArgCtTypes, info.external);
  }

  private DomainInfo getDomainInfo(TypeElement typeElement) {
    Domain domain = typeElement.getAnnotation(Domain.class);
    if (domain != null) {
      return getDomainInfo(typeElement, domain);
    }
    return getExternalDomainInfo(typeElement);
  }

  private DomainInfo getDomainInfo(TypeElement typeElement, Domain domain) {
    try {
      domain.valueType();
    } catch (MirroredTypeException e) {
      return new DomainInfo(e.getTypeMirror(), false);
    }
    throw new AptIllegalStateException("unreachable.");
  }

  private DomainInfo getExternalDomainInfo(TypeElement typeElement) {
    String csv = ctx.getOptions().getDomainConverters();
    if (csv != null) {
      TypeMirror domainType = typeElement.asType();
      for (String value : csv.split(",")) {
        String className = value.trim();
        if (className.isEmpty()) {
          continue;
        }
        TypeElement convertersProviderElement = ctx.getElements().getTypeElement(className);
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
          if (argTypes == null || !ctx.getTypes().isSameTypeWithErasure(domainType, argTypes[0])) {
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
    TypeElement typeElement = ctx.getTypes().toTypeElement(typeMirror);
    if (typeElement == null) {
      return null;
    }
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    typeElement = ctx.getElements().getTypeElement(binaryName);
    if (typeElement == null) {
      return null;
    }
    return typeElement.asType();
  }

  private TypeMirror[] getConverterArgTypes(TypeMirror typeMirror) {
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(typeMirror)) {
      if (!ctx.getTypes().isAssignableWithErasure(supertype, DomainConverter.class)) {
        continue;
      }
      if (ctx.getTypes().isSameTypeWithErasure(supertype, DomainConverter.class)) {
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
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

    public DomainInfo(TypeMirror valueType, boolean external) {
      assertNotNull(valueType);
      this.valueType = valueType;
      this.external = external;
    }
  }

  private EmbeddableCtType newEmbeddableCtType(TypeMirror type) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    Embeddable embeddable = typeElement.getAnnotation(Embeddable.class);
    if (embeddable == null) {
      return null;
    }
    return new EmbeddableCtType(ctx, type, typeElement);
  }

  private EntityCtType newEntityCtType(TypeMirror type) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement == null) {
      return null;
    }
    Entity entity = typeElement.getAnnotation(Entity.class);
    if (entity == null) {
      return null;
    }
    return new EntityCtType(ctx, type, typeElement, entity.immutable());
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

  private MapCtType newMapCtType(TypeMirror type) {
    if (!ctx.getTypes().isSameTypeWithErasure(type, Map.class)) {
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
    if (!ctx.getTypes().isSameTypeWithErasure(typeArgs.get(0), String.class)) {
      return null;
    }
    if (!ctx.getTypes().isSameTypeWithErasure(typeArgs.get(1), Object.class)) {
      return null;
    }
    return new MapCtType(ctx, type);
  }

  private NoneCtType newNoneCtType() {
    TypeMirror type = ctx.getTypes().getNoType(TypeKind.NONE);
    return new NoneCtType(ctx, type);
  }

  private OptionalCtType newOptionalCtType(TypeMirror type) {
    if (!ctx.getTypes().isSameTypeWithErasure(type, Optional.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new OptionalCtType(ctx, type, elementCtType);
  }

  private OptionalDoubleCtType newOptionalDoubleCtType(TypeMirror type) {
    if (!ctx.getTypes().isSameTypeWithErasure(type, OptionalDouble.class)) {
      return null;
    }
    return new OptionalDoubleCtType(ctx, type);
  }

  private OptionalIntCtType newOptionalIntCtType(TypeMirror type) {
    if (!ctx.getTypes().isSameTypeWithErasure(type, OptionalInt.class)) {
      return null;
    }
    return new OptionalIntCtType(ctx, type);
  }

  private OptionalLongCtType newOptionalLongCtType(TypeMirror type) {
    if (!ctx.getTypes().isSameTypeWithErasure(type, OptionalLong.class)) {
      return null;
    }
    return new OptionalLongCtType(ctx, type);
  }

  private PreparedSqlCtType newPreparedSqlCtType(TypeMirror type) {
    assertNotNull(type);
    if (!ctx.getTypes().isSameTypeWithErasure(type, PreparedSql.class)) {
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
    if (!ctx.getTypes().isSameTypeWithErasure(type, Result.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new ResultCtType(ctx, type, elementCtType);
  }

  private SelectOptionsCtType newSelectOptionsCtType(TypeMirror type) {
    if (!ctx.getTypes().isAssignableWithErasure(type, SelectOptions.class)) {
      return null;
    }
    return new SelectOptionsCtType(ctx, type);
  }

  private StreamCtType newStreamCtType(TypeMirror type) {
    if (!ctx.getTypes().isSameTypeWithErasure(type, Stream.class)) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    Iterator<? extends TypeMirror> typeArgs = declaredType.getTypeArguments().iterator();
    CtType elementCtType = typeArgs.hasNext() ? newCtType(typeArgs.next()) : newNoneCtType();
    return new StreamCtType(ctx, type, elementCtType);
  }

  private WrapperCtType newWrapperCtType(TypeMirror type) {
    Class<?> wrapperClass = type.accept(new WrapperCtType.WrapperTypeMappingVisitor(ctx), null);
    if (wrapperClass == null) {
      return null;
    }
    TypeElement wrapperTypeElement = ctx.getElements().getTypeElement(wrapperClass);
    if (wrapperTypeElement == null) {
      return null;
    }
    WrapperCtType wrapperCtType;
    if (wrapperClass == EnumWrapper.class) {
      DeclaredType declaredType = ctx.getTypes().getDeclaredType(wrapperTypeElement, type);
      wrapperCtType = new EnumWrapperCtType(ctx, declaredType);
    } else {
      wrapperCtType = new WrapperCtType(ctx, wrapperTypeElement.asType());
    }
    return wrapperCtType;
  }

  public CtType newCtType(TypeMirror type, CtTypeVisitor<Void, Void, AptException> validator) {
    assertNotNull(type, validator);
    return newCtTypeInternal(type, validator);
  }

  private CtType newCtType(TypeMirror type) {
    return newCtTypeInternal(type, new SimpleCtTypeVisitor<>());
  }

  private CtType newCtTypeInternal(
      TypeMirror type, CtTypeVisitor<Void, Void, AptException> validator) {
    List<Function<TypeMirror, CtType>> functions =
        Arrays.asList(
            this::newIterableCtType,
            this::newArrayCtType,
            this::newStreamCtType,
            this::newEntityCtType,
            this::newOptionalCtType,
            this::newOptionalIntCtType,
            this::newOptionalLongCtType,
            this::newOptionalDoubleCtType,
            this::newDomainCtType,
            this::newEmbeddableCtType,
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
            this::newBatchResultCtType);
    CtType ctType =
        functions
            .stream()
            .map(f -> f.apply(type))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(() -> newAnyCtType(type));
    ctType.accept(validator, null);
    return ctType;
  }

  private DeclaredType getSuperDeclaredType(TypeMirror type, Class<?> superclass) {
    if (ctx.getTypes().isSameTypeWithErasure(type, superclass)) {
      return ctx.getTypes().toDeclaredType(type);
    }
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(type)) {
      if (ctx.getTypes().isSameTypeWithErasure(supertype, superclass)) {
        return ctx.getTypes().toDeclaredType(supertype);
      }
      DeclaredType result = getSuperDeclaredType(supertype, superclass);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}
