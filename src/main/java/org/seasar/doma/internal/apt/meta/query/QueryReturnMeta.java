package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor8;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.message.Message;

public class QueryReturnMeta {

  protected final Context ctx;

  protected final ExecutableElement methodElement;

  protected final TypeElement daoElement;

  protected final TypeMirror type;

  protected final String typeName;

  protected final CtType ctType;

  public QueryReturnMeta(QueryMeta queryMeta, Context ctx) {
    assertNotNull(queryMeta, ctx);
    this.ctx = ctx;
    methodElement = queryMeta.getMethodElement();
    daoElement = queryMeta.getDaoElement();
    type = methodElement.getReturnType();
    typeName = ctx.getTypes().getTypeName(type);
    ctType = createCtType();
  }

  protected CtType createCtType() {
    IterableCtType iterableCtType = IterableCtType.newInstance(type, ctx);
    if (iterableCtType != null) {
      if (iterableCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4109,
            ctx.getEnv(),
            methodElement,
            new Object[] {typeName, daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      if (iterableCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4113,
            ctx.getEnv(),
            methodElement,
            new Object[] {typeName, daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      iterableCtType.getElementCtType().accept(new IterableElementCtTypeVisitor(), null);
      return iterableCtType;
    }

    StreamCtType streamCtType = StreamCtType.newInstance(type, ctx);
    if (streamCtType != null) {
      return streamCtType;
    }

    EntityCtType entityCtType = EntityCtType.newInstance(type, ctx);
    if (entityCtType != null) {
      return entityCtType;
    }

    OptionalCtType optionalCtType = OptionalCtType.newInstance(type, ctx);
    if (optionalCtType != null) {
      if (optionalCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4236,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (optionalCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4237,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      optionalCtType.getElementCtType().accept(new OptionalElementCtTypeVisitor(), null);
      return optionalCtType;
    }

    OptionalIntCtType optionalIntCtType = OptionalIntCtType.newInstance(type, ctx);
    if (optionalIntCtType != null) {
      return optionalIntCtType;
    }

    OptionalLongCtType optionalLongCtType = OptionalLongCtType.newInstance(type, ctx);
    if (optionalLongCtType != null) {
      return optionalLongCtType;
    }

    OptionalDoubleCtType optionalDoubleCtType = OptionalDoubleCtType.newInstance(type, ctx);
    if (optionalDoubleCtType != null) {
      return optionalDoubleCtType;
    }

    DomainCtType domainCtType = DomainCtType.newInstance(type, ctx);
    if (domainCtType != null) {
      if (domainCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4206,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (domainCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4207,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return domainCtType;
    }

    BasicCtType basicCtType = BasicCtType.newInstance(type, ctx);
    if (basicCtType != null) {
      return basicCtType;
    }

    MapCtType mapCtType = MapCtType.newInstance(type, ctx);
    if (mapCtType != null) {
      return mapCtType;
    }

    return AnyCtType.newInstance(type, ctx);
  }

  public String getTypeName() {
    return typeName;
  }

  public String getBoxedTypeName() {
    return ctType.getBoxedTypeName();
  }

  public boolean isPrimitiveInt() {
    return type.getKind() == TypeKind.INT;
  }

  public boolean isPrimitiveIntArray() {
    return type.accept(
        new TypeKindVisitor8<Boolean, Void>(false) {

          @Override
          public Boolean visitArray(ArrayType t, Void p) {
            return t.getComponentType().getKind() == TypeKind.INT;
          }
        },
        null);
  }

  public boolean isPrimitiveVoid() {
    return type.getKind() == TypeKind.VOID;
  }

  public boolean isResult(EntityCtType entityCtType) {
    if (!ctx.getTypes().isSameType(ctx.getTypes().erasure(type), Result.class)) {
      return false;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return false;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() != 1) {
      return false;
    }
    TypeMirror typeArg = typeArgs.get(0);
    return ctx.getTypes().isSameType(typeArg, entityCtType.getTypeMirror());
  }

  public boolean isBatchResult(EntityCtType entityCtType) {
    if (!ctx.getTypes().isSameType(ctx.getTypes().erasure(type), BatchResult.class)) {
      return false;
    }
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return false;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() != 1) {
      return false;
    }
    TypeMirror typeArg = typeArgs.get(0);
    return ctx.getTypes().isSameType(typeArg, entityCtType.getTypeMirror());
  }

  public ExecutableElement getMethodElement() {
    return methodElement;
  }

  public TypeElement getDaoElement() {
    return daoElement;
  }

  public TypeMirror getType() {
    return type;
  }

  public CtType getCtType() {
    return ctType;
  }

  protected class IterableElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4210,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4211,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }
  }

  protected class OptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4238,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4239,
            ctx.getEnv(),
            methodElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }
  }
}
