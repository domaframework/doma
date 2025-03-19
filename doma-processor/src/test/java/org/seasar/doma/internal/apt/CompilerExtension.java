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

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import javax.annotation.processing.Processor;
import org.junit.jupiter.api.extension.Extension;
import org.seasar.doma.message.Message;

public interface CompilerExtension extends Extension {

  void enableCompilationAssertion();

  void disableCompilationAssertion();

  void setSourceOutput(Path sourceOutput);

  void setClassOutput(Path classOutput);

  void addOption(String... options);

  void addProcessor(Processor... processors);

  void addCompilationUnit(Class<?>... classes);

  void addCompilationUnit(String className, CharSequence source);

  void compile() throws IOException;

  Boolean getCompiledResult() throws IllegalStateException;

  void assertEqualsGeneratedSourceWithResource(URL expectedResourceUrl, String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException;

  void assertMessage(Message message);

  void assertNoError();
}
