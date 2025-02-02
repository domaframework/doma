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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.seasar.doma.internal.Constants.EXTERNAL_DOMAIN_TYPE_ARRAY_SUFFIX;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.opentest4j.AssertionFailedError;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.Dao;
import org.seasar.doma.Domain;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.ClassNames;

public class GeneratedClassNameParameterResolver implements ParameterResolver {

  protected final Class<?> clazz;

  protected final boolean isExternalDomain;

  public GeneratedClassNameParameterResolver(Class<?> clazz) {
    assertNotNull(clazz);
    this.clazz = clazz;
    this.isExternalDomain = false;
  }

  public GeneratedClassNameParameterResolver(Class<?> clazz, boolean isExternalDomain) {
    assertNotNull(clazz);
    this.clazz = clazz;
    this.isExternalDomain = true;
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(String.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    if (isExternalDomain) {
      String name =
          clazz.isArray()
              ? clazz.getComponentType().getName() + EXTERNAL_DOMAIN_TYPE_ARRAY_SUFFIX
              : clazz.getName();
      return ClassNames.newExternalDomainTypeClassName(name).toString();
    }
    if (clazz.isAnnotationPresent(Dao.class)) {
      return clazz.getName() + Options.Constants.DEFAULT_DAO_SUFFIX;
    }
    if (clazz.isAnnotationPresent(Entity.class)) {
      return ClassNames.newEntityTypeClassName(clazz.getName()).toString();
    }
    if (clazz.isAnnotationPresent(Embeddable.class)) {
      return ClassNames.newEmbeddableTypeClassName(clazz.getName()).toString();
    }
    if (clazz.isAnnotationPresent(Domain.class)) {
      return ClassNames.newDomainTypeClassName(clazz.getName()).toString();
    }
    if (clazz.isAnnotationPresent(AggregateStrategy.class)) {
      return ClassNames.newAggregateStrategyTypeClassName(clazz.getName()).toString();
    }
    throw new AssertionFailedError("annotation not found.");
  }
}
