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
package org.seasar.doma.internal.apt.processor.metamodel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.CriteriaGeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.DomaProcessor;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;

class MetamodelOptionTest extends AbstractCompilerTest {

  private static final String PREFIX = "Q";
  private static final String SUFFIX = "Metamodel";

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
    addOption("-Adoma.metamodel.enabled=true");
    addOption("-Adoma.metamodel.prefix=" + PREFIX);
    addOption("-Adoma.metamodel.suffix=" + SUFFIX);
    addProcessor(new DomaProcessor());

    // Add the dependent domains
    addCompilationUnit(Name.class);

    // Add the dependent embeddables
    addCompilationUnit(EmpInfo.class);
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(Class<?> clazz, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addCompilationUnit(clazz);
    compile();
    assertEqualsGeneratedSourceWithResource(expectedResourceUrl, generatedClassName);
    assertTrue(getCompiledResult());
  }

  static class SuccessInvocationContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
      return Stream.of(invocationContext(Emp.class), invocationContext(Person.class));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> compilationUnit, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return compilationUnit.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(compilationUnit),
              new ResourceParameterResolver(compilationUnit),
              new CriteriaGeneratedClassNameParameterResolver(compilationUnit, PREFIX, SUFFIX),
              new SimpleParameterResolver(options));
        }
      };
    }
  }
}
