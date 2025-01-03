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
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.Constants.EXTERNAL_DOMAIN_TYPE_ARRAY_SUFFIX;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

public class Names {

  private final Context ctx;

  Names(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public Name createExternalDomainName(TypeMirror externalDomainType) {
    assertNotNull(externalDomainType);
    ArrayType arrayType = ctx.getMoreTypes().toArrayType(externalDomainType);
    if (arrayType != null) {
      TypeMirror componentType = arrayType.getComponentType();
      TypeElement componentElement = ctx.getMoreTypes().toTypeElement(componentType);
      if (componentElement == null) {
        throw new AptIllegalStateException(componentType.toString());
      }
      Name binaryName = ctx.getMoreElements().getBinaryName(componentElement);
      return ctx.getMoreElements().getName(binaryName + EXTERNAL_DOMAIN_TYPE_ARRAY_SUFFIX);
    }
    TypeElement domainElement = ctx.getMoreTypes().toTypeElement(externalDomainType);
    if (domainElement == null) {
      throw new AptIllegalStateException(externalDomainType.toString());
    }
    return ctx.getMoreElements().getBinaryName(domainElement);
  }
}
