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
package org.seasar.doma.internal.apt.processor.entity.association;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.EntityProcessor;
import org.seasar.doma.message.Message;

class EntityProcessorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
    addProcessor(new EntityProcessor());
    addCompilationUnit(Emp.class);
    addCompilationUnit(Dept.class);
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(
      Class<?>[] classes, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addCompilationUnit(classes);
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
      return Stream.of(
          invocationContext(EntityAssociation.class),
          invocationContext(ListAssociation.class),
          invocationContext(OptionalAssociation.class),
          invocationContext(ListOptionalAssociation.class),
          invocationContext(ChildEntity.class));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> compilationUnit, String... options) {
      return invocationContext(new Class[] {compilationUnit}, options);
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?>[] compilationUnitArray, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return compilationUnitArray[0].getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(compilationUnitArray),
              new ResourceParameterResolver(compilationUnitArray[0]),
              new GeneratedClassNameParameterResolver(compilationUnitArray[0]),
              new SimpleParameterResolver(options));
        }
      };
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> compilationUnit, Class<?> annotatedClass, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return compilationUnit.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(new Class[] {compilationUnit}),
              new ResourceParameterResolver(compilationUnit),
              new GeneratedClassNameParameterResolver(annotatedClass),
              new SimpleParameterResolver(options));
        }
      };
    }
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(Class<?> clazz, Message message, String... options) throws Exception {
    addOption(options);
    addCompilationUnit(clazz);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(message);
  }

  static class ErrorInvocationContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
      return Stream.of(invocationContext(IllegalAssociation.class, Message.DOMA4485));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> clazz, Message message, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return clazz.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(clazz),
              new SimpleParameterResolver(message),
              new SimpleParameterResolver(options));
        }
      };
    }
  }
}
