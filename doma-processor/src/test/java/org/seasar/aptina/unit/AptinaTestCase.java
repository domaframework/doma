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
package org.seasar.aptina.unit;

import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.seasar.aptina.unit.AssertionUtils.*;
import static org.seasar.aptina.unit.IOUtils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import org.seasar.doma.internal.apt.CompilerKind;

/**
 * @author koichik
 */
public abstract class AptinaTestCase {

  final CompilerKind compilerKind;

  Locale locale;

  Charset charset;

  Writer out;

  final List<String> options = new ArrayList<>();

  final List<File> sourcePaths = new ArrayList<>();

  final List<Processor> processors = new ArrayList<>();

  final List<CompilationUnit> compilationUnits = new ArrayList<>();

  JavaCompiler javaCompiler;

  DiagnosticCollector<JavaFileObject> diagnostics;

  StandardJavaFileManager standardJavaFileManager;

  Boolean compiledResult;

  boolean compilationAssertion = true;

  protected AptinaTestCase() {
    String compiler = System.getProperty("compiler");
    compilerKind = determineCompilerKind(compiler);
  }

  private static CompilerKind determineCompilerKind(String compiler) {
    return switch (compiler) {
      case "javac" -> CompilerKind.JAVAC;
      case "eclipse" -> CompilerKind.ECLIPSE;
      default -> throw new IllegalArgumentException(compiler);
    };
  }

  public void setUp() {}

  public void tearDown() {
    reset();
  }

  protected CompilerKind getCompilerKind() {
    return compilerKind;
  }

  protected abstract Path getSourceOutput();

  protected abstract void setSourceOutput(Path sourceOutput);

  protected abstract Path getClassOutput();

  protected abstract void setClassOutput(Path classOutput);

  public void enableCompilationAssertion() {
    this.compilationAssertion = true;
  }

  public void disableCompilationAssertion() {
    this.compilationAssertion = false;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

  public void setLocale(final String locale) {
    assertNotEmpty("locale", locale);
    setLocale(new Locale(locale));
  }

  public Charset getCharset() {
    return charset;
  }

  public void setCharset(final Charset charset) {
    this.charset = charset;
  }

  public void setCharset(final String charset) {
    assertNotEmpty("charset", charset);
    setCharset(Charset.forName(charset));
  }

  public void setOut(final Writer out) {
    this.out = out;
  }

  public void addSourcePath(final File... sourcePaths) {
    assertNotEmpty("sourcePaths", sourcePaths);
    this.sourcePaths.addAll(asList(sourcePaths));
  }

  public void addSourcePath(final String... sourcePaths) {
    assertNotEmpty("sourcePaths", sourcePaths);
    for (final String path : sourcePaths) {
      this.sourcePaths.add(new File(path));
    }
  }

  public void addOption(final String... options) {
    assertNotEmpty("options", options);
    this.options.addAll(asList(options));
  }

  public void addProcessor(final Processor... processors) {
    assertNotEmpty("processors", processors);
    this.processors.addAll(asList(processors));
  }

  public void addCompilationUnit(final Class<?> clazz) {
    AssertionUtils.assertNotNull("clazz", clazz);
    addCompilationUnit(clazz.getName());
  }

  public void addCompilationUnit(final String className) {
    assertNotEmpty("className", className);
    compilationUnits.add(new FileCompilationUnit(className));
  }

  public void addCompilationUnit(final Class<?> clazz, final CharSequence source) {
    AssertionUtils.assertNotNull("clazz", clazz);
    assertNotEmpty("source", source);
    addCompilationUnit(clazz.getName(), source);
  }

  public void addCompilationUnit(final String className, final CharSequence source) {
    assertNotEmpty("className", className);
    assertNotEmpty("source", source);
    compilationUnits.add(new InMemoryCompilationUnit(className, source.toString()));
  }

  public void compile() throws IOException {
    addOption("-d", getClassOutput().toFile().getPath());

    javaCompiler = compilerKind.createJavaCompiler();
    diagnostics = new DiagnosticCollector<>();
    final DiagnosticListener<JavaFileObject> listener =
        new LoggingDiagnosticListener(diagnostics, locale);

    standardJavaFileManager = javaCompiler.getStandardFileManager(listener, locale, charset);
    standardJavaFileManager.setLocation(StandardLocation.SOURCE_PATH, sourcePaths);
    standardJavaFileManager.setLocation(
        StandardLocation.SOURCE_OUTPUT, List.of(getSourceOutput().toFile()));

    final CompilationTask task =
        javaCompiler.getTask(
            out, standardJavaFileManager, listener, options, null, getCompilationUnits());
    task.setProcessors(processors);
    compiledResult = task.call();
    compilationUnits.clear();
  }

  public Boolean getCompiledResult() throws IllegalStateException {
    assertCompiled();
    return compiledResult;
  }

  public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() throws IllegalStateException {
    assertCompiled();
    return diagnostics.getDiagnostics();
  }

  public List<Diagnostic<? extends JavaFileObject>> getDiagnostics(final Class<?> clazz) {
    assertCompiled();
    return DiagnosticUtils.getDiagnostics(getDiagnostics(), clazz);
  }

  public List<Diagnostic<? extends JavaFileObject>> getDiagnostics(final String className) {
    assertCompiled();
    return DiagnosticUtils.getDiagnostics(getDiagnostics(), className);
  }

  public List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final javax.tools.Diagnostic.Kind kind) {
    assertCompiled();
    return DiagnosticUtils.getDiagnostics(getDiagnostics(), kind);
  }

  public List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final Class<?> clazz, final javax.tools.Diagnostic.Kind kind) {
    assertCompiled();
    return DiagnosticUtils.getDiagnostics(getDiagnostics(), clazz, kind);
  }

  public List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
      final String className, final javax.tools.Diagnostic.Kind kind) {
    assertCompiled();
    return DiagnosticUtils.getDiagnostics(getDiagnostics(), className, kind);
  }

  public String getGeneratedSource(final Class<?> clazz)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotNull("clazz", clazz);
    assertCompiled();
    return getGeneratedSource(clazz.getName());
  }

  public String getGeneratedSource(final String className)
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

  public void assertEqualsByLine(final String expected, final String actual) {
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

  public void assertEqualsByLine(
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

  public void assertEqualsGeneratedSource(final CharSequence expected, final Class<?> clazz)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("expected", expected);
    assertNotNull("clazz", clazz);
    assertCompiled();
    assertEqualsGeneratedSource(expected, clazz.getName());
  }

  public void assertEqualsGeneratedSource(final CharSequence expected, final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("className", className);
    assertCompiled();
    final String actual = getGeneratedSource(className);
    assertNotNull("actual", actual);
    assertEqualsByLine(expected == null ? null : expected.toString(), actual);
  }

  public void assertEqualsGeneratedSourceWithFile(
      final File expectedSourceFile, final Class<?> clazz)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotNull("expectedSourceFile", expectedSourceFile);
    assertNotNull("clazz", clazz);
    assertCompiled();
    assertEqualsGeneratedSourceWithFile(expectedSourceFile, clazz.getName());
  }

  protected void assertEqualsGeneratedSourceWithFile(
      final File expectedSourceFile, final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotNull("expectedSourceFile", expectedSourceFile);
    assertNotEmpty("className", className);
    assertCompiled();
    assertEqualsGeneratedSource(readString(expectedSourceFile, charset), className);
  }

  public void assertEqualsGeneratedSourceWithFile(
      final String expectedSourceFilePath, final Class<?> clazz)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("expectedSourceFilePath", expectedSourceFilePath);
    assertNotNull("clazz", clazz);
    assertCompiled();
    assertEqualsGeneratedSourceWithFile(expectedSourceFilePath, clazz.getName());
  }

  public void assertEqualsGeneratedSourceWithFile(
      final String expectedSourceFilePath, final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("expectedSourceFilePath", expectedSourceFilePath);
    assertNotEmpty("className", className);
    assertCompiled();
    assertEqualsGeneratedSourceWithFile(new File(expectedSourceFilePath), className);
  }

  public void assertEqualsGeneratedSourceWithResource(
      final URL expectedResourceUrl, final Class<?> clazz)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotNull("expectedResourceUrl", expectedResourceUrl);
    assertNotNull("clazz", clazz);
    assertCompiled();
    assertEqualsGeneratedSourceWithResource(expectedResourceUrl, clazz.getName());
  }

  public void assertEqualsGeneratedSourceWithResource(
      final URL expectedResourceUrl, final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotNull("expectedResourceUrl", expectedResourceUrl);
    assertNotEmpty("className", className);
    assertCompiled();
    assertEqualsGeneratedSource(readFromResource(expectedResourceUrl), className);
  }

  public void assertEqualsGeneratedSourceWithResource(
      final String expectedResource, final Class<?> clazz)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("expectedResource", expectedResource);
    assertNotNull("clazz", clazz);
    assertCompiled();
    assertEqualsGeneratedSourceWithResource(expectedResource, clazz.getName());
  }

  public void assertEqualsGeneratedSourceWithResource(
      final String expectedResource, final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    assertNotEmpty("expectedResource", expectedResource);
    assertNotEmpty("className", className);
    assertCompiled();
    final URL url = Thread.currentThread().getContextClassLoader().getResource(expectedResource);
    if (url == null) {
      throw new FileNotFoundException(expectedResource);
    }
    assertEqualsGeneratedSourceWithResource(url, className);
  }

  public void reset() {
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

  List<JavaFileObject> getCompilationUnits() throws IOException {
    final List<JavaFileObject> result = new ArrayList<>(compilationUnits.size());
    for (final CompilationUnit compilationUnit : compilationUnits) {
      result.add(compilationUnit.getJavaFileObject());
    }
    return result;
  }

  void assertCompiled() throws IllegalStateException {
    if (compilationAssertion && compiledResult == null) {
      throw new IllegalStateException("not compiled");
    }
  }

  String readFromResource(final URL url) throws IOException {
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
  static class LoggingDiagnosticListener implements DiagnosticListener<JavaFileObject> {

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
  interface CompilationUnit {

    JavaFileObject getJavaFileObject() throws IOException;
  }

  /**
   * @author koichik
   */
  class FileCompilationUnit implements CompilationUnit {

    final String className;

    public FileCompilationUnit(final String className) {
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
  class InMemoryCompilationUnit implements CompilationUnit {

    final String className;

    final String source;

    public InMemoryCompilationUnit(final String className, final String source) {
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
