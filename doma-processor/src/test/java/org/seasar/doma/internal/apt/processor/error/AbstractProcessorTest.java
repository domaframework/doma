package org.seasar.doma.internal.apt.processor.error;

import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.message.Message;

class AbstractProcessorTest extends CompilerSupport {

  @TempDir Path sourceOutput;
  @TempDir Path classOutput;

  @BeforeEach
  void beforeEach() {
    setSourceOutput(sourceOutput);
    setClassOutput(classOutput);
    disableCompilationAssertion();
  }

  @AfterEach
  void afterEach() {
    enableCompilationAssertion();
  }

  @Test
  void testAptIllegalStateException() {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyProcessor(
            __ -> {
              throw new AptIllegalStateException("hoge");
            }));
    try {
      // javac throws an exception, but ecj doesn't.
      compile();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4039);
  }

  @Test
  void testRuntimeException() {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyProcessor(
            __ -> {
              throw new NullPointerException("hoge");
            }));
    try {
      // javac throws an exception, but ecj doesn't.
      compile();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }

  @Test
  void testAssertionError() {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyProcessor(
            __ -> {
              throw new AssertionError("hoge");
            }));
    try {
      // javac throws an exception, but ecj doesn't.
      compile();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }
}
