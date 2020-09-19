package org.seasar.doma.internal.apt.processor.embeddable;

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
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.internal.apt.processor.EmbeddableProcessor;
import org.seasar.doma.message.Message;

class EmbeddableProcessorTest extends CompilerSupport {

  @BeforeEach
  void setUp() {
    addOption("-Adoma.test=true");
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(Class<?> clazz, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addProcessor(new EmbeddableProcessor());
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
          invocationContext(Address.class),
          invocationContext(NotTopLevel.class, NotTopLevel.Address.class),
          invocationContext(AbstractEmbeddable.class),
          invocationContext(LombokValue.class, "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokAllArgsConstructor.class,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(Derived.class));
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
              new GeneratedClassNameParameterResolver(compilationUnit),
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
              new SimpleParameterResolver(compilationUnit),
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
    addProcessor(new EmbeddableProcessor());
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
          invocationContext(Outer_nonStaticInner.class, Message.DOMA4415),
          invocationContext(Outer_nonPublicInner.class, Message.DOMA4415),
          invocationContext(Outer_nonPublicMiddle.class, Message.DOMA4415),
          invocationContext(
              LombokValueStaticConstructor.class,
              Message.DOMA4423,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokAllArgsConstructorStaticName.class,
              Message.DOMA4424,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(
              LombokAllArgsConstructorAccess_private.class,
              Message.DOMA4425,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(
              LombokAllArgsConstructorAccess_none.class,
              Message.DOMA4427,
              "-Adoma.lombok.AllArgsConstructor=" + AllArgsConstructor.class.getName()),
          invocationContext(Outer__illegalName.class, Message.DOMA4417));
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
