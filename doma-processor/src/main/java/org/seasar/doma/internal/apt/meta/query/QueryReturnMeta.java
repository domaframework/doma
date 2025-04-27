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
package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor14;
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
            new TypeKindVisitor14<Boolean, Void>(false) {

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

  public boolean isOptional() {
    return ctType.accept(
        new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {
          @Override
          public Boolean visitOptionalCtType(OptionalCtType ctType, Void unused)
              throws RuntimeException {
            return true;
          }
        },
        null);
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

  public boolean isMultiResult(EntityCtType entityCtType) {
    return ctType.accept(
        new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {
          @Override
          public Boolean visitMultiResultCtType(MultiResultCtType ctType, Void aVoid)
              throws RuntimeException {
            CtType elementCtType = ctType.getElementCtType();
            return elementCtType.isSameType(entityCtType);
          }
        },
        null);
  }

  public boolean isEntity(EntityCtType entityCtType) {
    return ctType.isSameType(entityCtType);
  }

  public boolean isOptionalEntity(EntityCtType entityCtType) {
    return ctType.accept(
        new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {
          @Override
          public Boolean visitOptionalCtType(OptionalCtType ctType, Void unused)
              throws RuntimeException {
            return ctType.getElementCtType().isSameType(entityCtType);
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
