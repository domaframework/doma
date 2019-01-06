package org.seasar.doma.internal.apt.processor.error;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.message.Message;

public class AbstractProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    disableCompilationAssertion();
    addSourcePath("src/main/java");
    addSourcePath("src/test/java");
  }

  @Override
  protected void tearDown() throws Exception {
    enableCompilationAssertion();
    super.tearDown();
  }

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
