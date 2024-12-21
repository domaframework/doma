package org.seasar.doma.internal.apt.processor.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.nio.file.Path;
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
import org.junit.jupiter.api.io.TempDir;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.lombok.Value;
import org.seasar.doma.internal.apt.processor.DomainProcessor;
import org.seasar.doma.message.Message;

class DomainProcessorTest extends CompilerSupport {

  @TempDir Path sourceOutput;
  @TempDir Path classOutput;

  @BeforeEach
  void beforeEach() {
    setSourceOutput(sourceOutput);
    setClassOutput(classOutput);
    addOption("-Adoma.test=true");
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(Class<?> clazz, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addProcessor(new DomainProcessor());
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
          invocationContext(IntersectionParameterizedDomain.class),
          invocationContext(UpperBoundParameterizedDomain.class),
          invocationContext(Salary.class),
          invocationContext(InterfaceDomain.class),
          invocationContext(ObjectDomain.class),
          invocationContext(NullRejectionDomain.class),
          invocationContext(ParameterizedSalary.class),
          invocationContext(ParameterizedOfSalary.class),
          invocationContext(SpecificDomain.class),
          invocationContext(OfAbstractDomain.class),
          invocationContext(OfPrimitiveValueType.class),
          invocationContext(OfSalary.class),
          invocationContext(OfEnumDomain.class),
          invocationContext(OfJobType.class),
          invocationContext(OfPrimitiveValueDomain.class),
          invocationContext(EnumDomain.class),
          invocationContext(PackagePrivateDomain.class),
          invocationContext(Outer.class, Outer.Inner.class),
          invocationContext(Outer_deepInner.class, Outer_deepInner.Middle.Inner.class),
          invocationContext(InterfaceOuter.class, InterfaceOuter.Inner.class),
          invocationContext(VersionCheckSuppressedDomain.class, "-Adoma.version.validation=false"),
          invocationContext(LombokValue.class, "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(PrimitiveValueDomain.class));
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
    addProcessor(new DomainProcessor());
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
          invocationContext(UnsupportedValueTypeDomain.class, Message.DOMA4102),
          invocationContext(AnnotationDomain.class, Message.DOMA4105),
          invocationContext(InterfaceNew.class, Message.DOMA4268),
          invocationContext(IllegalAcceptNullDomain.class, Message.DOMA4251),
          invocationContext(IllegalTypeParameterizedOfSalary.class, Message.DOMA4106),
          invocationContext(AbstractDomain.class, Message.DOMA4132),
          invocationContext(JobType.class, Message.DOMA4184),
          invocationContext(AccessorNotFoundDomain.class, Message.DOMA4104),
          invocationContext(Outer_nonStaticInner.class, Message.DOMA4275),
          invocationContext(Outer_nonPublicInner.class, Message.DOMA4275),
          invocationContext(Outer_nonPublicMiddle.class, Message.DOMA4275),
          invocationContext(Outer__illegalName.class, Message.DOMA4277),
          invocationContext(
              LombokValueStaticConstructor.class,
              Message.DOMA4428,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokValueNoField.class,
              Message.DOMA4430,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokValueTwoFields.class,
              Message.DOMA4431,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokValueTypeNotAssignable.class,
              Message.DOMA4432,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokValueAccessorMethod.class,
              Message.DOMA4429,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(
              LombokValueAccessorMethod_boolean.class,
              Message.DOMA4429,
              "-Adoma.lombok.Value=" + Value.class.getName()),
          invocationContext(ConstructorNotFoundDomain.class, Message.DOMA4103));
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
