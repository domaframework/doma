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

import java.net.URL;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.util.ResourceUtil;

public class ResourceParameterResolver implements ParameterResolver {

  private final String className;

  public ResourceParameterResolver(Class<?> clazz) {
    this(clazz.getSimpleName());
  }

  public ResourceParameterResolver(String clazz) {
    assertNotNull(clazz);
    this.className = new ClassName(clazz).getSimpleName();
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(URL.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    Class<?> testClass = extensionContext.getTestClass().orElseThrow(AssertionError::new);
    String prefix = testClass.getName().replace(".", "/");
    String path = String.format("%s_%s.txt", prefix, className);
    URL url = ResourceUtil.getResource(path);
    if (url == null) {
      throw new AssertionError("The path not found: " + path);
    }
    return url;
  }
}
