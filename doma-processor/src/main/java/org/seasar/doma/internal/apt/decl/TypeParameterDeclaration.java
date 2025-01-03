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
package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;

public class TypeParameterDeclaration {

  private final TypeMirror formalType;

  private final TypeMirror actualType;

  TypeParameterDeclaration(TypeMirror formalType, TypeMirror actualType) {
    assertNotNull(formalType, actualType);
    this.formalType = formalType;
    this.actualType = actualType;
  }

  public TypeMirror getFormalType() {
    return formalType;
  }

  public TypeMirror getActualType() {
    return actualType;
  }
}
