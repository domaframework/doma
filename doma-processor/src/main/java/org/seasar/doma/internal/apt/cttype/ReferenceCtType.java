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

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class ReferenceCtType extends AbstractCtType {

  private final CtType referentCtType;

  ReferenceCtType(Context ctx, TypeMirror type, CtType referentCtType) {
    super(ctx, type);
    this.referentCtType = referentCtType;
  }

  public CtType getReferentCtType() {
    return referentCtType;
  }

  public TypeMirror getReferentTypeMirror() {
    return referentCtType.getType();
  }

  public boolean isRaw() {
    return referentCtType.isNone();
  }

  public boolean hasWildcard() {
    return referentCtType.isWildcard();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitReferenceCtType(this, p);
  }
}
