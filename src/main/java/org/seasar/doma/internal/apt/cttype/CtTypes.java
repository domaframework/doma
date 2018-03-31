package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor8;
import org.seasar.doma.internal.apt.AptIllegalOptionException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.EmbeddableAnnot;
import org.seasar.doma.internal.apt.annot.EntityAnnot;
import org.seasar.doma.internal.apt.annot.HolderAnnot;
import org.seasar.doma.internal.apt.annot.HolderConvertersAnnot;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.holder.HolderConverter;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.*;

public class CtTypes {

  private final Context ctx;

  public CtTypes(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  private AnyCtType newAnyCtType(TypeMirror type) {
    assertNotNull(type);
    return new AnyCtType(ctx, type);
  }

  public BasicCtType newBasicCtType(TypeMirror type) {
    assertNotNull(type);
    Class<?> wrapperClass = type.accept(new WrapperClassMapper(ctx), null);
    if (wrapperClass == null) {
      return null;
    }
    TypeElement wrapperTypeElement = ctx.getElements().getTypeElement(wrapperClass);
    if (wrapperTypeElement == null) {
      return null;
    }
    TypeMirror wrapperType;
    if (wrapperClass == EnumWrapper.class) {
      wrapperType = ctx.getTypes().getDeclaredType(wrapperTypeElement, type);
    } else {
      wrapperType = wrapperTypeElement.asType();
    }
    return new BasicCtType(ctx, type, wrapperType);
  }

  public BiFunctionCtType newBiFunctionCtType(TypeMirror type) {
    assertNotNull(type);
    DeclaredType biFunctionDeclaredType = getDeclaredTypeFromHierarchy(type, BiFunction.class);
    if (biFunctionDeclaredType == null) {
      return null;
    }
    CtType firstArgCtType = null;
    CtType secondArgCtType = null;
    AnyCtType resultCtType = null;
    List<? extends TypeMirror> typeArguments = biFunctionDeclaredType.getTypeArguments();

    if (typeArguments.size() == 3) {
      TypeMirror firstArgTypeMirror = typeArguments.get(0);
      TypeMirror secondArgTypeMirror = typeArguments.get(1);
      TypeMirror resultTypeMirror = typeArguments.get(2);
      firstArgCtType = toCtType(firstArgTypeMirror, List.of(this::newConfigCtType));
      secondArgCtType = toCtType(secondArgTypeMirror, List.of(this::newPreparedSqlCtType));
      resultCtType = newAnyCtType(resultTypeMirror);
    }

    return new BiFunctionCtType(ctx, type, firstArgCtType, secondArgCtType, resultCtType);
  }

  public CollectorCtType newCollectorCtType(TypeMirror type) {
    assertNotNull(type);
    DeclaredType collectorDeclaredType = getDeclaredTypeFromHierarchy(type, Collector.class);
    if (collectorDeclaredType == null) {
      return null;
    }

    List<? extends TypeMirror> typeArguments = collectorDeclaredType.getTypeArguments();
    CtType targetCtType = null;
    AnyCtType returnCtType = null;
    if (typeArguments.size() == 3) {
      TypeMirror targetTypeMirror = typeArguments.get(0);
      TypeMirror returnTypeMirror = typeArguments.get(2);
      targetCtType =
          toCtType(
              targetTypeMirror,
              List.of(
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
    EmbeddableAnnot embeddableAnnot = ctx.getAnnots().newEmbeddableAnnot(typeElement);
    if (embeddableAnnot == null) {
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
    EntityAnnot entityAnnot = ctx.getAnnots().newEntityAnnot(typeElement);
    if (entityAnnot == null) {
      return null;
    }
    return new EntityCtType(ctx, type, entityAnnot.getImmutableValue());
  }

  public FunctionCtType newFunctionCtType(TypeMirror type) {
    assertNotNull(type);
    DeclaredType functionDeclaredType = getDeclaredTypeFromHierarchy(type, Function.class);
    if (functionDeclaredType == null) {
      return null;
    }

    List<? extends TypeMirror> typeArguments = functionDeclaredType.getTypeArguments();
    CtType targetCtType = null;
    AnyCtType returnCtType = null;
    if (typeArguments.size() == 2) {
      TypeMirror targetTypeMirror = typeArguments.get(0);
      TypeMirror returnTypeMirror = typeArguments.get(1);
      targetCtType =
          toCtType(targetTypeMirror, List.of(this::newStreamCtType, this::newPreparedSqlCtType));
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
    Function<TypeElement, CodeSpec> codeSpecFactory;
    if (info.external) {
      codeSpecFactory = ctx.getCodeSpecs()::newExternalHolderDescCodeSpec;
    } else {
      codeSpecFactory = ctx.getCodeSpecs()::newHolderDescCodeSpec;
    }
    return new HolderCtType(ctx, type, basicCtType, codeSpecFactory);
  }

  private HolderInfo getHolderInfo(TypeElement typeElement) {
    HolderAnnot holderAnnot = ctx.getAnnots().newHolderAnnot(typeElement);
    if (holderAnnot != null) {
      return new HolderInfo(holderAnnot.getValueTypeValue(), false);
    }
    return getExternalHolderInfo(typeElement);
  }

  private HolderInfo getExternalHolderInfo(TypeElement typeElement) {
    String csv = ctx.getOptions().getHolderConverters();
    if (csv == null) {
      return null;
    }
    TypeMirror holderType = typeElement.asType();
    for (String value : csv.split(",")) {
      String className = value.trim();
      if (className.isEmpty()) {
        continue;
      }
      TypeElement providerElement = ctx.getElements().getTypeElement(className);
      if (providerElement == null) {
        throw new AptIllegalOptionException(Message.DOMA4200.getMessage(className));
      }
      HolderConvertersAnnot convertersMirror =
          ctx.getAnnots().newHolderConvertersAnnot(providerElement);
      if (convertersMirror == null) {
        throw new AptIllegalOptionException(Message.DOMA4201.getMessage(className));
      }
      for (TypeMirror converterType : convertersMirror.getValueValue()) {
        // converterType does not contain adequate information in
        // eclipse incremental compile, so reload typeMirror
        converterType = reloadTypeMirror(converterType);
        if (converterType == null) {
          continue;
        }
        TypeMirror[] argTypes = getConverterArgTypes(converterType);
        if (argTypes == null || !ctx.getTypes().isSameType(holderType, argTypes[0])) {
          continue;
        }
        TypeMirror valueType = argTypes[1];
        return new HolderInfo(valueType, true);
      }
    }
    return null;
  }

  private TypeMirror reloadTypeMirror(TypeMirror typeMirror) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(typeMirror);
    if (typeElement == null) {
      return null;
    }
    String binaryName = ctx.getElements().getBinaryName(typeElement).toString();
    typeElement = ctx.getElements().getTypeElement(binaryName);
    if (typeElement == null) {
      return null;
    }
    return typeElement.asType();
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
        return new TypeMirror[] {args.get(0), args.get(1)};
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
    TypeMirror supertype = ctx.getTypes().getSupertype(type, Iterable.class);
    if (supertype == null) {
      return null;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
    if (declaredType == null) {
      return null;
    }
    CtType elementCtType = null;
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() > 0) {
      elementCtType =
          toCtType(
              typeArgs.get(0),
              List.of(
                  this::newEntityCtType, this::newOptionalCtType,
                  this::newOptionalIntCtType, this::newOptionalLongCtType,
                  this::newOptionalDoubleCtType, this::newHolderCtType,
                  this::newBasicCtType, this::newMapCtType));
    }
    return new IterableCtType(ctx, type, elementCtType);
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
    if (declaredType.getTypeArguments().isEmpty()) {
      elementCtType = null;
    } else {
      TypeMirror typeArg = declaredType.getTypeArguments().get(0);
      elementCtType =
          toCtType(
              typeArg,
              List.of(
                  this::newEntityCtType,
                  this::newHolderCtType,
                  this::newBasicCtType,
                  this::newMapCtType));
    }
    return new OptionalCtType(ctx, type, elementCtType);
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
    PrimitiveType primitiveType = ctx.getTypes().getPrimitiveType(TypeKind.DOUBLE);
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
    PrimitiveType primitiveType = ctx.getTypes().getPrimitiveType(TypeKind.INT);
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
    PrimitiveType primitiveType = ctx.getTypes().getPrimitiveType(TypeKind.LONG);
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
    DeclaredType referenceDeclaredType = getDeclaredTypeFromHierarchy(type, Reference.class);
    if (referenceDeclaredType == null) {
      return null;
    }
    List<? extends TypeMirror> typeArgs = referenceDeclaredType.getTypeArguments();
    CtType referentCtType = null;
    if (typeArgs.size() == 1) {
      referentCtType =
          toCtType(
              typeArgs.get(0),
              List.of(
                  this::newOptionalCtType, this::newOptionalIntCtType,
                  this::newOptionalLongCtType, this::newOptionalDoubleCtType,
                  this::newHolderCtType, this::newBasicCtType));
    }
    return new ReferenceCtType(ctx, type, referentCtType);
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
    CtType elementCtType = null;
    if (typeArgs.size() > 0) {
      elementCtType =
          toCtType(
              typeArgs.get(0),
              List.of(
                  this::newEntityCtType, this::newOptionalCtType,
                  this::newOptionalIntCtType, this::newOptionalLongCtType,
                  this::newOptionalDoubleCtType, this::newHolderCtType,
                  this::newBasicCtType, this::newMapCtType));
    }
    return new StreamCtType(ctx, type, elementCtType);
  }

  private DeclaredType getDeclaredTypeFromHierarchy(TypeMirror type, Class<?> clazz) {
    if (ctx.getTypes().isSameType(type, clazz)) {
      return ctx.getTypes().toDeclaredType(type);
    }
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(type)) {
      if (ctx.getTypes().isSameType(supertype, clazz)) {
        return ctx.getTypes().toDeclaredType(supertype);
      }
      DeclaredType result = getDeclaredTypeFromHierarchy(supertype, clazz);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  public CtType toCtType(TypeMirror typeMirror, List<Function<TypeMirror, CtType>> factories) {
    return factories
        .stream()
        .map(f -> f.apply(typeMirror))
        .filter(Objects::nonNull)
        .findFirst()
        .orElseGet(() -> newAnyCtType(typeMirror));
  }

  public static class WrapperClassMapper extends SimpleTypeVisitor8<Class<?>, Void> {

    protected final Context ctx;

    protected WrapperClassMapper(Context ctx) {
      this.ctx = ctx;
    }

    @Override
    public Class<?> visitArray(ArrayType t, Void p) {
      if (t.getComponentType().getKind() == TypeKind.BYTE) {
        return BytesWrapper.class;
      }
      return null;
    }

    @Override
    public Class<?> visitDeclared(DeclaredType t, Void p) {
      TypeElement typeElement = ctx.getTypes().toTypeElement(t);
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
      if (ctx.getTypes().isAssignable(t, BigDecimal.class)) {
        return BigDecimalWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, BigInteger.class)) {
        return BigIntegerWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, Time.class)) {
        return TimeWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, Timestamp.class)) {
        return TimestampWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, Date.class)) {
        return DateWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, java.util.Date.class)) {
        return UtilDateWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, LocalTime.class)) {
        return LocalTimeWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, LocalDateTime.class)) {
        return LocalDateTimeWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, LocalDate.class)) {
        return LocalDateWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, Array.class)) {
        return ArrayWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, Blob.class)) {
        return BlobWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, NClob.class)) {
        return NClobWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, Clob.class)) {
        return ClobWrapper.class;
      }
      if (ctx.getTypes().isAssignable(t, SQLXML.class)) {
        return SQLXMLWrapper.class;
      }
      return null;
    }

    @Override
    public Class<?> visitPrimitive(PrimitiveType t, Void p) {
      switch (t.getKind()) {
        case BOOLEAN:
          return BooleanWrapper.class;
        case BYTE:
          return ByteWrapper.class;
        case SHORT:
          return ShortWrapper.class;
        case INT:
          return IntegerWrapper.class;
        case LONG:
          return LongWrapper.class;
        case FLOAT:
          return FloatWrapper.class;
        case DOUBLE:
          return DoubleWrapper.class;
        case CHAR:
          return null;
        default:
          return assertUnreachable();
      }
    }
  }
}
