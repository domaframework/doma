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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;
import static org.seasar.doma.internal.apt.AssertionUtils.assertNotEmpty;
import static org.seasar.doma.internal.apt.AssertionUtils.assertNotNull;
import static org.seasar.doma.internal.apt.IOUtils.closeSilently;
import static org.seasar.doma.internal.apt.IOUtils.readString;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.seasar.doma.message.Message;

/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
/**
 * This class was forked from <a
 * href="https://github.com/seasarorg/aptina/tree/1.0.0/aptina/aptina-unit">aptina-unit</a>.
 *
 * @author koichik
 */
class AptinaTestCase
    implements BeforeEachCallback, AfterEachCallback, ExecutionCondition, CompilerExtension {

  private final CompilerKind compilerKind;
  private Locale locale;
  private Charset charset;
  private Writer out;
  private final List<String> options = new ArrayList<>();
  private final List<File> sourcePaths = new ArrayList<>();
  private final List<Processor> processors = new ArrayList<>();
  private final List<CompilationUnit> compilationUnits = new ArrayList<>();
  private JavaCompiler javaCompiler;
  private DiagnosticCollector<JavaFileObject> diagnostics;
  private StandardJavaFileManager standardJavaFileManager;
  private Boolean compiledResult;
  private boolean compilationAssertion = true;
  private Path sourceOutput;
  private Path classOutput;

  AptinaTestCase() {
    String compiler = System.getProperty("compiler");
    compilerKind =
        switch (compiler) {
          case "javac" -> CompilerKind.JAVAC;
          case "ecj" -> CompilerKind.ECJ;
          default -> throw new IllegalStateException(compiler);
        };
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    addOption("-Adoma.test.unit=true");
    addSourcePath("src/test/java");
    addSourcePath("src/test/resources");
    charset = StandardCharsets.UTF_8;
    locale = Locale.ENGLISH;
  }

  @Override
  public void afterEach(ExtensionContext context) {
    locale = null;
    charset = null;
    out = null;
    options.clear();
    sourcePaths.clear();
    processors.clear();
    compilationUnits.clear();
    javaCompiler = null;
    diagnostics = null;
    standardJavaFileManager = null;
    compiledResult = null;
  }

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    Optional<Run> optional = findAnnotation(context.getElement(), Run.class);
    if (optional.isEmpty()) {
      return enabled("@Run is not present");
    }
    Run run = optional.get();
    if (isRunnable(run, compilerKind)) {
      return enabled("runnable: " + compilerKind);
    }
    return disabled("not runnable");
  }

  private boolean isRunnable(Run run, CompilerKind compiler) {
    List<CompilerKind> onlyIf = List.of(run.onlyIf());
    List<CompilerKind> unless = List.of(run.unless());

    if (!onlyIf.isEmpty()) {
      return onlyIf.contains(compiler);
    } else {
      if (!unless.isEmpty()) {
        return !unless.contains(compiler);
      } else {
        return true;
      }
    }
  }

  @Override
  public void enableCompilationAssertion() {
    this.compilationAssertion = true;
  }

  @Override
  public void disableCompilationAssertion() {
    this.compilationAssertion = false;
  }

  @Override
  public void setSourceOutput(Path sourceOutput) {
    this.sourceOutput = sourceOutput;
  }

  @Override
  public void setClassOutput(Path classOutput) {
    this.classOutput = classOutput;
  }

  void addSourcePath(final String... sourcePaths) {
    assertNotEmpty("sourcePaths", sourcePaths);
    for (final String path : sourcePaths) {
      this.sourcePaths.add(new File(path));
    }
  }

  @Override
  public void addOption(final String... options) {
    assertNotEmpty("options", options);
    this.options.addAll(asList(options));
  }

  @Override
  public void addProcessor(final Processor... processors) {
    assertNotEmpty("processors", processors);
    this.processors.addAll(asList(processors));
  }

  @Override
  public void addCompilationUnit(final Class<?>... classes) {
    assertNotNull("clazz", classes);
    for (final Class<?> clazz : classes) {
      addCompilationUnit(clazz.getName());
    }
  }

  private void addCompilationUnit(final String className) {
    assertNotEmpty("className", className);
    compilationUnits.add(new InputCompilationUnit(className));
  }

  @Override
  public void addCompilationUnit(final String className, final CharSequence source) {
    assertNotEmpty("className", className);
    assertNotEmpty("source", source);
    compilationUnits.add(new OutputCompilationUnit(className, source.toString()));
  }

  @Override
  public void compile() throws IOException {
    if (sourceOutput == null) {
      throw new IllegalStateException("sourceOutput is not set");
    }
    if (classOutput == null) {
      throw new IllegalStateException("classOutput is not set");
    }

    addOption("-d", classOutput.toFile().getPath());

    javaCompiler = compilerKind.createJavaCompiler();
    diagnostics = new DiagnosticCollector<>();
    final DiagnosticListener<JavaFileObject> listener =
        new LoggingDiagnosticListener(diagnostics, locale);

    standardJavaFileManager = javaCompiler.getStandardFileManager(listener, locale, charset);
    standardJavaFileManager.setLocation(StandardLocation.SOURCE_PATH, sourcePaths);
    standardJavaFileManager.setLocation(
        StandardLocation.SOURCE_OUTPUT, List.of(sourceOutput.toFile()));

    final CompilationTask task =
        javaCompiler.getTask(
            out, standardJavaFileManager, listener, options, null, getCompilationUnits());
    task.setProcessors(processors);
    compiledResult = task.call();
    compilationUnits.clear();
  }

  @Override
  public Boolean getCompiledResult() throws IllegalStateException {
    assertCompiled();
    return compiledResult;
  }

  private List<Diagnostic<? extends JavaFileObject>> getDiagnostics() throws IllegalStateException {
    assertCompiled();
    List<Diagnostic<? extends JavaFileObject>> results = new ArrayList<>();
    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
      switch (diagnostic.getKind()) {
        case ERROR:
        case WARNING:
        case MANDATORY_WARNING:
          results.add(diagnostic);
          break;
        default:
          // do nothing
      }
    }
    return results;
  }

  private String getGeneratedSource(final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("className", className);
    assertCompiled();
    final JavaFileObject javaFileObject =
        standardJavaFileManager.getJavaFileForInput(
            StandardLocation.SOURCE_OUTPUT, className, Kind.SOURCE);
    if (javaFileObject == null) {
      throw new SourceNotGeneratedException(className);
    }
    final CharSequence content = javaFileObject.getCharContent(true);
    if (content == null) {
      throw new SourceNotGeneratedException(className);
    }
    return content.toString();
  }

  @Override
  public void assertEqualsGeneratedSourceWithResource(URL expectedResourceUrl, String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotNull("expectedResourceUrl", expectedResourceUrl);
    assertNotEmpty("className", className);
    assertCompiled();
    assertEqualsGeneratedSource(readFromResource(expectedResourceUrl), className);
  }

  private void assertEqualsGeneratedSource(final CharSequence expected, final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("className", className);
    assertCompiled();
    final String actual = getGeneratedSource(className);
    assertNotNull("actual", actual);
    try {
      assertEqualsByLine(expected == null ? null : expected.toString(), actual);
    } catch (AssertionError error) {
      System.out.println(actual);
      throw error;
    }
  }

  private void assertEqualsByLine(final String expected, final String actual) {
    if (expected == null || actual == null) {
      assertEquals(expected, actual);
      return;
    }
    final BufferedReader expectedReader = new BufferedReader(new StringReader(expected));
    final BufferedReader actualReader = new BufferedReader(new StringReader(actual));
    try {
      assertEqualsByLine(expectedReader, actualReader);
    } catch (final IOException ignore) {
      // unreachable
    } finally {
      closeSilently(expectedReader);
      closeSilently(actualReader);
    }
  }

  private void assertEqualsByLine(
      final BufferedReader expectedReader, final BufferedReader actualReader) throws IOException {
    String expectedLine;
    String actualLine;
    int lineNo = 0;
    while ((expectedLine = expectedReader.readLine()) != null) {
      ++lineNo;
      actualLine = actualReader.readLine();
      assertEquals(expectedLine, actualLine, "line:" + lineNo);
    }
    ++lineNo;
    assertNull(actualReader.readLine(), "line:" + lineNo);
  }

  @Override
  public void assertMessage(Message message) {
    assertCompiled();
    var unmatchedMessages = new ArrayList<Message>();
    for (Diagnostic<? extends JavaFileObject> diagnostic : getDiagnostics()) {
      Message m = extractMessage(diagnostic);
      if (m == message) {
        // found
        return;
      }
      unmatchedMessages.add(m);
    }
    fail("unmatched messages: " + unmatchedMessages);
  }

  private Message extractMessage(Diagnostic<? extends JavaFileObject> diagnostic) {
    String message = diagnostic.getMessage(locale);
    int start = message.indexOf('[');
    int end = message.indexOf(']');
    if (start > -1 && end > -1) {
      String code = message.substring(start + 1, end);
      if (code.startsWith("DOMA")) {
        return Enum.valueOf(Message.class, code);
      }
    }
    return null;
  }

  @Override
  public void assertNoError() {
    assertCompiled();
    boolean match = getDiagnostics().stream().anyMatch(d -> d.getKind() == Diagnostic.Kind.ERROR);
    if (match) {
      fail();
    }
  }

  private List<JavaFileObject> getCompilationUnits() throws IOException {
    final List<JavaFileObject> result = new ArrayList<>(compilationUnits.size());
    for (final CompilationUnit compilationUnit : compilationUnits) {
      result.add(compilationUnit.getJavaFileObject());
    }
    return result;
  }

  private void assertCompiled() throws IllegalStateException {
    if (compilationAssertion && compiledResult == null) {
      throw new IllegalStateException("not compiled");
    }
  }

  private String readFromResource(final URL url) throws IOException {
    final InputStream is = url.openStream();
    try {
      return readString(is, charset);
    } finally {
      closeSilently(is);
    }
  }

  /**
   * @author koichik
   */
  private static class LoggingDiagnosticListener implements DiagnosticListener<JavaFileObject> {

    final DiagnosticListener<JavaFileObject> listener;

    final Locale locale;

    LoggingDiagnosticListener(
        final DiagnosticListener<JavaFileObject> listener, final Locale locale) {
      this.listener = listener;
      this.locale = locale;
    }

    @Override
    public void report(final Diagnostic<? extends JavaFileObject> diagnostic) {
      System.err.println(diagnostic.getMessage(locale));
      listener.report(diagnostic);
    }
  }

  /**
   * @author koichik
   */
  private interface CompilationUnit {

    JavaFileObject getJavaFileObject() throws IOException;
  }

  /**
   * @author koichik
   */
  private class InputCompilationUnit implements CompilationUnit {

    final String className;

    InputCompilationUnit(final String className) {
      this.className = className;
    }

    @Override
    public JavaFileObject getJavaFileObject() throws IOException {
      return standardJavaFileManager.getJavaFileForInput(
          StandardLocation.SOURCE_PATH, className, Kind.SOURCE);
    }
  }

  /**
   * @author koichik
   */
  private class OutputCompilationUnit implements CompilationUnit {

    final String className;

    final String source;

    OutputCompilationUnit(final String className, final String source) {
      this.className = className;
      this.source = source;
    }

    @Override
    public JavaFileObject getJavaFileObject() throws IOException {
      final JavaFileObject javaFileObject =
          standardJavaFileManager.getJavaFileForOutput(
              StandardLocation.SOURCE_OUTPUT, className, Kind.SOURCE, null);
      final Writer writer = javaFileObject.openWriter();
      try {
        writer.write(source);
      } finally {
        closeSilently(writer);
      }
      return javaFileObject;
    }
  }
}
