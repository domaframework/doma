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
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.generator.Code;

public class DomainCtType extends AbstractCtType {

  private final BasicCtType basicCtType;

  private final List<CtType> typeArgCtTypes;

  private final ClassName typeClassName;

  DomainCtType(
      Context ctx,
      TypeMirror type,
      BasicCtType basicCtType,
      List<CtType> typeArgCtTypes,
      ClassName typeClassName) {
    super(ctx, type);
    assertNotNull(basicCtType, typeArgCtTypes, typeClassName);
    this.basicCtType = basicCtType;
    this.typeArgCtTypes = typeArgCtTypes;
    this.typeClassName = typeClassName;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public boolean isRaw() {
    return typeArgCtTypes.stream().anyMatch(CtType::isNone);
  }

  public boolean hasWildcard() {
    return typeArgCtTypes.stream().anyMatch(CtType::isWildcard);
  }

  public boolean hasTypevar() {
    return typeArgCtTypes.stream().anyMatch(CtType::isTypevar);
  }

  public Code getTypeCode() {
    return new Code(
        p -> {
          if (typeArgCtTypes.isEmpty()) {
            p.print("%1$s.getSingletonInternal()", typeClassName);
          } else {
            List<TypeMirror> typeArgs =
                typeArgCtTypes.stream().map(CtType::getType).collect(toList());
            p.print("%1$s.<%2$s>getSingletonInternal()", typeClassName, typeArgs);
          }
        });
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitDomainCtType(this, p);
  }
}
