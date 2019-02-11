package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor8;
import org.seasar.doma.internal.apt.cttype.*;

public class QueryReturnMeta {

  private final CtType ctType;

  private final ExecutableElement methodElement;

  private final TypeElement daoElement;

  public QueryReturnMeta(CtType ctType, ExecutableElement methodElement, TypeElement daoElement) {
    assertNotNull(ctType, methodElement, daoElement);
    this.ctType = ctType;
    this.methodElement = methodElement;
    this.daoElement = daoElement;
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

  public ExecutableElement getMethodElement() {
    return methodElement;
  }

  public TypeElement getDaoElement() {
    return daoElement;
  }

  public TypeMirror getType() {
    return ctType.getType();
  }

  public CtType getCtType() {
    return ctType;
  }
}
