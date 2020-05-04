package org.seasar.doma.internal.apt.processor.entitydesc;

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
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.opentest4j.AssertionFailedError;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.EntityDesc;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.GeneratedClassNameParameterResolver;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.EntityDescProcessor;

class EntityDescProcessorOptionTest extends CompilerSupport {

  private static final String prefix = "Q";
  private static final String suffix = "Def";

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.test=true");
    addOption("-Adoma.criteria.prefix=" + prefix);
    addOption("-Adoma.criteria.suffix=" + suffix);
  }

  @TestTemplate
  @ExtendWith(SuccessInvocationContextProvider.class)
  void success(Class clazz, URL expectedResourceUrl, String generatedClassName, String[] options)
      throws Exception {
    addOption(options);
    addProcessor(new EntityDescProcessor());
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
      return Stream.of(invocationContext(_Emp.class));
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
              new CustomGeneratedClassNameParameterResolver(compilationUnit),
              new SimpleParameterResolver(options));
        }
      };
    }
  }

  private static class CustomGeneratedClassNameParameterResolver
      extends GeneratedClassNameParameterResolver {

    public CustomGeneratedClassNameParameterResolver(Class<?> clazz) {
      super(clazz);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
      if (clazz.isAnnotationPresent(EntityDesc.class)) {
        EntityDesc entityDesc = clazz.getAnnotation(EntityDesc.class);
        ClassName className =
            ClassNames.newEntityDefClassNameBuilder(entityDesc.value().getName(), prefix, suffix);
        return className.toString();
      }
      throw new AssertionFailedError("annotation not found.");
    }
  }
}
