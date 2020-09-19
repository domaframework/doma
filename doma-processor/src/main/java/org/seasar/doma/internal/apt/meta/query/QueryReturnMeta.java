package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor8;
import org.seasar.doma.internal.apt.cttype.*;

public class QueryReturnMeta {

  private final CtType ctType;

  public QueryReturnMeta(CtType ctType) {
    assertNotNull(ctType);
    this.ctType = ctType;
  }

  public TypeMirror getBoxedType() {
    return ctType.accept(
        new SimpleCtTypeVisitor<TypeMirror, Void, RuntimeException>() {
          @Override
          public TypeMirror visitBasicCtType(BasicCtType ctType, Void o) {
            return ctType.getBoxedType();
          }

          @Override
          protected TypeMirror defaultAction(CtType ctType, Void o) {
            return ctType.getType();
          }
        },
        null);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isPrimitiveInt() {
    return ctType.getType().getKind() == TypeKind.INT;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isPrimitiveVoid() {
    return ctType.getType().getKind() == TypeKind.VOID;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isResult(EntityCtType entityCtType) {
    return ctType.accept(
        new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {
          @Override
          public Boolean visitResultCtType(ResultCtType ctType, Void aVoid)
              throws RuntimeException {
            CtType elementCtType = ctType.getElementCtType();
            return elementCtType.isSameType(entityCtType);
          }
        },
        null);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isBatchResult(EntityCtType entityCtType) {
    return ctType.accept(
        new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {
          @Override
          public Boolean visitBatchResultCtType(BatchResultCtType ctType, Void aVoid)
              throws RuntimeException {
            CtType elementCtType = ctType.getElementCtType();
            return elementCtType.isSameType(entityCtType);
          }
        },
        null);
  }

  public TypeMirror getType() {
    return ctType.getType();
  }

  public CtType getCtType() {
    return ctType;
  }
}
