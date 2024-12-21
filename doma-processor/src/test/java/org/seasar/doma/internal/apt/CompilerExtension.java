package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.doma.message.Message;

public class CompilerExtension extends AptinaTestCase
    implements BeforeEachCallback, AfterEachCallback {

  Path sourceOutput;

  Path classOutput;

  @Override
  public Path getSourceOutput() {
    return sourceOutput;
  }

  @Override
  public void setSourceOutput(Path sourceOutput) {
    this.sourceOutput = sourceOutput;
  }

  @Override
  public Path getClassOutput() {
    return classOutput;
  }

  @Override
  public void setClassOutput(Path classOutput) {
    this.classOutput = classOutput;
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    super.setUp();
    addSourcePath("src/test/java");
    setCharset("UTF-8");
    setLocale(Locale.ENGLISH);
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+9"));
    addOption("-Adoma.resources.dir=src/test/resources");
  }

  @Override
  public void afterEach(ExtensionContext context) {
    TimeZone.setDefault(null);
    super.tearDown();
  }

  public void assertMessage(Message message) {
    List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
    if (diagnostics.size() == 1) {
      Message m = extractMessage(diagnostics.get(0));
      if (m == null) {
        fail();
      }
      if (message == m) {
        return;
      }
      fail("actual message id: " + m.name());
    }
    fail();
  }

  public void assertContainsMessage(Message message) {
    List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
    if (diagnostics.stream().map(this::extractMessage).noneMatch(m -> m == message)) {
      fail();
    }
  }

  private Message extractMessage(Diagnostic<? extends JavaFileObject> diagnostic) {
    String message = diagnostic.getMessage(getLocale());
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

  public void assertNoMessage() {
    List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics();
    if (!diagnostics.isEmpty()) {
      fail();
    }
  }

  @Override
  public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
    List<Diagnostic<? extends JavaFileObject>> results = new ArrayList<>();
    for (Diagnostic<? extends JavaFileObject> diagnostic : super.getDiagnostics()) {
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
}
