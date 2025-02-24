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
package org.seasar.doma.internal.apt.processor.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.seasar.doma.internal.apt.CompilationUnitsParameterResolver;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.DomaProcessor;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.domain.NestingValueObjectConverter.NestingValueObject;
import org.seasar.doma.message.Message;

class ExternalDomainProcessorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
    addProcessor(new DomaProcessor());
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(Class<?> clazz, URL expectedResourceUrl, String generatedClassName)
      throws Exception {
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
      return Stream.of(
          invocationContext(ColorConverter.class, Color.class),
          invocationContext(StringArrayConverter.class, String[].class),
          invocationContext(
              UpperBoundParameterizedValueObjectConverter.class,
              UpperBoundParameterizedValueObject.class),
          invocationContext(NestingValueObjectConverter.class, NestingValueObject.class),
          invocationContext(
              ParameterizedValueObjectConverter.class, ParameterizedValueObject.class),
          invocationContext(UUIDConverter.class, UUID.class),
          invocationContext(ValueObjectConverter.class, ValueObject.class),
          invocationContext(BoxJdbcTypeProvider.class, Box.class));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> compilationUnit, Class<?> externalDomainClass) {
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
              new GeneratedClassNameParameterResolver(externalDomainClass, true));
        }
      };
    }
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(List<Class<?>> compilationUnits, Message message, String... options) throws Exception {
    addOption(options);
    compilationUnits.forEach(this::addCompilationUnit);
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
          invocationContext(NotDomainConverter.class, Message.DOMA4191),
          invocationContext(IllegalParameterizedValueObjectConverter.class, Message.DOMA4203),
          invocationContext(NotPersistentValueObjectConverter.class, Message.DOMA4194),
          invocationContext(ConstructorNotFoundDomainConverter.class, Message.DOMA4193),
          invocationContext(AbstractDomainConverter.class, Message.DOMA4192),
          invocationContext(MultidimensionalArrayConverter.class, Message.DOMA4447),
          invocationContext(ListArrayConverter.class, Message.DOMA4448),
          invocationContext(BasicTypeConverter.class, Message.DOMA4460),
          invocationContext(List.of(URLConverter1.class, URLConverter2.class), Message.DOMA4490));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> clazz, Message message, String... options) {
      return invocationContext(List.of(clazz), message, options);
    }

    private TestTemplateInvocationContext invocationContext(
        List<Class<?>> classes, Message message, String... options) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return classes.get(0).getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new CompilationUnitsParameterResolver(classes.toArray(new Class<?>[0])),
              new SimpleParameterResolver(message),
              new SimpleParameterResolver(options));
        }
      };
    }
  }
}
