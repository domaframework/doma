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
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.EntityProcessor;

public class ScopeTest extends CompilerSupport {

  @BeforeEach
  void setup() {
    addOption("-Adoma.test=true");
    addOption("-Adoma.metamodel.enabled=true");
    addOption("--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED");
  }

  @TestTemplate
  @ExtendWith(ScopeTest.SuccessInvocationContextProvider.class)
  void success(String fqn, String[] otherClasses, URL expected) throws Exception {
    addProcessor(new EntityProcessor());
    for (String otherClass : otherClasses) {
      addResourceFileCompilationUnit(otherClass);
    }
    addResourceFileCompilationUnit(fqn);
    compile();
    String metamodel = ClassNames.newEntityMetamodelClassNameBuilder(fqn, "", "_").toString();
    assertEqualsGeneratedSourceWithResource(expected, metamodel);
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
          invocationContext(
              "org.seasar.doma.internal.apt.processor.metamodel.Room",
              "org.seasar.doma.internal.apt.processor.metamodel.RoomScope"),
          invocationContext(
              "org.seasar.doma.internal.apt.processor.metamodel.Multi",
              "org.seasar.doma.internal.apt.processor.metamodel.CreatedAtScope",
              "org.seasar.doma.internal.apt.processor.metamodel.NameScope"),
          invocationContext(
              "org.seasar.doma.internal.apt.processor.metamodel.Item",
              "org.seasar.doma.internal.apt.processor.metamodel.ItemScope",
              "org.seasar.doma.internal.apt.processor.metamodel.Publishable",
              "org.seasar.doma.internal.apt.processor.metamodel.GenericIdQueries"));
    }

    private TestTemplateInvocationContext invocationContext(
        String classFqn, String... otherClasses) {
      return new TestTemplateInvocationContext() {
        @Override
        public String getDisplayName(int invocationIndex) {
          return new ClassName(classFqn).getSimpleName();
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Arrays.asList(
              new ResourceParameterResolver(classFqn),
              new SimpleParameterResolver(classFqn),
              new SimpleParameterResolver(otherClasses));
        }
      };
    }
  }
}
