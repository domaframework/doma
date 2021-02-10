package org.seasar.doma.internal.apt;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.message.Message;

public abstract class CompilerSupport {

  @RegisterExtension final CompilerExtension compiler = new CompilerExtension();

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

  protected void addCompilationUnit(final Class<?> clazz) {
    compiler.addCompilationUnit(clazz);
  }

  protected void addResourceFileCompilationUnit(final String fqn) {
    String fileName = fqn.replace(".", "/") + ".java";
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      String source = reader.lines().collect(Collectors.joining("\n"));
      compiler.addCompilationUnit(fqn, source);
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

  protected void assertContainsMessage(Message message) {
    compiler.assertContainsMessage(message);
  }

  protected void assertNoMessage() {
    compiler.assertNoMessage();
  }
}
