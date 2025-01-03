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
package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.BasicCtType;

public class OptionalBasicInParameterMeta implements CallableSqlParameterMeta {

  private final String name;

  private final BasicCtType basicCtType;

  public OptionalBasicInParameterMeta(String name, BasicCtType basicCtType) {
    assertNotNull(name, basicCtType);
    this.name = name;
    this.basicCtType = basicCtType;
  }

  public String getName() {
    return name;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalBasicInParameterMeta(this, p);
  }
}
