package org.seasar.doma.internal.apt.processor.scope;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

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
import org.seasar.doma.internal.apt.CompilationUnitsParameterResolver;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.ScopeProcessor;
import org.seasar.doma.message.Message;

public class ScopeProcessorTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
    addProcessor(new ScopeProcessor());
  }

  @Test
  void success() throws Exception {
    addCompilationUnit(Item.class);
    addCompilationUnit(ItemScope.class);
    compile();
    assertTrue(getCompiledResult());
  }

  @TestTemplate
  @ExtendWith(ErrorInvocationContextProvider.class)
  void error(List<Class<?>> compilationUnits, Message message) throws Exception {
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
          invocationContext(NoParamMethod.class, NoParamMethodScope.class, Message.DOMA4457),
          invocationContext(StaticMethod.class, StaticMethodScope.class, Message.DOMA4458),
          invocationContext(NonPublicMethod.class, NonPublicMethodScope.class, Message.DOMA4459));
    }

    private TestTemplateInvocationContext invocationContext(
        Class<?> entityClass, Class<?> scopeClass, Message message) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return entityClass.getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new CompilationUnitsParameterResolver(entityClass, scopeClass),
              new SimpleParameterResolver(message));
        }
      };
    }
  }
}
