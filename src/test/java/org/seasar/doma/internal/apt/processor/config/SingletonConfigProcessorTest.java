package org.seasar.doma.internal.apt.processor.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.SingletonConfigProcessor;
import org.seasar.doma.message.Message;

class SingletonConfigProcessorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
  }

  @Test
  void success() throws Exception {
    addProcessor(new SingletonConfigProcessor());
    addCompilationUnit(ValidConfig.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(Class clazz, Message message, String... options) throws Exception {
    addOption(options);
    addProcessor(new SingletonConfigProcessor());
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
          invocationContext(NoConfig.class, Message.DOMA4253),
          invocationContext(MethodNotFoundConfig.class, Message.DOMA4254),
          invocationContext(PublicConstructorConfig.class, Message.DOMA4256));
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
