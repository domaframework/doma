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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.generator.Code;
import org.seasar.doma.internal.jdbc.scalar.BasicScalarSuppliers;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalarSuppliers;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.internal.wrapper.WrapperSuppliers;

public class BasicCtType extends AbstractCtType {

  private final TypeMirror boxedType;

  private final TypeElement wrapperTypeElement;

  BasicCtType(Context ctx, TypeMirror type, Pair<TypeElement, TypeMirror> wrapperElementAndType) {
    super(ctx, type);
    assertNotNull(wrapperElementAndType);
    this.boxedType = ctx.getMoreTypes().boxIfPrimitive(type);
    this.wrapperTypeElement = wrapperElementAndType.fst;
  }

  public TypeMirror getBoxedType() {
    return boxedType;
  }

  public Code getWrapperSupplierCode() {
    Class<?> clazz = WrapperSuppliers.class;
    String methodName = "of" + wrapperTypeElement.getSimpleName().toString().replace("Wrapper", "");
    return new Code(
        p -> {
          if (isEnum()) {
            p.print("%1$s.%2$s(%3$s.class)", clazz, methodName, getQualifiedName());
          } else {
            p.print("%1$s.%2$s()", clazz, methodName);
          }
        });
  }

  public Code getScalarSupplierCode(boolean optional) {
    Class<?> clazz = optional ? OptionalBasicScalarSuppliers.class : BasicScalarSuppliers.class;
    String methodName = "of" + wrapperTypeElement.getSimpleName().toString().replace("Wrapper", "");
    return new Code(
        p -> {
          if (isEnum()) {
            p.print("%1$s.%2$s(%3$s.class)", clazz, methodName, getQualifiedName());
          } else {
            p.print("%1$s.%2$s()", clazz, methodName);
          }
        });
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBasicCtType(this, p);
  }
}
