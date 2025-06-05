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

public final class BiConsumerCtType extends AbstractCtType {

  private final CtType firstArgCtType;

  private final CtType secondArgCtType;

  BiConsumerCtType(
      RoundContext ctx, TypeMirror type, CtType firstArgCtType, CtType secondArgCtType) {
    super(ctx, type);
    assertNotNull(firstArgCtType, secondArgCtType);
    this.firstArgCtType = firstArgCtType;
    this.secondArgCtType = secondArgCtType;
  }

  public CtType getFirstArgCtType() {
    return firstArgCtType;
  }

  public CtType getSecondArgCtType() {
    return secondArgCtType;
  }

  public boolean isRaw() {
    return firstArgCtType.isNone() || secondArgCtType.isNone();
  }

  public boolean hasWildcard() {
    return firstArgCtType.isWildcard() || secondArgCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBiConsumerCtType(this, p);
  }
}
