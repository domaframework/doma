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

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.RoundContext;

public class BiFunctionCtType extends AbstractCtType {

  private final CtType firstArgCtType;

  private final CtType secondArgCtType;

  private final CtType resultCtType;

  BiFunctionCtType(
      RoundContext ctx,
      TypeMirror type,
      CtType firstArgCtType,
      CtType secondArgCtType,
      CtType resultCtType) {
    super(ctx, type);
    assertNotNull(firstArgCtType, secondArgCtType, resultCtType);
    this.firstArgCtType = firstArgCtType;
    this.secondArgCtType = secondArgCtType;
    this.resultCtType = resultCtType;
  }

  public CtType getFirstArgCtType() {
    return firstArgCtType;
  }

  public CtType getSecondArgCtType() {
    return secondArgCtType;
  }

  public CtType getResultCtType() {
    return resultCtType;
  }

  public boolean isRaw() {
    return firstArgCtType.isNone() || secondArgCtType.isNone() || resultCtType.isNone();
  }

  public boolean hasWildcard() {
    return firstArgCtType.isWildcard() || secondArgCtType.isWildcard() || resultCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBiFunctionCtType(this, p);
  }
}
