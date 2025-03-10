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
package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;
import org.seasar.doma.message.Message;

public abstract class CompilerSupport {

  @RegisterExtension final CompilerExtension compiler = new CompilerExtension();

  @TempDir Path sourceOutput;

  @TempDir Path classOutput;

  @BeforeEach
  final void setupTempDirs() {
    compiler.setSourceOutput(sourceOutput);
    compiler.setClassOutput(classOutput);
  }

  protected void enableCompilationAssertion() {
    compiler.enableCompilationAssertion();
  }

  protected void disableCompilationAssertion() {
    compiler.disableCompilationAssertion();
  }

  protected void addSourcePath(final String... sourcePaths) {
    compiler.addSourcePath(sourcePaths);
  }

  protected void addOption(final String... options) {
    if (options.length == 0) {
      return;
    }
    compiler.addOption(options);
  }

  protected void addProcessor(final Processor... processors) {
    compiler.addProcessor(processors);
  }

  protected void addCompilationUnit(final Class<?>... classes) {
    compiler.addCompilationUnit(classes);
  }

  protected void addResourceFileCompilationUnit(final String fqn) {
    String fileName = fqn.replace(".", "/") + ".java";
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
      Objects.requireNonNull(in);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        String source = reader.lines().collect(Collectors.joining("\n"));
        compiler.addCompilationUnit(fqn, source);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  protected void compile() throws IOException {
    compiler.compile();
  }

  protected Boolean getCompiledResult() throws IllegalStateException {
    return compiler.getCompiledResult();
  }

  protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
    return compiler.getDiagnostics();
  }

  protected void assertEqualsGeneratedSourceWithResource(
      final URL expectedResourceUrl, final String className) throws Exception {
    try {
      compiler.assertEqualsGeneratedSourceWithResource(expectedResourceUrl, className);
    } catch (AssertionError error) {
      System.out.println(compiler.getGeneratedSource(className));
      throw error;
    }
  }

  protected void assertMessage(Message message) {
    compiler.assertMessage(message);
  }

  protected void assertNoError() {
    boolean match =
        compiler.getDiagnostics().stream().anyMatch(d -> d.getKind() == Diagnostic.Kind.ERROR);
    if (match) {
      fail();
    }
  }
}
