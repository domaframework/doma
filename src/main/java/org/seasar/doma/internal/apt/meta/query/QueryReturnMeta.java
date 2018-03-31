package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor8;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

public class QueryReturnMeta {

  private final Context ctx;

  private final ExecutableElement methodElement;

  private final CtType ctType;

  public QueryReturnMeta(Context ctx, ExecutableElement methodElement, CtType ctType) {
    assertNotNull(ctx, methodElement, ctType);
    this.ctx = ctx;
    this.methodElement = methodElement;
    this.ctType = ctType;
  }

  public String getTypeName() {
    return ctType.getTypeName();
  }

  public boolean isPrimitiveInt() {
    return ctType.getType().getKind() == TypeKind.INT;
  }

  public boolean isPrimitiveIntArray() {
    return ctType
        .getType()
        .accept(
            new TypeKindVisitor8<Boolean, Void>(false) {

              @Override
              public Boolean visitArray(ArrayType t, Void p) {
                return t.getComponentType().getKind() == TypeKind.INT;
              }
            },
            null);
  }

  public boolean isPrimitiveVoid() {
    return ctType.getType().getKind() == TypeKind.VOID;
  }

  public boolean isResult(EntityCtType entityCtType) {
    var type = ctType.getType();
    if (!ctx.getTypes().isSameType(type, Result.class)) {
      return false;
    }
    var declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return false;
    }
    var typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() != 1) {
      return false;
    }
    var typeArg = typeArgs.get(0);
    return ctx.getTypes().isSameType(typeArg, entityCtType.getType());
  }

  public boolean isBatchResult(EntityCtType entityCtType) {
    var type = ctType.getType();
    if (!ctx.getTypes().isSameType(type, BatchResult.class)) {
      return false;
    }
    var declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return false;
    }
    var typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() != 1) {
      return false;
    }
    var typeArg = typeArgs.get(0);
    return ctx.getTypes().isSameType(typeArg, entityCtType.getType());
  }

  public ExecutableElement getMethodElement() {
    return methodElement;
  }

  public TypeMirror getType() {
    return ctType.getType();
  }

  public CtType getCtType() {
    return ctType;
  }
}
