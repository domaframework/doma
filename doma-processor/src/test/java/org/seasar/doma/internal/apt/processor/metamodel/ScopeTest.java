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
import org.seasar.doma.internal.apt.AbstractCompilerTest;
import org.seasar.doma.internal.apt.CompilerKind;
import org.seasar.doma.internal.apt.DomaProcessor;
import org.seasar.doma.internal.apt.ResourceParameterResolver;
import org.seasar.doma.internal.apt.Run;
import org.seasar.doma.internal.apt.SimpleParameterResolver;

public class ScopeTest extends AbstractCompilerTest {

  @BeforeEach
  void beforeEach() {
    addOption("-Adoma.metamodel.enabled=true");
    addProcessor(new DomaProcessor());
  }

  @Run(onlyIf = {CompilerKind.JAVAC})
  @TestTemplate
  @ExtendWith(ScopeTest.SuccessInvocationContextProvider.class)
  void success(String fqn, String[] otherClasses, URL expected) throws Exception {
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
