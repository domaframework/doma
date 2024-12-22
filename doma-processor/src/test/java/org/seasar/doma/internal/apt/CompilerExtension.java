package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.doma.message.Message;

public class CompilerExtension extends AptinaTestCase
    implements BeforeEachCallback, AfterEachCallback, ExecutionCondition {

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
    for (Diagnostic<? extends JavaFileObject> diagnostic : getDiagnostics()) {
      Message m = extractMessage(diagnostic);
      if (m == message) {
        // found
        return;
      }
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

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    Optional<Run> optional = findAnnotation(context.getElement(), Run.class);
    if (optional.isEmpty()) {
      return enabled("@Run is not present");
    }
    Run run = optional.get();
    CompilerKind compilerKind = getCompilerKind();
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
}
