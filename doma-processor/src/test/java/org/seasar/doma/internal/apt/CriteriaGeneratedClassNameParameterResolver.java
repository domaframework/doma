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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.opentest4j.AssertionFailedError;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.ClassNames;

public class CriteriaGeneratedClassNameParameterResolver implements ParameterResolver {

  private final Class<?> clazz;
  private final String prefix;
  private final String suffix;

  public CriteriaGeneratedClassNameParameterResolver(Class<?> clazz) {
    this(clazz, "", "_");
  }

  public CriteriaGeneratedClassNameParameterResolver(Class<?> clazz, String prefix, String suffix) {
    assertNotNull(clazz);
    assertNotNull(prefix);
    assertNotNull(suffix);
    this.clazz = clazz;
    this.prefix = prefix;
    this.suffix = suffix;
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
    if (clazz.isAnnotationPresent(Entity.class)) {
      return ClassNames.newEntityMetamodelClassNameBuilder(clazz.getName(), prefix, suffix)
          .toString();
    }
    throw new AssertionFailedError("annotation not found.");
  }
}
