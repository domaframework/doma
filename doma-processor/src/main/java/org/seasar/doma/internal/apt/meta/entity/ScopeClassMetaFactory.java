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
package org.seasar.doma.internal.apt.meta.entity;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Scope;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.apt.decl.TypeParameterDeclaration;

public class ScopeClassMetaFactory {

  private final Context ctx;
  private final TypeElement typeElement;
  private final List<TypeParameterDeclaration> allTypeParameterDeclarations;

  public ScopeClassMetaFactory(Context ctx, TypeElement typeElement) {
    this.ctx = Objects.requireNonNull(ctx);
    this.typeElement = Objects.requireNonNull(typeElement);
    TypeDeclaration typeDeclaration = ctx.getDeclarations().newTypeDeclaration(typeElement);
    allTypeParameterDeclarations = typeDeclaration.getAllTypeParameterDeclarations();
  }

  public ScopeClassMeta createScopeClassMeta() {
    List<ScopeMethodMeta> scopeMethodMetas =
        ElementFilter.methodsIn(ctx.getMoreElements().getAllMembers(typeElement)).stream()
            .filter(m -> m.getAnnotation(Scope.class) != null)
            .filter(m -> m.getParameters().size() > 0)
            .filter(m -> m.getModifiers().contains(Modifier.PUBLIC))
            .filter(m -> !m.getModifiers().contains(Modifier.STATIC))
            .sorted(Comparator.comparing(m -> m.getSimpleName().toString()))
            .map(this::createScopeMethodMeta)
            .collect(toList());
    return new ScopeClassMeta(typeElement, scopeMethodMetas);
  }

  private ScopeMethodMeta createScopeMethodMeta(ExecutableElement method) {
    List<ScopeParameterMeta> scopeParameterMetas = new ArrayList<>(method.getParameters().size());
    Iterator<? extends VariableElement> it = method.getParameters().stream().skip(1L).iterator();
    while (it.hasNext()) {
      VariableElement parameter = it.next();
      boolean isVarArgs = !it.hasNext() && method.isVarArgs();
      ScopeParameterMeta p = createScopeParameterMeta(parameter, isVarArgs);
      scopeParameterMetas.add(p);
    }
    return new ScopeMethodMeta(method, scopeParameterMetas);
  }

  private ScopeParameterMeta createScopeParameterMeta(
      VariableElement parameter, boolean isVarArgs) {
    TypeMirror actualType = resolveTypeParameter(parameter.asType());
    return new ScopeParameterMeta(parameter, actualType, isVarArgs);
  }

  private TypeMirror resolveTypeParameter(TypeMirror formalType) {
    TypeParameterDeclaration typeParameterDeclaration =
        ctx.getDeclarations()
            .newTypeParameterDeclarationUsingTypeParams(formalType, allTypeParameterDeclarations);
    return typeParameterDeclaration.getActualType();
  }
}
