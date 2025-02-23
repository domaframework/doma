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

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.RoundContext;

public abstract class AbstractCtType implements CtType {

  protected final RoundContext ctx;

  protected final TypeMirror type;

  protected final TypeElement typeElement;

  private final String qualifiedName;

  protected final String simpleName;

  protected AbstractCtType(RoundContext ctx, TypeMirror type) {
    assertNotNull(ctx, type);
    this.ctx = ctx;
    this.type = type;
    this.typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement != null) {
      qualifiedName = typeElement.getQualifiedName().toString();
      simpleName = typeElement.getSimpleName().toString();
    } else {
      qualifiedName = ctx.getMoreTypes().getTypeName(type);
      simpleName = qualifiedName;
    }
  }

  @Override
  public TypeMirror getType() {
    return type;
  }

  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  public boolean isEnum() {
    return typeElement != null && typeElement.getKind() == ElementKind.ENUM;
  }

  @Override
  public boolean isPrimitive() {
    return type.getKind().isPrimitive();
  }

  @Override
  public boolean isNone() {
    return type.getKind() == TypeKind.NONE;
  }

  @Override
  public boolean isWildcard() {
    return type.getKind() == TypeKind.WILDCARD;
  }

  @Override
  public boolean isArray() {
    return type.getKind() == TypeKind.ARRAY;
  }

  @Override
  public boolean isTypevar() {
    return type.getKind() == TypeKind.TYPEVAR;
  }

  @Override
  public boolean isSameType(CtType other) {
    return ctx.getMoreTypes().isSameTypeWithErasure(type, other.getType());
  }

  @Override
  public boolean hasTypeParameter() {
    return typeElement != null && !typeElement.getTypeParameters().isEmpty();
  }
}
