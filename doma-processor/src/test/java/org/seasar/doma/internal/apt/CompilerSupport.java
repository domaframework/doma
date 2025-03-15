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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.processing.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;
import org.seasar.doma.message.Message;

public abstract class CompilerSupport {

  private @RegisterExtension final AptinaTestCase delegate = new AptinaTestCase();

  private @TempDir Path sourceOutput;

  private @TempDir Path classOutput;

  @BeforeEach
  final void setupTempDirs() {
    delegate.setSourceOutput(sourceOutput);
    delegate.setClassOutput(classOutput);
  }

  protected void enableCompilationAssertion() {
    delegate.enableCompilationAssertion();
  }

  protected void disableCompilationAssertion() {
    delegate.disableCompilationAssertion();
  }

  protected void addOption(final String... options) {
    if (options.length == 0) {
      return;
    }
    delegate.addOption(options);
  }

  protected void addProcessor(final Processor... processors) {
    delegate.addProcessor(processors);
  }

  protected void addCompilationUnit(final Class<?>... classes) {
    delegate.addCompilationUnit(classes);
  }

  protected void addResourceFileCompilationUnit(final String fqn) {
    String fileName = fqn.replace(".", "/") + ".java";
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
      Objects.requireNonNull(in);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        String source = reader.lines().collect(Collectors.joining("\n"));
        delegate.addCompilationUnit(fqn, source);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  protected void compile() throws IOException {
    delegate.compile();
  }

  protected Boolean getCompiledResult() throws IllegalStateException {
    return delegate.getCompiledResult();
  }

  protected void assertEqualsGeneratedSourceWithResource(
      final URL expectedResourceUrl, final String className) throws Exception {
    delegate.assertEqualsGeneratedSourceWithResource(expectedResourceUrl, className);
  }

  protected void assertMessage(Message message) {
    delegate.assertMessage(message);
  }

  protected void assertNoError() {
    delegate.assertNoError();
  }
}
