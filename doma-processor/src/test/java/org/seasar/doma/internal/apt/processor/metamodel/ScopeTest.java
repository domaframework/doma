package org.seasar.doma.internal.apt.processor.metamodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.*;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.internal.apt.SimpleParameterResolver;
import org.seasar.doma.internal.apt.processor.EntityProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScopeTest extends CompilerSupport {

    @BeforeEach
    void setup() {
        addOption("-Adoma.test=true");
        addOption("-Adoma.metamodel.enabled=true");
    }

    @TestTemplate
    @ExtendWith(ScopeTest.SuccessInvocationContextProvider.class)
    void success(String fqn, String[] otherClasses) throws Exception {
        addProcessor(new EntityProcessor());
        for (String otherClass : otherClasses) {
            addResourceFileCompilationUnit(otherClass);
        }
        addResourceFileCompilationUnit(fqn);
        compile();
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
                            "org.seasar.doma.internal.apt.processor.entity.ScopedEntity",
                            "org.seasar.doma.internal.apt.processor.entity.ScopeClass")
            );
        }

        private TestTemplateInvocationContext invocationContext(
                String classFqn, String... otherClasses) {
            return new TestTemplateInvocationContext() {
                @Override
                public String getDisplayName(int invocationIndex) {
                    String[] split = classFqn.split("\\.");
                    return split[split.length - 1];
                }

                @Override
                public List<Extension> getAdditionalExtensions() {
                    return Arrays.asList(
                            new SimpleParameterResolver(classFqn),
                            new SimpleParameterResolver(otherClasses));
                }
            };
        }
    }
}
