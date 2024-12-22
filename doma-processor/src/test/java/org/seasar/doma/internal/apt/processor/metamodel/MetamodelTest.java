package org.seasar.doma.internal.apt.processor.metamodel;

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
import org.seasar.doma.internal.apt.CriteriaGeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.DomainProcessor;
import org.seasar.doma.internal.apt.processor.EmbeddableProcessor;
import org.seasar.doma.internal.apt.processor.EntityProcessor;
import org.seasar.doma.message.Message;

class MetamodelTest extends CompilerSupport {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
    addProcessor(new EntityProcessor());

    // Process the dependent domains
    addProcessor(new DomainProcessor());
    addCompilationUnit(Name.class);

    // Process the dependent embeddables
    addProcessor(new EmbeddableProcessor());
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
      return Stream.of(invocationContext(Emp.class));
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
              new CriteriaGeneratedClassNameParameterResolver(compilationUnit),
              new SimpleParameterResolver(options));
        }
      };
    }
  }

  @TestTemplate
  @ExtendWith(SuccessPrefixInvocationContextProvider.class)
  void success_prefix(
      Class<?> clazz, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addCompilationUnit(clazz);
    compile();
    assertEqualsGeneratedSourceWithResource(expectedResourceUrl, generatedClassName);
    assertTrue(getCompiledResult());
  }

  static class SuccessPrefixInvocationContextProvider
      implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {
      return Stream.of(invocationContext(Dept.class));
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
              new CriteriaGeneratedClassNameParameterResolver(compilationUnit, "Q", ""),
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
      return Stream.of(
          invocationContext(Worker.class, Message.DOMA4455),
          invocationContext(Salesman.class, Message.DOMA4455));
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
