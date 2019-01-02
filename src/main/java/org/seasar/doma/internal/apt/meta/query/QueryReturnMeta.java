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
    ctType = this.ctx.getCtTypes().newCtType(type, new CtTypeValidator());
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
    return ctx.getTypes().isSameType(typeArg, entityCtType.getType());
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
    return ctx.getTypes().isSameType(typeArg, entityCtType.getType());
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

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, AptException> {
    @Override
    protected Void defaultAction(CtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitMapCtType(MapCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
        throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType domainCtType, Void aVoid) throws AptException {
      if (domainCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4206,
            methodElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (domainCtType.hasWildcard() || domainCtType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4207,
            methodElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType optionalCtType, Void aVoid) throws AptException {
      if (optionalCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4236,
            methodElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (optionalCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4237,
            methodElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      optionalCtType.getElementCtType().accept(new OptionalElementCtTypeVisitor(), null);
      return null;
    }

    @Override
    public Void visitIterableCtType(IterableCtType iterableCtType, Void aVoid) throws AptException {
      if (iterableCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4109,
            methodElement,
            new Object[] {typeName, daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      if (iterableCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4113,
            methodElement,
            new Object[] {typeName, daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      iterableCtType.getElementCtType().accept(new IterableElementCtTypeVisitor(), null);
      return null;
    }
  }

  protected class IterableElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4210,
            methodElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4211,
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
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4238,
            methodElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4239,
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
