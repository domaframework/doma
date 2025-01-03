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
package org.seasar.doma.internal.apt.generator;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.util.Pair;

public class UnwrapOptionalVisitor
    extends SimpleCtTypeVisitor<Pair<CtType, TypeMirror>, Void, RuntimeException> {

  @Override
  protected Pair<CtType, TypeMirror> defaultAction(CtType ctType, Void aVoid)
      throws RuntimeException {
    return new Pair<>(ctType, ctType.getType());
  }

  @Override
  public Pair<CtType, TypeMirror> visitBasicCtType(BasicCtType ctType, Void aVoid)
      throws RuntimeException {
    return new Pair<>(ctType, ctType.getBoxedType());
  }

  @Override
  public Pair<CtType, TypeMirror> visitDomainCtType(DomainCtType ctType, Void aVoid)
      throws RuntimeException {
    return new Pair<>(ctType, ctType.getType());
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalCtType(OptionalCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }

  @Override
  public Pair<CtType, TypeMirror> visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid)
      throws RuntimeException {
    return ctType.getElementCtType().accept(this, null);
  }
}
