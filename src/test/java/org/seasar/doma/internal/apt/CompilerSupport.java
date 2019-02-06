package org.seasar.doma.internal.apt;

import java.io.IOException;
import java.util.List;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.aptina.unit.SourceNotGeneratedException;
import org.seasar.doma.message.Message;

public abstract class CompilerSupport {

  @RegisterExtension CompilerExtension compiler = new CompilerExtension();

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
    compiler.addOption(options);
  }

  protected void addProcessor(final Processor... processors) {
    compiler.addProcessor(processors);
  }

  protected void addCompilationUnit(final Class<?> clazz) {
    compiler.addCompilationUnit(clazz);
  }

  protected void compile() throws IOException {
    compiler.compile();
  }

  protected Boolean getCompiledResult() throws IllegalStateException {
    return compiler.getCompiledResult();
  }

  protected void assertGeneratedSource(Class<?> originalClass) throws Exception {
    compiler.assertGeneratedSource(originalClass);
  }

  protected String getExpectedContent() throws Exception {
    return compiler.getExpectedContent();
  }

  protected void assertMessage(Message message) {
    compiler.assertMessage(message);
  }

  protected void assertNoMessage() {
    compiler.assertNoMessage();
  }

  protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
    return compiler.getDiagnostics();
  }

  protected void assertEqualsGeneratedSource(final CharSequence expected, final String className)
      throws IOException {
    compiler.assertEqualsGeneratedSource(expected, className);
  }

  protected String getGeneratedSource(final String className)
      throws IllegalStateException, IOException, SourceNotGeneratedException {
    return compiler.getGeneratedSource(className);
  }
}
