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

public class CollectorCtType extends AbstractCtType {

  private final CtType targetCtType;

  private final CtType returnCtType;

  CollectorCtType(RoundContext ctx, TypeMirror type, CtType targetCtType, CtType returnCtType) {
    super(ctx, type);
    assertNotNull(targetCtType, returnCtType);
    this.targetCtType = targetCtType;
    this.returnCtType = returnCtType;
  }

  public CtType getTargetCtType() {
    return targetCtType;
  }

  public CtType getReturnCtType() {
    return returnCtType;
  }

  public boolean isRaw() {
    return targetCtType.isNone() || returnCtType.isNone();
  }

  public boolean hasWildcard() {
    return targetCtType.isWildcard() || returnCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitCollectorCtType(this, p);
  }
}
