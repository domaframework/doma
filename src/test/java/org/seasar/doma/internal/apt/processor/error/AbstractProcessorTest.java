package org.seasar.doma.internal.apt.processor.error;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.CompilerSupport;
import org.seasar.doma.message.Message;

public class AbstractProcessorTest extends CompilerSupport {

  @BeforeEach
  protected void setUp() throws Exception {
    disableCompilationAssertion();
    addSourcePath("src/main/java");
    addSourcePath("src/test/java");
  }

  @AfterEach
  protected void tearDown() throws Exception {
    enableCompilationAssertion();
  }

  @Test
  public void testAptIllegalStateException() throws Exception {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyProcessor(
            __ -> {
              throw new AptIllegalStateException("hoge");
            }));
    try {
      compile();
      fail();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4039);
  }

  @Test
  public void testRuntimeException() throws Exception {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyProcessor(
            __ -> {
              throw new NullPointerException("hoge");
            }));
    try {
      compile();
      fail();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }

  @Test
  public void testAssertionError() throws Exception {
    Class<?> target = Person.class;
    addCompilationUnit(target);
    addProcessor(
        new MyProcessor(
            __ -> {
              throw new AssertionError("hoge");
            }));
    try {
      compile();
      fail();
    } catch (Exception ignored) {
    }
    assertMessage(Message.DOMA4016);
  }
}
