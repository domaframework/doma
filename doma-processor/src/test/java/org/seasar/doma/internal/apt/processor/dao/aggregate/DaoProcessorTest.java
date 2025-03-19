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
package org.seasar.doma.internal.apt.processor.dao.aggregate;

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
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.DomaProcessor;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.entity.ChildEntity;
import org.seasar.doma.message.Message;

class DaoProcessorTest extends AbstractCompilerTest {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");

    addProcessor(new DomaProcessor());

    // Add the dependent entities
    addCompilationUnit(Emp.class, Dept.class, Address.class, ChildEntity.class);

    // Add the dependent aggregate strategies
    addCompilationUnit(EmpStrategy.class);
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
      return Stream.of(invocationContext(EmpDao.class));
    }

    private TestTemplateInvocationContext invocationContext(Class<?> clazz, String... options) {
      return invocationContext(new Class[] {clazz}, options);
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?>[] classArray, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return classArray[0].getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(classArray),
              new ResourceParameterResolver(classArray[0]),
              new GeneratedClassNameParameterResolver(classArray[0]),
              new SimpleParameterResolver(options));
        }
      };
    }
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(Class<?> clazz, Message message) throws Exception {
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
      return Stream.of(
          invocationContext(UnmatchedType.class, Message.DOMA4480),
          invocationContext(NonEntityReturnType.class, Message.DOMA4473),
          invocationContext(StreamReturnType.class, Message.DOMA4473),
          invocationContext(InvalidSelectType.class, Message.DOMA4484));
    }

    private TestTemplateInvocationContext invocationContext(Class<?> clazz, Message message) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return clazz.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new SimpleParameterResolver(clazz), new SimpleParameterResolver(message));
        }
      };
    }
  }
}
