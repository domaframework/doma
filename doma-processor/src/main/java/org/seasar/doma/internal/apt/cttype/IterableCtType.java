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

import java.util.List;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.RoundContext;

public final class IterableCtType extends AbstractCtType {

  private final CtType elementCtType;

  IterableCtType(RoundContext ctx, TypeMirror type, CtType elementCtType) {
    super(ctx, type);
    assertNotNull(elementCtType);
    this.elementCtType = elementCtType;
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public boolean isRaw() {
    return elementCtType.isNone();
  }

  public boolean hasWildcard() {
    return elementCtType.isWildcard();
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isList() {
    return ctx.getMoreTypes().isSameTypeWithErasure(type, List.class);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitIterableCtType(this, p);
  }
}
